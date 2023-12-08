package net.zsoo.mythic.mythicweb.crawler;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.zsoo.mythic.mythicweb.battlenet.wow.DataAPI;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.PeriodIndex;

@Slf4j
@Component
@RequiredArgsConstructor
public class PeriodTask {
    @Qualifier("accessToken")
    private final Supplier<String> accessTokenSupplier;
    private final DataAPI dataApi;
    private int period = 0;

    @Scheduled(cron = "${mythic.crawler.period.cron:-}")
    public void updatePeriod() {
        String accessToken = accessTokenSupplier.get();
        PeriodIndex index = dataApi.periodIndex(accessToken);
        period = index.getCurrentPeriod().getId();
    }

    public int getPeriod() {
        if (period == 0) {
            updatePeriod();
        }
        return period;
    }
}
