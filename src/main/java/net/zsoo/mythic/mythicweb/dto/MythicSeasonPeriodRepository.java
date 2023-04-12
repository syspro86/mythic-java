package net.zsoo.mythic.mythicweb.dto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MythicSeasonPeriodRepository extends JpaRepository<MythicSeasonPeriod, MythicSeasonPeriodId> {
    Optional<MythicSeasonPeriod> findTopByPeriodOrderBySeasonAsc(int period);
}
