package net.zsoo.mythic.mythicweb.recent;

import java.util.List;

import org.springframework.stereotype.Service;

import net.zsoo.mythic.mythicweb.dto.MythicRecord;

@Service
public interface RecentService {
    List<MythicRecord> findRecord(String realm, String name, int timestamp);
}
