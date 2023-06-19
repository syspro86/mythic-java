package net.zsoo.mythic.mythicweb.wowapi;

import org.springframework.stereotype.Service;

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
