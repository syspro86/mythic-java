package net.zsoo.mythic.mythicweb.crawler;

import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.zsoo.mythic.mythicweb.crawler.CrawlerRepository.NextPlayer;

@Component
@AllArgsConstructor
@Slf4j
public class CrawlerTask {
    private final CrawlerRepository crawlerRepo;

    @Scheduled(cron = "${config.crawler.cron}")
    public void printTime() {
        long now = System.currentTimeMillis();
        log.debug("time: {}", now);

        Optional<NextPlayer> player = crawlerRepo.findNextUpdatePlayer1();
        if (!player.isPresent()) {
            player = crawlerRepo.findNextUpdatePlayer2();
        }
        if (!player.isPresent()) {
            player = crawlerRepo.findNextUpdatePlayer3(now - 1000 * 60 * 60 * 24);
        }
        if (!player.isPresent()) {
            player = crawlerRepo.findNextUpdatePlayer4();
        }
        NextPlayer nextPlayer = player.get();
        log.debug("player: {}-{}", nextPlayer.getPlayerName(), nextPlayer.getPlayerRealm());
    }

}
