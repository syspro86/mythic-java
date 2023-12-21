package net.zsoo.mythic.mythicweb.battlenet.wow;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.zsoo.mythic.mythicweb.battlenet.wow.dto.MythicLeaderboardIndex;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.MythicLeaderboardPeriod;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.PeriodIndex;

@FeignClient(name = "api.blizzard.com/data", url = "https://${battlenet.region:kr}.api.blizzard.com/", configuration = DataAPIConfiguration.class, dismiss404 = true)
public interface DataAPI {
        @RequestMapping(method = RequestMethod.GET, value = "/data/wow/mythic-keystone/period/index")
        PeriodIndex periodIndex(@RequestParam("access_token") String accessToken);

        @RequestMapping(method = RequestMethod.GET, value = "/data/wow/connected-realm/{realmId}/mythic-leaderboard/index")
        MythicLeaderboardIndex mythicLeaderboardIndex(
                        @PathVariable("realmId") int realmId, @RequestParam("access_token") String accessToken);

        @RequestMapping(method = RequestMethod.GET, value = "/data/wow/connected-realm/{realmId}/mythic-leaderboard/{dungeonId}/period/{period}")
        MythicLeaderboardPeriod mythicLeaderboardPeriod(
                        @PathVariable("realmId") int realmId,
                        @PathVariable("dungeonId") int dungeonId,
                        @RequestParam("period") int period, @RequestParam("access_token") String accessToken);
}
