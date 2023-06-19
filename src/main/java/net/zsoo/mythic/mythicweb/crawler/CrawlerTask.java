package net.zsoo.mythic.mythicweb.crawler;

import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.zsoo.mythic.mythicweb.dto.MythicPlayer;

@Component
@AllArgsConstructor
@Slf4j
public class CrawlerTask {
    private final CrawlerRepository crawlerRepo;

    @Scheduled(cron = "${config.crawler.cron}")
    public void printTime() {
        log.debug("time: {}", System.currentTimeMillis());

        Optional<MythicPlayer> player = crawlerRepo.findNextUpdatePlayer1();
        log.debug("player: {}", player);

        Optional<MythicPlayer> player2 = crawlerRepo.findNextUpdatePlayer2();
        log.debug("player: {}", player2);
    }

}
