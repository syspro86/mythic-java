package net.zsoo.mythic.mythicweb.timeline;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.zsoo.mythic.mythicweb.dto.MythicRecord;

public interface TimelineRepository extends JpaRepository<MythicRecord, String> {
      @Query("""
                  SELECT RR.dungeonId AS dungeonId,
                          RR.dungeonName AS dungeonName,
                          RR.mythicRating AS mythicRating,
                          RR.period AS period,
                          RR.season AS season,
                          RR.timestamp AS timestamp
                    FROM (
                          SELECT MR.dungeonId AS dungeonId,
                                  (SELECT dungeonName FROM MythicDungeon WHERE dungeonId = MR.dungeonId) AS dungeonName,
                                  MR.mythicRating AS mythicRating,
                                  MR.period AS period,
                                  ROW_NUMBER() OVER (PARTITION BY MR.dungeonId, MR.period ORDER BY MR.mythicRating DESC) AS RN,
                                  (SELECT MIN(season) FROM MythicSeasonPeriod WHERE period = MR.period) AS season,
                                  (SELECT startTimestamp FROM MythicPeriod WHERE period = MR.period) AS timestamp
                            FROM MythicRecord MR JOIN MR.players MRP
                           WHERE MRP.playerName = :playerName
                             AND MRP.playerRealm = :playerRealm
                             AND MR.mythicRating IS NOT NULL
                         ) AS RR
                   WHERE RR.RN = 1
                     AND RR.season IS NOT NULL
                   ORDER BY 4, 1
                  """)
      List<TimelineResult> findTimelineData(@Param("playerRealm") String playerRealm,
                  @Param("playerName") String playerName);
}
