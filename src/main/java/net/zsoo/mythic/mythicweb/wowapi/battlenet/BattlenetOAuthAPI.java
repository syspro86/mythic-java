package net.zsoo.mythic.mythicweb.wowapi.battlenet;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "auth.battle.net", url = "https://kr.battle.net/", configuration = BattlenetOAuthConfiguration.class)
public interface BattlenetOAuthAPI {
    @RequestMapping(method = RequestMethod.POST, value = "/oauth/token?grant_type=client_credentials", headers = {
            "Content-Type: application/x-www-form-urlencoded" })
    Map<String, Object> token();
}
