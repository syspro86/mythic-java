package net.zsoo.mythic.mythicweb.statistics;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface StatisticsService {
    List<RecordCountResponse> getRecordCount(int page, int timeWindow);
}