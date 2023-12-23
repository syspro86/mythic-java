package net.zsoo.mythic.mythicweb.battlenet.wow;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.zsoo.mythic.mythicweb.battlenet.wow.dto.PlayableSpec;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.SpecIndex;

@FeignClient(name = "api.blizzard.com/static_data", url = "https://${mythic.battlenet.region:kr}.api.blizzard.com/", configuration = StaticDataAPIConfiguration.class, dismiss404 = true)
public interface StaticDataAPI {
        @RequestMapping(method = RequestMethod.GET, value = "/data/wow/playable-specialization/index")
        SpecIndex specIndex(@RequestParam("access_token") String accessToken);

        @RequestMapping(method = RequestMethod.GET, value = "/data/wow/playable-specialization/{specId}")
        PlayableSpec playableSpec(@PathVariable("specId") int specId, @RequestParam("access_token") String accessToken);
}
