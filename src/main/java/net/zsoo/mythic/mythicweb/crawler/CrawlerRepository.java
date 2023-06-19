package net.zsoo.mythic.mythicweb.crawler;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import net.zsoo.mythic.mythicweb.dto.MythicPlayer;
import net.zsoo.mythic.mythicweb.dto.MythicPlayerId;

public interface CrawlerRepository extends Repository<MythicPlayer, MythicPlayerId> {

  @Query("""
      SELECT PT.playerRealm, PT.playerName
        FROM PlayerTalent PT
       WHERE PT.lastUpdateTs = 0
       ORDER BY PT.lastUpdateTs ASC
       LIMIT 1
      """)
  Optional<MythicPlayer> findNextUpdatePlayer1();

  @Query("""
      SELECT RP.playerRealm, RP.playerName FROM (
        SELECT DISTINCT MRP.playerRealm, MRP.playerName
          FROM MythicRecord MR JOIN MythicRecordPlayer MRP
        WHERE MR.period = (SELECT MAX(MSP.period) FROM MythicSeasonPeriod MSP)
          AND MR.keystoneLevel >= 20
          AND MR.keystoneUpgrade >= 1
      ) RP
        LEFT OUTER JOIN PlayerTalent PT
          ON (RP.playerRealm = PT.playerRealm AND RP.playerName = PT.playerName)
       WHERE PT.lsatUdpateTs IS NULL
       ORDER BY RP.playerRealm ASC, RP.playerName ASC
       LIMIT 1
      """)
  Optional<MythicPlayer> findNextUpdatePlayer2();

}
