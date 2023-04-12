package net.zsoo.mythic.mythicweb.common;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.zsoo.mythic.mythicweb.dto.MythicRecord;
import net.zsoo.mythic.mythicweb.dto.MythicRecordRepository;

@Service
@RequiredArgsConstructor
public class MythicRecordServiceImpl implements MythicRecordService {

    private final MythicRecordRepository recordRepo;

    @Override
    public List<MythicRecord> leaderboard(int dungeon) {
        var topPeriod = recordRepo.findTopByOrderByPeriodDesc();
        if (!topPeriod.isPresent()) {
            return null;
        }
        return recordRepo.findTop100ByPeriodAndDungeonIdOrderByKeystoneLevelDescDurationDesc(
                topPeriod.map(o -> o.getPeriod()).get(), dungeon);
    }

}
