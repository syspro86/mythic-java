package net.zsoo.mythic.mythicweb.crawler;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.zsoo.mythic.mythicweb.dto.MythicRecord;

@Getter
@RequiredArgsConstructor
public class RecordSaveEvent {
    private final MythicRecord record;

    boolean update;
    Map<String, String> updatedProperties;

    public RecordSaveEvent updated(Map<String, String> updatedProperties) {
        if (updatedProperties == null) {
            return this;
        }
        update = true;
        this.updatedProperties = new HashMap<>(updatedProperties);
        return this;
    }

}