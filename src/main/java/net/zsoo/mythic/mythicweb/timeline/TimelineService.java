package net.zsoo.mythic.mythicweb.timeline;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface TimelineService {
    List<TimelineResult> findTimelineData(String playerRealm, String playerName, boolean allSeason);
}