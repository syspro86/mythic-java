package net.zsoo.mythic.mythicweb.crawler;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.zsoo.mythic.mythicweb.battlenet.wow.ProfileAPI;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.BestRun;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.MythicKeystoneProfile;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.MythicKeystoneProfileSeason;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.RunMember;
import net.zsoo.mythic.mythicweb.crawler.CrawlerRepository.NextPlayer;
import net.zsoo.mythic.mythicweb.dto.PlayerRealm;
import net.zsoo.mythic.mythicweb.dto.PlayerRealmRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdatePlayerTask {
    private final CrawlerRepository crawlerRepo;
    @Qualifier("apiToken")
    private final Supplier<String> apiTokenSupplier;
    private final ProfileAPI wowApi;
    private final PlayerRealmRepository realmRepo;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(cron = "${mythic.crawler.cron:-}")
    public void onTimer() {
        long now = System.currentTimeMillis();
        log.debug("time: {}", now);

        String apiToken = apiTokenSupplier.get();
        log.debug("token: {}", apiToken);

        for (int i = 0; i < 100; i++) {
            NextPlayer nextPlayer = getNextPlayer(now);
            updatePlayer(apiToken, nextPlayer.getPlayerRealm(), nextPlayer.getPlayerName());
        }
    }

    private void updatePlayer(String apiToken, String playerRealm, String playerName) {
        log.debug("player: {}-{}", playerName, playerRealm);
        PlayerRealm realm = realmRepo.findByRealmName(playerRealm)
                .orElseThrow(() -> new RuntimeException("invalid realm name " + playerRealm));

        MythicKeystoneProfile result = wowApi.mythicKeystoneProfile(realm.getRealmSlug(), playerName, apiToken);
        if (result.getSeasons() == null) {
            return;
        }
        result.getSeasons().forEach(season -> {
            MythicKeystoneProfileSeason seasonResult = wowApi.mythicKeystoneProfileSeason(realm.getRealmSlug(),
                    playerName, season.getId(), apiToken);
            log.debug("season: {}", seasonResult);
            for (BestRun run : seasonResult.getBestRuns()) {
                log.debug("run: {}", run);
                for (RunMember member : run.getMembers()) {
                    log.debug("member: {}", member);
                }
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
