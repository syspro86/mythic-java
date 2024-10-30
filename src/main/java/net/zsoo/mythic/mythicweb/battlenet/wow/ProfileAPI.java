package net.zsoo.mythic.mythicweb.battlenet.wow;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.zsoo.mythic.mythicweb.battlenet.wow.dto.MythicKeystoneProfile;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.MythicKeystoneProfileSeason;

@FeignClient(name = "api.blizzard.com/profile", url = "https://${mythic.battlenet.region:kr}.api.blizzard.com/", configuration = ProfileAPIConfiguration.class, dismiss404 = true, fallback = ProfileAPIFallback.class)
public interface ProfileAPI {
        @RequestMapping(method = RequestMethod.GET, value = "/profile/wow/character/{realmSlug}/{characterName}/mythic-keystone-profile")
        MythicKeystoneProfile mythicKeystoneProfile(
                        @PathVariable("realmSlug") String realmSlug,
                        @PathVariable("characterName") String characterName,
                        @RequestHeader("Authorization") String accessToken);

        @RequestMapping(method = RequestMethod.GET, value = "/profile/wow/character/{realmSlug}/{characterName}/mythic-keystone-profile/season/{season}")
        MythicKeystoneProfileSeason mythicKeystoneProfileSeason(
                        @PathVariable("realmSlug") String realmSlug,
                        @PathVariable("characterName") String characterName,
                        @PathVariable("season") int season,
                        @RequestHeader("Authorization") String accessToken);
}
