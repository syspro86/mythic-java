package net.zsoo.mythic.mythicweb.wowapi;

import java.util.HashMap;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.RequiredArgsConstructor;
import net.zsoo.mythic.mythicweb.wowapi.battlenet.BattlenetOAuthAPI;

@Service
@RequiredArgsConstructor
public class WowAPI {

    private static final String ACCESS_TOKEN = "access_token";
    private final BattlenetOAuthAPI oauthAPI;

    public String getToken() {
        return oauthAPI.token().get(ACCESS_TOKEN).toString();
    }
}
