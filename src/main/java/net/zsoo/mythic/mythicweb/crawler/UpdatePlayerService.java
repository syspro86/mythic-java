package net.zsoo.mythic.mythicweb.crawler;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.zsoo.mythic.mythicweb.battlenet.wow.ProfileAPI;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.BestRun;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.MythicKeystoneProfile;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.MythicKeystoneProfileSeason;
import net.zsoo.mythic.mythicweb.dto.MythicPeriodRepository;
import net.zsoo.mythic.mythicweb.dto.PlayerRealm;
import net.zsoo.mythic.mythicweb.dto.PlayerRealmRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdatePlayerService {
    private final ProfileAPI wowApi;
    private final CrawlerCommonService crawlerService;
    private final PlayerRealmRepository realmRepo;
    private final MythicPeriodRepository periodRepo;

    @Retryable(backoff = @Backoff(delay = 60 * 1000))
    public void updatePlayer(String playerRealm, String playerName) {
        String accessToken = crawlerService.getAccessToken();
        log.debug("token: {}", accessToken);
        int curSeason = crawlerService.getSeason();

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
}
