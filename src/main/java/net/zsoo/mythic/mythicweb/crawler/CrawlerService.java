package net.zsoo.mythic.mythicweb.crawler;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.zsoo.mythic.mythicweb.wowapi.WowAPI;

@Service
@AllArgsConstructor
public class CrawlerService {

    private final WowAPI wowAPI;

    public String authorizeBattleNet() {
        return wowAPI.getToken();
    }
}
