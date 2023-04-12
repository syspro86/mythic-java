package net.zsoo.mythic.mythicweb.dto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MythicRecordRepository extends JpaRepository<MythicRecord, String> {

    List<MythicRecord> findByDungeonId(int dungeonId);

    List<MythicRecord> findTop100ByPeriodAndDungeonIdOrderByKeystoneLevelDescDurationDesc(int period, int dungeon);

    Optional<MythicRecord> findTopByOrderByPeriodDesc();
}
