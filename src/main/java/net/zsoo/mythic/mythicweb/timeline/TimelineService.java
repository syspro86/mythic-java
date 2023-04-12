package net.zsoo.mythic.mythicweb.timeline;

import java.util.List;

public interface TimelineService {
    List<TimelineResult> findTimelineData(String playerRealm, String playerName);
}