package net.zsoo.mythic.mythicweb.dto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MythicRecordRepository extends JpaRepository<MythicRecord, String> {

  List<MythicRecord> findByDungeonId(int dungeonId);

  List<MythicRecord> findTop100ByPeriodAndDungeonIdOrderByKeystoneLevelDescDurationDesc(int period, int dungeon);

  Optional<MythicRecord> findTopByOrderByPeriodDesc();

  @Query("""
      SELECT MR
        FROM MythicRecord MR JOIN MR.players MRP
       WHERE MRP.playerName = :playerName
         AND MRP.playerRealm = :playerRealm
         AND (:timestamp = 0 OR MR.completedTimestamp < :timestamp)
       ORDER BY MR.completedTimestamp DESC
       LIMIT :count
      """)
  List<MythicRecord> findRecentRecords(String playerRealm, String playerName, int timestamp, int count);
}
