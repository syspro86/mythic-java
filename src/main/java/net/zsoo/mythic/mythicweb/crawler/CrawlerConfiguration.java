package net.zsoo.mythic.mythicweb.crawler;

import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;
import net.zsoo.mythic.mythicweb.battlenet.oauth.BattlenetOAuth;

@Configuration
@RequiredArgsConstructor
public class CrawlerConfiguration {
    private static final String ACCESS_TOKEN = "access_token";
    private final BattlenetOAuth oauth;
    private static String accessTokenCache;

    @Scheduled(cron = "0 8 * * * *")
    public void onTokenRefresh() {
        accessTokenCache = null;
    }

    @Bean("accessToken")
    public Supplier<String> getAccessTokenSupplier() {
        return () -> {
            if (accessTokenCache == null) {
                accessTokenCache = oauth.token().get(ACCESS_TOKEN).toString();
            }
            return accessTokenCache;
        };
    }
}
