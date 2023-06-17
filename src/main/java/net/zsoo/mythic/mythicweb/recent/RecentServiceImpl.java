package net.zsoo.mythic.mythicweb.recent;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.zsoo.mythic.mythicweb.dto.MythicRecord;
import net.zsoo.mythic.mythicweb.dto.MythicRecordRepository;

@Service
@RequiredArgsConstructor
public class RecentServiceImpl implements RecentService {
    private final MythicRecordRepository recordRepo;

    public List<MythicRecord> findRecord(String realm, String name, int timestamp) {
        return recordRepo.findRecentRecords(realm, name, timestamp);
    }
}
