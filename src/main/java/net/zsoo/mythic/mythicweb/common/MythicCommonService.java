package net.zsoo.mythic.mythicweb.common;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import net.zsoo.mythic.mythicweb.dto.MythicDungeon;
import net.zsoo.mythic.mythicweb.dto.MythicPeriod;
import net.zsoo.mythic.mythicweb.dto.MythicPlayer;
import net.zsoo.mythic.mythicweb.dto.PlayerRealm;

@Service
public interface MythicCommonService {

    List<MythicDungeon> findAllDungeons();

    List<PlayerRealm> findAllRealms();

    Optional<MythicPeriod> findPeriod(int period);

    int findSeaonByPeriod(int period);

    Optional<MythicPlayer> findPlayer(String realm, String name);

    void updatePlayerTalentTimestamp(String realm, String name, long timestamp);
}
