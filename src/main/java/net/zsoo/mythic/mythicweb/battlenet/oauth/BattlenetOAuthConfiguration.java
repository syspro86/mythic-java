package net.zsoo.mythic.mythicweb.battlenet.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import feign.auth.BasicAuthRequestInterceptor;

public class BattlenetOAuthConfiguration {
    @Value("${battlenet.api_id:}")
    private String apiId;
    @Value("${battlenet.api_secret:}")
    private String apiSecret;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(apiId, apiSecret);
    }
}
