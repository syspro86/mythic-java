package net.zsoo.mythic.mythicweb.battlenet.wow;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.zsoo.mythic.mythicweb.battlenet.wow.dto.MythicLeaderboardPeriod;

@FeignClient(name = "api.blizzard.com", url = "https://${battlenet.region}.api.blizzard.com/", configuration = ProfileAPIConfiguration.class, dismiss404 = true)
public interface DataAPI {
        @RequestMapping(method = RequestMethod.GET, value = "/data/wow/connected-realm/{realmId}/mythic-leaderboard/{dungeonId}/period/{period}")
        MythicLeaderboardPeriod mythicLeaderboardPeriod(
                        @PathVariable("realmId") int realmId,
                        @PathVariable("dungeonId") int dungeonId,
                        @RequestParam("period") int period);
}
