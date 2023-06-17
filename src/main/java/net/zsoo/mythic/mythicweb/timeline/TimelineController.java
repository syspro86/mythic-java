package net.zsoo.mythic.mythicweb.timeline;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/char/mythic_rating")
@RequiredArgsConstructor
public class TimelineController {
        private final TimelineService timelineService;

        @GetMapping("/{realm}/{name}")
        public ResponseEntity<List<TimelineResponse>> findTimelineData(
                        @PathVariable("realm") String realm,
                        @PathVariable("name") String name,
                        @RequestParam("season") int allSeason) {

                List<TimelineResult> timeline = timelineService.findTimelineData(realm, name, allSeason > 0);
                List<TimelineResponse> collect = timeline.stream()
                                .map(r -> new TimelineResponse(r.getDungeonId(), r.getDungeonName(),
                                                r.getMythicRating(), r.getPeriod(), r.getSeason(), r.getTimestamp()))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(collect, HttpStatus.OK);
        }

        record TimelineResponse(int dungeon_id, String dungeon_name, float mythic_rating, int period, int season,
                        long timestamp) {
        }
}
