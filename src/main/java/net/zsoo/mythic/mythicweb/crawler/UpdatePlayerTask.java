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
import net.zsoo.mythic.mythicweb.dto.MythicSeasonRepository;
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
    private final MythicSeasonRepository seasonRepo;
    private final MythicPeriodRepository periodRepo;

    @Scheduled(cron = "${mythic.crawler.player.cron:-}")
    public void onTimer() {
        long now = System.currentTimeMillis();
        log.debug("time: {}", now);

        String accessToken = crawlerService.getAccessToken();
        log.debug("token: {}", accessToken);

        long endTime = now - now % 60000 + 55000;

        while (true) {
            NextPlayer nextPlayer = getNextPlayer(now);
            updatePlayer(accessToken, nextPlayer.getPlayerRealm(), nextPlayer.getPlayerName());

            if (System.currentTimeMillis() >= endTime) {
                break;
            }
        }
    }

    private void updatePlayer(String accessToken, String playerRealm, String playerName) {
        log.debug("player: {}-{}", playerName, playerRealm);
        PlayerRealm realm = realmRepo.findByRealmName(playerRealm)
                .orElseThrow(() -> new RuntimeException("invalid realm name " + playerRealm));

        MythicKeystoneProfile result = wowApi.mythicKeystoneProfile(realm.getRealmSlug(), playerName, accessToken);
        if (result.getSeasons() == null) {
            return;
        }
        var period = result.getCurrentPeriod();
        period.getBestRuns().forEach(run -> {
            log.debug("run: {}", run);
            crawlerService.saveRun(crawlerService.getSeason(), period.getPeriod().getId(), run);
        });
        result.getSeasons().forEach(season -> {
            MythicKeystoneProfileSeason seasonResult = wowApi.mythicKeystoneProfileSeason(realm.getRealmSlug(),
                    playerName, season.getId(), accessToken);
            log.debug("season: {}", seasonResult);
            for (BestRun run : seasonResult.getBestRuns()) {
                log.debug("run: {}", run);
                crawlerService.saveRun(season.getId(), periodRepo.findByTimestamp(run.getCompletedTimestamp())
                        .map(p -> p.getPeriod()).orElse(0), run);
            }
        });
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
}
