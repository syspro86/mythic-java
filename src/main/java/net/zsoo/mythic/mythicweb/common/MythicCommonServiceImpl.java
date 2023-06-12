package net.zsoo.mythic.mythicweb.common;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.zsoo.mythic.mythicweb.dto.MythicDungeon;
import net.zsoo.mythic.mythicweb.dto.MythicDungeonRepository;
import net.zsoo.mythic.mythicweb.dto.MythicPeriod;
import net.zsoo.mythic.mythicweb.dto.MythicPeriodRepository;
import net.zsoo.mythic.mythicweb.dto.MythicPlayer;
import net.zsoo.mythic.mythicweb.dto.MythicPlayerId;
import net.zsoo.mythic.mythicweb.dto.MythicPlayerRepository;
import net.zsoo.mythic.mythicweb.dto.MythicSeasonPeriodRepository;
import net.zsoo.mythic.mythicweb.dto.PlayerRealm;
import net.zsoo.mythic.mythicweb.dto.PlayerRealmRepository;
import net.zsoo.mythic.mythicweb.dto.PlayerTalent;
import net.zsoo.mythic.mythicweb.dto.PlayerTalentRepository;

@Service
@RequiredArgsConstructor
public class MythicCommonServiceImpl implements MythicCommonService {

    private final MythicDungeonRepository dungeonRepo;
    private final PlayerRealmRepository realmRepo;
    private final MythicPeriodRepository periodRepo;
    private final MythicSeasonPeriodRepository seasonPeriodRepo;
    private final PlayerTalentRepository talentRepo;
    private final MythicPlayerRepository playerRepo;

    @Override
    public List<MythicDungeon> findAllDungeons() {
        return dungeonRepo.findAll();
    }

    @Override
    public List<PlayerRealm> findAllRealms() {
        return realmRepo.findAll();
    }

    @Override
    public Optional<MythicPeriod> findPeriod(int period) {
        return periodRepo.findById(period);
    }

    @Override
    public int findSeaonByPeriod(int period) {
        return seasonPeriodRepo.findTopByPeriodOrderBySeasonAsc(period)
                .map(sp -> sp.getSeason())
                .orElseThrow(() -> new RuntimeException("no season id for " + period));
    }

    @Override
    public Optional<MythicPlayer> findPlayer(String realm, String name) {
        return playerRepo.findById(new MythicPlayerId(realm, name));
    }

    @Override
    public void updatePlayerTalentTimestamp(String realm, String name, long timestamp) {
        List<PlayerTalent> talents = talentRepo.findByPlayerRealmAndPlayerName(realm, name);
        if (talents.size() == 0) {
            PlayerTalent talent = new PlayerTalent();
            talent.setPlayerRealm(realm);
            talent.setPlayerName(name);
            talent.setSpecId(0);
            talent.setLastUpdateTs(timestamp);
            talentRepo.save(talent);
        } else {
            talents.forEach(t -> t.setLastUpdateTs(timestamp));
            talentRepo.saveAll(talents);
        }
    }
}
