package net.zsoo.mythic.mythicweb.dto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MythicSeasonRepository extends JpaRepository<MythicSeason, Integer> {
  @Query("""
      SELECT MS
        FROM MythicSeason MS
        WHERE MS.startTimestamp <= :timestamp
          AND (MS.endTimestamp IS NULL OR MS.endTimestamp > :timestamp)
        ORDER BY MS.season DESC
        LIMIT 1
      """)
  Optional<MythicSeason> findByTimestamp(long timestamp);
}
