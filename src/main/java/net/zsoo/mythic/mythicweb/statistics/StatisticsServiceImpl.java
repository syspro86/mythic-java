package net.zsoo.mythic.mythicweb.statistics;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.zsoo.mythic.mythicweb.dto.MythicPeriod;
import net.zsoo.mythic.mythicweb.dto.MythicPeriodRepository;
import net.zsoo.mythic.mythicweb.dto.MythicRecordRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final MythicPeriodRepository periodRepo;
    private final MythicRecordRepository recordRepo;

    @Override
    public List<RecordCountResponse> getRecordCount(int page, int timeWindow) {
        var periods = periodRepo.findAll(PageRequest.of(page, 1, Sort.by(Sort.Direction.DESC, "startTimestamp")))
                .getContent().toArray(MythicPeriod[]::new);

        if (periods.length == 0) {
            return List.of();
        }

        int period = periods[0].getPeriod();
        long startTimestamp = periods[0].getStartTimestamp();
        long endTimestamp = periods[0].getEndTimestamp();
        if (endTimestamp == 0) {
            endTimestamp = System.currentTimeMillis();
            endTimestamp = endTimestamp + timeWindow - 1;
            endTimestamp -= endTimestamp % timeWindow;
        }
        log.debug("period {} start {}, end {}", period, startTimestamp, endTimestamp);

        var list = new ArrayList<RecordCountResponse>();
        for (long ts = startTimestamp; ts < endTimestamp; ts += timeWindow) {
            var count = recordRepo.getRecordCount(period, ts, ts + timeWindow);
            list.add(new RecordCountResponse(period, ts, count));
        }
        return list;
    }
}
