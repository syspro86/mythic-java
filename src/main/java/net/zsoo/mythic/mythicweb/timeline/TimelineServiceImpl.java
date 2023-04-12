package net.zsoo.mythic.mythicweb.timeline;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimelineServiceImpl implements TimelineService {

    private final TimelineRepository repo;

    public List<TimelineResult> findTimelineData(String playerRealm, String playerName) {
        return repo.findTimelineData(playerRealm, playerName);
    }
}