package net.zsoo.mythic.mythicweb.crawler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.zsoo.mythic.mythicweb.battlenet.wow.DataAPI;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.KeyIdName;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.MythicLeaderboardIndex;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.MythicLeaderboardPeriod;
import net.zsoo.mythic.mythicweb.dto.PlayerRealm;
import net.zsoo.mythic.mythicweb.dto.PlayerRealmRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateLeaderboardTask {
    private final CrawlerCommonService crawlerService;
    private final PeriodTask periodTask;
    private final DataAPI dataApi;
    private final PlayerRealmRepository realmRepo;

    @Scheduled(cron = "${mythic.crawler.leaderboard.cron:-}")
    public void onTimer() {
        long now = System.currentTimeMillis();
        log.debug("time: {}", now);

        String accessToken = crawlerService.getAccessToken();
        log.debug("token: {}", accessToken);

        List<PlayerRealm> realms = realmRepo.findAll();
        for (PlayerRealm realm : realms) {
            if (!realm.isConnectedRealm()) {
                continue;
            }
            updateRealm(realm, accessToken);
        }

        log.debug("done!");
    }

    private void updateRealm(PlayerRealm realm, String accessToken) {
        MythicLeaderboardIndex index = dataApi.mythicLeaderboardIndex(realm.getRealmId(), accessToken);
        if (index.getCurrentLeaderboards() == null) {
            return;
        }
        index.getCurrentLeaderboards().forEach(id -> updateRealmDungeon(realm, id.getId(), accessToken));
    }

    private void updateRealmDungeon(PlayerRealm realm, int dungeonId, String accessToken) {
        int period = periodTask.getPeriod();
        int season = periodTask.getSeason();
        MythicLeaderboardPeriod leaderboard = dataApi.mythicLeaderboardPeriod(realm.getRealmId(),
                dungeonId, period, accessToken);
        if (leaderboard.getLeadingGroups() == null) {
            return;
        }
        leaderboard.getLeadingGroups().forEach(run -> {
            KeyIdName dungeon = new KeyIdName();
            dungeon.setId(dungeonId);
            run.setDungeon(dungeon);
            crawlerService.saveRun(season, period, run);
        });
    }
}
