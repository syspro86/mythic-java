package net.zsoo.mythic.mythicweb.battlenet.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import feign.auth.BasicAuthRequestInterceptor;

public class BattlenetOAuthConfiguration {
    @Value("${mythic.battlenet.apiId:}")
    private String apiId;
    @Value("${mythic.battlenet.apiSecret:}")
    private String apiSecret;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(apiId, apiSecret);
    }
}
