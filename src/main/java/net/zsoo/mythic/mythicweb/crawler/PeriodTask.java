package net.zsoo.mythic.mythicweb.crawler;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.var;
import lombok.extern.slf4j.Slf4j;
import net.zsoo.mythic.mythicweb.battlenet.wow.DataAPI;
import net.zsoo.mythic.mythicweb.battlenet.wow.StaticDataAPI;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.Dungeon;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.DungeonIndex;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.KeyIdName;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.PeriodIndex;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.PlayableSpec;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.Realm;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.RealmIndex;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.SpecIndex;
import net.zsoo.mythic.mythicweb.dto.MythicDungeon;
import net.zsoo.mythic.mythicweb.dto.MythicDungeonRepository;
import net.zsoo.mythic.mythicweb.dto.MythicSeasonPeriod;
import net.zsoo.mythic.mythicweb.dto.MythicSeasonPeriodRepository;
import net.zsoo.mythic.mythicweb.dto.PlayerRealm;
import net.zsoo.mythic.mythicweb.dto.PlayerRealmRepository;
import net.zsoo.mythic.mythicweb.dto.PlayerSpec;
import net.zsoo.mythic.mythicweb.dto.PlayerSpecRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class PeriodTask {
    private final CrawlerCommonService crawlerSerivce;
    private final DataAPI dataApi;
    private final StaticDataAPI staticApi;
    private int period = 0;
    private int season = 0;

    private final MythicSeasonPeriodRepository periodRepo;
    private final PlayerRealmRepository realmRepo;
    private final MythicDungeonRepository dungeonRepo;
    private final PlayerSpecRepository specRepo;

    @Value("${mythic.crawler.period.cron:}")
    private String cronString;

    @Scheduled(cron = "${mythic.crawler.period.cron:-}")
    @PostConstruct
    public void updatePeriod() {
        if (cronString == null || cronString.equals("")) {
            return;
        }
        String accessToken = crawlerSerivce.getAccessToken();
        log.debug("token: {}", accessToken);

        PeriodIndex periodIndex = dataApi.periodIndex(accessToken);
        period = periodIndex.getCurrentPeriod().getId();

        Optional<MythicSeasonPeriod> seasonPeriod = periodRepo.findTopByPeriodOrderBySeasonAsc(period);
        if (seasonPeriod.isPresent()) {
            season = seasonPeriod.get().getSeason();
        }

        RealmIndex realmIndex = dataApi.realmIndex(accessToken);
        for (Realm realm : realmIndex.getRealms()) {
            saveRealm(realm);
        }

        DungeonIndex dungeonIndex = dataApi.dungeonIndex(accessToken);
        for (KeyIdName dungeonId : dungeonIndex.getDungeons()) {
            Dungeon dungeon = dataApi.dungeon(dungeonId.getId(), accessToken);
            saveDungeon(dungeon);
        }

        SpecIndex specIndex = staticApi.specIndex(accessToken);
        specIndex.getCharacterSpecializations().forEach(spec -> {
            PlayableSpec playableSpec = staticApi.playableSpec(spec.getId(), accessToken);
            savePlayableSpec(playableSpec);
        });
    }

    public int getPeriod() {
        if (period == 0) {
            updatePeriod();
        }
        return period;
    }

    public int getSeason() {
        if (season == 0) {
            updatePeriod();
        }
        return season;
    }

    private void saveRealm(Realm src) {
        PlayerRealm realm = new PlayerRealm();
        realm.setRealmId(src.getId());
        realm.setRealmName(src.getName());
        realm.setRealmSlug(src.getSlug());
        // realm.setConnectedRealm(false);
        // realmRepo.save(realm);
    }

    private void saveDungeon(Dungeon src) {
        MythicDungeon dungeon = new MythicDungeon();
        dungeon.setDungeonId(src.getId());
        dungeon.setDungeonName(src.getName());
        dungeon.setZone(src.getZone().getSlug());
        var upgrades = src.getKeystoneUpgrades();
        dungeon.setUpgrade1(upgrades.stream().filter(u -> u.getUpgradeLevel() == 1)
                .mapToInt(u -> u.getQualifyingDuration()).max().orElse(0));
        dungeon.setUpgrade2(upgrades.stream().filter(u -> u.getUpgradeLevel() == 2)
                .mapToInt(u -> u.getQualifyingDuration()).max().orElse(0));
        dungeon.setUpgrade3(upgrades.stream().filter(u -> u.getUpgradeLevel() == 3)
                .mapToInt(u -> u.getQualifyingDuration()).max().orElse(0));
        dungeonRepo.save(dungeon);
    }

    private void savePlayableSpec(PlayableSpec src) {
        PlayerSpec spec = new PlayerSpec();
        spec.setId(src.getId());
        spec.setName(src.getName());
        spec.setClassName(src.getPlayableClass().getName());
        spec.setRole(src.getRole().getType());
        specRepo.save(spec);
    }
}
