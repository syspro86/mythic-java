package net.zsoo.mythic.mythicweb.timeline;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimelineServiceImpl implements TimelineService {

    private final TimelineRepository repo;

    public List<TimelineResult> findTimelineData(String playerRealm, String playerName, boolean allSeason) {
        return repo.findTimelineData(playerRealm, playerName, allSeason ? 1 : 0);
    }
}