package net.zsoo.mythic.mythicweb.crawler;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import net.zsoo.mythic.mythicweb.dto.MythicPlayer;
import net.zsoo.mythic.mythicweb.dto.MythicPlayerId;

public interface CrawlerRepository extends Repository<MythicPlayer, MythicPlayerId> {

  interface NextPlayer {
    String getPlayerRealm();

    String getPlayerName();
  }

  @Query("""
      SELECT MP.playerRealm playerRealm, MP.playerName playerName
        FROM MythicPlayer MP
       WHERE MP.lastUpdateTs = 0
       ORDER BY MP.lastUpdateTs ASC
       LIMIT 1
      """)
  Optional<NextPlayer> findNextUpdatePlayer1();

  @Query("""
      SELECT MRP.playerRealm playerRealm, MRP.playerName playerName
        FROM MythicRecord MR JOIN MR.players MRP LEFT JOIN MythicPlayer MP
       WHERE MRP.playerRealm = MP.playerRealm
         AND MRP.playerName = MP.playerName
         AND MR.period = (SELECT MAX(MSP.period) FROM MythicSeasonPeriod MSP)
         AND MR.keystoneLevel >= 20
         AND MR.keystoneUpgrade >= 1
         AND MP IS NULL
       ORDER BY MR.recordId ASC
       LIMIT 1
      """)
  Optional<NextPlayer> findNextUpdatePlayer2();

  @Query("""
      SELECT MRP.playerRealm playerRealm, MRP.playerName playerName
        FROM MythicRecord MR JOIN MR.players MRP JOIN MythicPlayer MP
       WHERE MRP.playerRealm = MP.playerRealm
         AND MRP.playerName = MP.playerName
         AND MR.period = (SELECT MAX(MSP.period) FROM MythicSeasonPeriod MSP)
         AND MR.keystoneLevel >= 20
         AND MR.keystoneUpgrade >= 1
         AND MP.lastUpdateTs < :timestamp
       ORDER BY MP.lastUpdateTs ASC
       LIMIT 1
      """)
  Optional<NextPlayer> findNextUpdatePlayer3(long timestamp);

  @Query("""
      SELECT MP.playerRealm playerRealm, MP.playerName playerName FROM MythicPlayer MP
       ORDER BY MP.lastUpdateTs ASC
      LIMIT 1
      """)
  Optional<NextPlayer> findNextUpdatePlayer4();
}
