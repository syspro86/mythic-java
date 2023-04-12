package net.zsoo.mythic.mythicweb.dto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MythicPeriodRepository extends JpaRepository<MythicPeriod, Integer> {
    Optional<MythicPeriod> findByPeriod(int period);
}
