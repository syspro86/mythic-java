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
import net.zsoo.mythic.mythicweb.common.MythicCommonService;
import net.zsoo.mythic.mythicweb.dto.MythicPlayer;

@RestController
@RequestMapping("/char/mythic_rating")
@RequiredArgsConstructor
public class TimelineController {
        private final MythicCommonService commonService;
        private final TimelineService timelineService;

        @GetMapping("/{realm}/{name}")
        public ResponseEntity<TimelineResponse> findTimelineData(
                        @PathVariable("realm") String realm,
                        @PathVariable("name") String name,
                        @RequestParam("season") int allSeason) {

                MythicPlayer player = commonService.findPlayer(realm, name).orElse(null);
                List<TimelineResult> timeline = timelineService.findTimelineData(realm, name, allSeason > 0);

                List<TimelineResponseData> collect = timeline.stream()
                                .map(r -> new TimelineResponseData(r.getDungeonId(), r.getDungeonName(),
                                                r.getMythicRating(), r.getPeriod(), r.getSeason(), r.getTimestamp()))
                                .collect(Collectors.toList());
                return new ResponseEntity<>(new TimelineResponse(player, collect), HttpStatus.OK);
        }

        record TimelineResponse(MythicPlayer player, List<TimelineResponseData> data) {
        }

        record TimelineResponseData(int dungeon_id, String dungeon_name, float mythic_rating, int period, int season,
                        long timestamp) {
        }
}
