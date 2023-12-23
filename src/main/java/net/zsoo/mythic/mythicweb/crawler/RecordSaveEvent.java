package net.zsoo.mythic.mythicweb.crawler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.zsoo.mythic.mythicweb.dto.MythicRecord;

@Getter
@AllArgsConstructor
public class RecordSaveEvent {
    MythicRecord record;
}