package net.zsoo.mythic.mythicweb.statistics;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatisticsController {
        private final StatisticsService statisticsService;

        @GetMapping("/records/{period}")
        public ResponseEntity<List<RecordCountResponse>> getRecordCount(@PathVariable("period") int period) {
                List<RecordCountResponse> timeline = statisticsService.getRecordCount(period, 3600000 * 24 * 7);
                return new ResponseEntity<>(timeline, HttpStatus.OK);
        }
}
