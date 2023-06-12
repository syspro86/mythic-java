package net.zsoo.mythic.mythicweb.crawler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class CrawlerTask {
    @Scheduled(cron = "${config.crawler.cron}")
    public void printTime() {
        log.debug("time: {}", System.currentTimeMillis());
    }
}
