package net.zsoo.mythic.mythicweb.common;

import java.util.List;

import org.springframework.stereotype.Service;

import net.zsoo.mythic.mythicweb.dto.MythicRecord;

@Service
public interface MythicRecordService {

    List<MythicRecord> leaderboard(int dungeon);

}
