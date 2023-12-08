package net.zsoo.mythic.mythicweb.battlenet.wow;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import feign.RequestInterceptor;

public class DataAPIConfiguration {
    @Value("${battlenet.region}")
    private String region;

    private String getNamespace() {
        return "dynamic-" + region;
    }

    private String getLocale() {
        switch (region) {
            case "kr":
                return "ko_KR";
        }
        return null;
    }

    @Bean
    public RequestInterceptor addQueryParams() {
        return template -> {
            Map<String, Collection<String>> queries = template.queries();
            if (!queries.containsKey("region")) {
                template.query("region", region);
            }
            if (!queries.containsKey("namespace")) {
                template.query("namespace", getNamespace());
            }
            if (!queries.containsKey("locale")) {
                template.query("locale", getLocale());
            }
        };
    }
}
