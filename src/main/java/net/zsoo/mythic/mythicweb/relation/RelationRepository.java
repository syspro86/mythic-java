package net.zsoo.mythic.mythicweb.relation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.zsoo.mythic.mythicweb.dto.MythicRecordPlayer;

public interface RelationRepository extends JpaRepository<MythicRecordPlayer, Long> {
    @Query("""
            SELECT MRP2.playerRealm playerRealm, MRP2.playerName playerName, COUNT(1) playCount
                FROM MythicRecord MR JOIN MR.players MRP1 JOIN MR.players MRP2
                WHERE MRP1.playerRealm = :playerRealm
                AND MRP1.playerName = :playerName
                GROUP BY MRP2.playerRealm, MRP2.playerName
            HAVING COUNT(1) >= :minimumRun
                AND (MRP2.playerRealm <> :playerRealm
                OR  MRP2.playerName <> :playerName)
                AND MRP2.playerName <> '?'
                ORDER BY 3 DESC
                LIMIT 100
            """)
    List<RelationResult> findGroupByRelationList(@Param("playerRealm") String playerRealm,
            @Param("playerName") String playerName, @Param("minimumRun") int minimumRun);
}
