package net.zsoo.mythic.mythicweb.crawler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.zsoo.mythic.mythicweb.crawler.CrawlerRepository.NextPlayer;
import net.zsoo.mythic.mythicweb.dto.MythicPlayer;
import net.zsoo.mythic.mythicweb.dto.MythicPlayerId;
import net.zsoo.mythic.mythicweb.dto.MythicPlayerRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdatePlayerTask {
    private final CrawlerRepository crawlerRepo;
    private final MythicPlayerRepository playerRepo;
    private final UpdatePlayerService updatePlayerService;

    @Scheduled(cron = "${mythic.crawler.player.cron:-}")
    public void onTimer() {
        long now = System.currentTimeMillis();
        log.debug("time: {}", now);
        long endTime = now + 55000;

        int collectedPlayerCount = 0;
        long started = now;
        while (true) {
            NextPlayer nextPlayer = getNextPlayer(now);
            try {
                updatePlayerService.updatePlayer(nextPlayer.getPlayerRealm(), nextPlayer.getPlayerName());
            } catch (Exception e) {
                log.error("갱신 오류", e);
            }
            setPlayerUpdateTime(nextPlayer);
            collectedPlayerCount++;

            if ((now = System.currentTimeMillis()) >= endTime) {
                break;
            }
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        log.info("{} ~ {}: {} plyaer collected", df.format(new Date(started)), df.format(new Date(now)),
                collectedPlayerCount);
    }

    private NextPlayer getNextPlayer(long now) {
        Optional<NextPlayer> player = crawlerRepo.findNextUpdatePlayer1();
        if (!player.isPresent()) {
            player = crawlerRepo.findNextUpdatePlayer2();
        }
        if (!player.isPresent()) {
            // player = crawlerRepo.findNextUpdatePlayer3(now - 1000 * 60 * 60 * 24);
        }
        if (!player.isPresent()) {
            player = crawlerRepo.findNextUpdatePlayer4();
        }
        return player.get();
    }

    private void setPlayerUpdateTime(NextPlayer nextPlayer) {
        var player = playerRepo.findById(new MythicPlayerId(nextPlayer.getPlayerRealm(), nextPlayer.getPlayerName()))
                .orElseGet(() -> {
                    var p = new MythicPlayer();
                    p.setPlayerRealm(nextPlayer.getPlayerRealm());
                    p.setPlayerName(nextPlayer.getPlayerName());
                    p.setSpecId(0);
                    p.setClassName("null");
                    p.setSpecName("null");
                    return p;
                });
        // TODO
        // player.setSpecId( xx );
        // player.setClassName( xx );
        // player.setSpecName( xx );
        player.setLastUpdateTs(System.currentTimeMillis());
        playerRepo.save(player);
    }

}
