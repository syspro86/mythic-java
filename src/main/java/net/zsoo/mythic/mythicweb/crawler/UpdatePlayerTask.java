package net.zsoo.mythic.mythicweb.crawler;

import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.zsoo.mythic.mythicweb.battlenet.wow.ProfileAPI;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.BestRun;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.MythicKeystoneProfile;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.MythicKeystoneProfileSeason;
import net.zsoo.mythic.mythicweb.crawler.CrawlerRepository.NextPlayer;
import net.zsoo.mythic.mythicweb.dto.MythicPeriodRepository;
import net.zsoo.mythic.mythicweb.dto.MythicPlayer;
import net.zsoo.mythic.mythicweb.dto.MythicPlayerId;
import net.zsoo.mythic.mythicweb.dto.MythicPlayerRepository;
import net.zsoo.mythic.mythicweb.dto.PlayerRealm;
import net.zsoo.mythic.mythicweb.dto.PlayerRealmRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdatePlayerTask {
    private final CrawlerRepository crawlerRepo;
    private final CrawlerCommonService crawlerService;
    private final ProfileAPI wowApi;
    private final PlayerRealmRepository realmRepo;
    private final MythicPeriodRepository periodRepo;
    private final MythicPlayerRepository playerRepo;

    @Scheduled(cron = "${mythic.crawler.player.cron:-}")
    public void onTimer() {
        long now = System.currentTimeMillis();
        log.debug("time: {}", now);
        long endTime = now + 55000;

        String accessToken = crawlerService.getAccessToken();
        log.debug("token: {}", accessToken);

        int season = crawlerService.getSeason();

        while (true) {
            NextPlayer nextPlayer = getNextPlayer(now);
            try {
                updatePlayer(season, nextPlayer.getPlayerRealm(), nextPlayer.getPlayerName(), accessToken);
            } catch (Exception e) {
                log.error("갱신 오류", e);
            }
            setPlayerUpdateTime(nextPlayer);

            if (System.currentTimeMillis() >= endTime) {
                break;
            }
        }
    }

    private NextPlayer getNextPlayer(long now) {
        Optional<NextPlayer> player = crawlerRepo.findNextUpdatePlayer1();
        if (!player.isPresent()) {
            player = crawlerRepo.findNextUpdatePlayer2();
        }
        if (!player.isPresent()) {
            player = crawlerRepo.findNextUpdatePlayer3(now - 1000 * 60 * 60 * 24);
        }
        if (!player.isPresent()) {
            player = crawlerRepo.findNextUpdatePlayer4();
        }
        return player.get();
    }

    private void updatePlayer(int curSeason, String playerRealm, String playerName, String accessToken) {
        log.debug("player: {}-{}", playerName, playerRealm);
        PlayerRealm realm = realmRepo.findByRealmName(playerRealm)
                .orElseThrow(() -> new RuntimeException("invalid realm name " + playerRealm));

        MythicKeystoneProfile result = wowApi.mythicKeystoneProfile(realm.getRealmSlug(), playerName, accessToken);
        if (result == null) {
            return;
        }
        var period = result.getCurrentPeriod();
        if (period != null && period.getBestRuns() != null) {
            period.getBestRuns().forEach(run -> {
                log.debug("run: {}", run);
                crawlerService.saveRun(curSeason, period.getPeriod().getId(), run);
            });
        }
        var seasons = result.getSeasons();
        if (seasons != null) {
            seasons.forEach(season -> {
                MythicKeystoneProfileSeason seasonResult = wowApi.mythicKeystoneProfileSeason(realm.getRealmSlug(),
                        playerName, season.getId(), accessToken);
                if (seasonResult == null || seasonResult.getBestRuns() == null) {
                    return;
                }
                log.debug("season: {}", seasonResult);
                for (BestRun run : seasonResult.getBestRuns()) {
                    log.debug("run: {}", run);
                    crawlerService.saveRun(season.getId(), periodRepo.findByTimestamp(run.getCompletedTimestamp())
                            .map(p -> p.getPeriod()).orElse(0), run);
                }
            });
        }
    }

    private void setPlayerUpdateTime(NextPlayer nextPlayer) {
        var player = playerRepo.findById(new MythicPlayerId(nextPlayer.getPlayerRealm(), nextPlayer.getPlayerName()))
                .orElseGet(() -> {
                    var p = new MythicPlayer();
                    p.setPlayerRealm(nextPlayer.getPlayerRealm());
                    p.setPlayerName(nextPlayer.getPlayerName());
                    p.setSpecId(0);
                    p.setClassName("null");
                    p.setSpecName("null");
                    return p;
                });
        // TODO
        // player.setSpecId( xx );
        // player.setClassName( xx );
        // player.setSpecName( xx );
        player.setLastUpdateTs(System.currentTimeMillis());
        playerRepo.save(player);
    }

}
