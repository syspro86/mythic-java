package net.zsoo.mythic.mythicweb.relation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.zsoo.mythic.mythicweb.dto.MythicRecordPlayer;
import net.zsoo.mythic.mythicweb.dto.MythicRecordPlayerId;

public interface RelationRepository extends JpaRepository<MythicRecordPlayer, MythicRecordPlayerId> {
        @Query("""
                        SELECT playerRealm playerRealm, playerName playerName, COUNT(1) playCount FROM MythicRecordPlayer
                        WHERE recordId IN (
                        SELECT mbp.recordId FROM MythicRecordPlayer mbp
                        WHERE mbp.playerRealm = :playerRealm
                        AND mbp.playerName = :playerName
                        )
                        GROUP BY playerRealm, playerName
                        HAVING COUNT(1) >= :minimumRun
                           AND (playerRealm <> :playerRealm
                            OR  playerName <> :playerName)
                        ORDER BY 3 DESC
                        LIMIT 100
                        """)
        List<RelationResult> findGroupByRelationList(@Param("playerRealm") String playerRealm,
                        @Param("playerName") String playerName, @Param("minimumRun") int minimumRun);
}
