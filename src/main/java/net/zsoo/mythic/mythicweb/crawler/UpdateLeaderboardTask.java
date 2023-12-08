package net.zsoo.mythic.mythicweb.crawler;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.zsoo.mythic.mythicweb.battlenet.wow.DataAPI;
import net.zsoo.mythic.mythicweb.battlenet.wow.ProfileAPI;
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
    @Qualifier("accessToken")
    private final Supplier<String> accessTokenSupplier;
    private final PeriodTask periodTask;
    private final DataAPI dataApi;
    private final PlayerRealmRepository realmRepo;

    @Scheduled(cron = "${mythic.crawler.leaderboard.cron:-}")
    public void onTimer() {
        long now = System.currentTimeMillis();
        log.debug("time: {}", now);

        int period = periodTask.getPeriod();

        String accessToken = accessTokenSupplier.get();
        log.debug("token: {}", accessToken);

        List<PlayerRealm> realms = realmRepo.findAll();
        realms.stream()
                .filter(r -> r.isConnectedRealm())
                .forEach(realm -> updateRealm(realm, accessToken, period));

        log.debug("done!");
    }

    private void updateRealm(PlayerRealm realm, String accessToken, int period) {
        MythicLeaderboardIndex index = dataApi.mythicLeaderboardIndex(realm.getRealmId(), accessToken);
        if (index.getCurrentLeaderboards() == null) {
            return;
        }
        index.getCurrentLeaderboards().forEach(id -> updateRealmDungeon(realm, id.getId(), period, accessToken));
    }

    private void updateRealmDungeon(PlayerRealm realm, int dungeonId, int period, String accessToken) {
        MythicLeaderboardPeriod leaderboard = dataApi.mythicLeaderboardPeriod(realm.getRealmId(),
                dungeonId, period, accessToken);
        if (leaderboard.getLeadingGroups() == null) {
            return;
        }
        leaderboard.getLeadingGroups().forEach(run -> {
            KeyIdName dungeon = new KeyIdName();
            dungeon.setId(dungeonId);
            run.setDungeon(dungeon);
            crawlerService.saveRun(run);
        });
    }
}
