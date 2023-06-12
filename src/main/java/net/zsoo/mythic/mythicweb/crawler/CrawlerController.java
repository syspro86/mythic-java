package net.zsoo.mythic.mythicweb.crawler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class CrawlerController {
    private final CrawlerService crawlerService;

    @GetMapping("/cr")
    public String authorizeBattlNetToken() {
        return crawlerService.authorizeBattleNet();
    }
}
