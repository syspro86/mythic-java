package net.zsoo.mythic.mythicweb.dto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MythicPeriodRepository extends JpaRepository<MythicPeriod, Integer> {
  Optional<MythicPeriod> findByPeriod(int period);

  @Query("""
      SELECT MP
        FROM MythicPeriod MP
        WHERE MP.startTimestamp <= :timestamp
          AND (MP.endTimestamp IS NULL OR MP.endTimestamp > :timestamp)
        ORDER BY MP.period DESC
        LIMIT 1
      """)
  Optional<MythicPeriod> findByTimestamp(long timestamp);
}
