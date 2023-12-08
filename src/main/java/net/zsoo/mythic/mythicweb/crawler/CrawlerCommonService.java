package net.zsoo.mythic.mythicweb.crawler;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.BestRun;
import net.zsoo.mythic.mythicweb.dto.MythicRecord;
import net.zsoo.mythic.mythicweb.dto.MythicRecordRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlerCommonService {

    private final MythicRecordRepository recordRepo;

    public void saveRun(BestRun run) {
        MythicRecord record = new MythicRecord();
        record.setRecordId("");
        record.setDungeonId(run.getDungeon().getId());
        record.setCompletedTimestamp(run.getCompletedTimestamp());
        recordRepo.save(record);
    }
}
