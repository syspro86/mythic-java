package net.zsoo.mythic.mythicweb.crawler;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.zsoo.mythic.mythicweb.battlenet.oauth.BattlenetOAuth;
import net.zsoo.mythic.mythicweb.battlenet.wow.DataAPI;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.BestRun;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.PeriodIndex;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.RunMemberProfile;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.SeasonIndex;
import net.zsoo.mythic.mythicweb.dto.MythicDungeon;
import net.zsoo.mythic.mythicweb.dto.MythicDungeonRepository;
import net.zsoo.mythic.mythicweb.dto.MythicRecord;
import net.zsoo.mythic.mythicweb.dto.MythicRecordPlayer;
import net.zsoo.mythic.mythicweb.dto.MythicRecordRepository;
import net.zsoo.mythic.mythicweb.dto.PlayerRealm;
import net.zsoo.mythic.mythicweb.dto.PlayerRealmRepository;
import net.zsoo.mythic.mythicweb.dto.PlayerSpec;
import net.zsoo.mythic.mythicweb.dto.PlayerSpecRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlerCommonService {
    private static final String ACCESS_TOKEN = "access_token";
    private final BattlenetOAuth oauth;
    private static String accessTokenCache;

    private final ApplicationEventPublisher publisher;

    private final MythicRecordRepository recordRepo;
    private final MythicDungeonRepository dungeonRepo;
    private final PlayerRealmRepository realmRepo;
    private final PlayerSpecRepository specRepo;
    private final DataAPI dataApi;

    private Map<Integer, MythicDungeon> dungeonCache = new HashMap<>();
    private Map<Integer, PlayerRealm> realmCache = new HashMap<>();
    private Map<Integer, PlayerSpec> specCache = new HashMap<>();

    private Set<String> idCache = ConcurrentHashMap.newKeySet();

    private int period = 0;
    private int season = 0;

    private void updateSeasonPeriod() {
        String accessToken = getAccessToken();
        log.debug("token: {}", accessToken);

        PeriodIndex periodIndex = dataApi.periodIndex(accessToken);
        period = periodIndex.getCurrentPeriod().getId();

        SeasonIndex seasonIndex = dataApi.seasonIndex(accessToken);
        season = seasonIndex.getCurrentSeason().getId();
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void onTokenRefresh() {
        accessTokenCache = null;
        period = 0;
        season = 0;
        idCache.clear();
    }

    @Retryable(Exception.class)
    public String getAccessToken() {
        if (accessTokenCache == null) {
            accessTokenCache = Optional.ofNullable(oauth.token())
                    .map(m -> m.get(ACCESS_TOKEN))
                    .map(m -> "Bearer " + m.toString())
                    .orElse(null);
        }
        return accessTokenCache;
    }

    @Retryable(Exception.class)
    public int getSeason() {
        if (season == 0) {
            updateSeasonPeriod();
        }
        return season;
    }

    @Retryable(Exception.class)
    public int getPeriod() {
        if (period == 0) {
            updateSeasonPeriod();
        }
        return period;
    }

    public void saveRun(int season, int period, BestRun run) {
        dungeonCache.computeIfAbsent(run.getDungeon().getId(), id -> dungeonRepo.findById(id).orElse(null));
        MythicDungeon dungeon = dungeonCache.get(run.getDungeon().getId());

        MythicRecord record = new MythicRecord();
        record.setSeason(season);
        record.setPeriod(period);
        record.setDungeonId(run.getDungeon().getId());
        record.setDuration(run.getDuration());
        record.setCompletedTimestamp(run.getCompletedTimestamp());
        record.setKeystoneLevel(run.getKeystoneLevel());
        if (record.getDuration() <= dungeon.getUpgrade3()) {
            record.setKeystoneUpgrade(3);
        } else if (record.getDuration() <= dungeon.getUpgrade2()) {
            record.setKeystoneUpgrade(2);
        } else if (record.getDuration() <= dungeon.getUpgrade1()) {
            record.setKeystoneUpgrade(1);
        } else {
            record.setKeystoneUpgrade(-1);
        }
        if (run.getMythicRating() != null) {
            record.setMythicRating(run.getMythicRating().getRating());
        }

        record.setPlayers(run.getMembers().stream().map(member -> {
            MythicRecordPlayer player = new MythicRecordPlayer();
            RunMemberProfile profile = Optional.ofNullable(member.getCharacter()).orElse(member.getProfile());
            player.setRecord(record);
            if (profile.getName() == null || profile.getName().equals("")) {
                player.setPlayerName("?");
            } else {
                player.setPlayerName(profile.getName());
            }

            realmCache.computeIfAbsent(profile.getRealm().getId(), id -> realmRepo.findById(id).orElse(null));
            PlayerRealm realm = realmCache.get(profile.getRealm().getId());
            player.setPlayerRealm(realm.getRealmName());

            specCache.computeIfAbsent(member.getSpecialization().getId(), id -> specRepo.findById(id).orElse(null));
            PlayerSpec spec = specCache.get(member.getSpecialization().getId());
            player.setSpecId(spec.getId());
            player.setSpecName(spec.getName());
            player.setClassName(spec.getClassName());
            player.setRoleName(spec.getRole());
            player.setPlayerId(Integer.parseInt(profile.getId()));
            return player;
        }).collect(Collectors.toCollection(ArrayList::new)));

        List<String> roleOrder = List.of("TANK", "HEALER", "DAMAGE", "UNKNOWN");
        record.getPlayers().sort(Comparator.comparing((MythicRecordPlayer p) -> roleOrder.indexOf(p.getRoleName()))
                .thenComparing((MythicRecordPlayer p) -> p.getSpecId())
                .thenComparing((MythicRecordPlayer p) -> p.getPlayerRealm())
                .thenComparing((MythicRecordPlayer p) -> p.getPlayerName()));

        DateFormat tsFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        MessageFormat idFormat = new MessageFormat("{0}_{1}_{2}_{3,number,#}_{4,number,#}_");
        String idString = idFormat.format(new Object[] {
                tsFormat.format(new Date(record.getCompletedTimestamp())),
                dungeon.getDungeonName(),
                record.getKeystoneLevel(),
                record.getPeriod(),
                record.getCompletedTimestamp()
        });
        for (MythicRecordPlayer player : record.getPlayers()) {
            idString += player.getPlayerRealm().substring(0, 1) + player.getPlayerName().substring(0, 1);
        }
        record.setRecordId(idString);
        if (idString == null || idCache.contains(idString)) {
            return;
        }

        var savedRecord = recordRepo.findById(idString);
        var updatedProperties = new HashMap<String, String>();
        if (savedRecord.isPresent()) {
            var dbRecord = savedRecord.get();
            var dbPlayers = dbRecord.getPlayers();

            record.getPlayers().forEach(p -> {
                dbPlayers.stream()
                        .filter(p2 -> p.getPlayerRealm().equals(p2.getPlayerRealm())
                                && p.getPlayerName().equals(p2.getPlayerName()))
                        .sorted(Comparator.comparing(p2 -> p.getId()))
                        .findFirst()
                        .ifPresent(p2 -> p.setId(p2.getId()));
            });

            var recordChanged = false;
            if (dbPlayers.size() > record.getPlayers().size()) {
                updatedProperties.put("member count",
                        String.format("%d -> %d", dbPlayers.size(), record.getPlayers().size()));
                recordChanged |= dbPlayers.size() > record.getPlayers().size();
            }
            if (dbRecord.getMythicRating() + 0.01f < record.getMythicRating()) {
                updatedProperties.put("mythic rating",
                        String.format("%f -> %f", dbRecord.getMythicRating(), record.getMythicRating()));
                recordChanged |= dbRecord.getMythicRating() < record.getMythicRating();
            } else {
                record.setMythicRating(dbRecord.getMythicRating());
            }

            if (!recordChanged) {
                idCache.add(idString);
                return;
            }
        }

        recordRepo.save(record);
        log.info("{} new record!", record.getRecordId());
        idCache.add(idString);

        publisher.publishEvent(new RecordSaveEvent(record).updated(updatedProperties));
    }
}
