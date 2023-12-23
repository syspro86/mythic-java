package net.zsoo.mythic.mythicweb.battlenet.oauth;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "battle.net", url = "https://${mythic.battlenet.region:kr}.battle.net/", configuration = BattlenetOAuthConfiguration.class)
public interface BattlenetOAuth {
    @RequestMapping(method = RequestMethod.POST, value = "/oauth/token?grant_type=client_credentials", headers = {
            "Content-Type: application/x-www-form-urlencoded" })
    Map<String, Object> token();
}
