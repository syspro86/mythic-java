package net.zsoo.mythic.mythicweb.crawler;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
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
        FROM MythicRecord MR JOIN MR.players MRP LEFT JOIN MRP.player MP
       WHERE MR.period = (SELECT MAX(MSP.period) FROM MythicSeasonPeriod MSP)
         AND MR.keystoneLevel >= 10
         AND MR.keystoneUpgrade >= 1
         AND MP IS NULL
       ORDER BY MR.recordId ASC
       LIMIT 1
      """)
  Optional<NextPlayer> findNextUpdatePlayer2();

  @Query("""
      SELECT MRP.playerRealm playerRealm, MRP.playerName playerName
        FROM MythicRecordPlayer MRP LEFT JOIN MRP.player MP
       WHERE MP.lastUpdateTs IS NULL
      """)
  List<NextPlayer> findNextUpdatePlayer3(Pageable pageable);

  @Query("""
      SELECT MP.playerRealm playerRealm, MP.playerName playerName FROM MythicPlayer MP
       ORDER BY MP.lastUpdateTs ASC
       LIMIT 1
      """)
  Optional<NextPlayer> findNextUpdatePlayer4();
}
