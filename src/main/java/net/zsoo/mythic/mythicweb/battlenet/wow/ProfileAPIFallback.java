package net.zsoo.mythic.mythicweb.battlenet.wow;

import org.springframework.stereotype.Component;

import net.zsoo.mythic.mythicweb.battlenet.wow.dto.MythicKeystoneProfile;
import net.zsoo.mythic.mythicweb.battlenet.wow.dto.MythicKeystoneProfileSeason;

@Component
public class ProfileAPIFallback implements ProfileAPI {
    @Override
    public MythicKeystoneProfile mythicKeystoneProfile(String realmSlug, String characterName, String accessToken) {
        return null;
    }

    @Override
    public MythicKeystoneProfileSeason mythicKeystoneProfileSeason(String realmSlug, String characterName, int season,
            String accessToken) {
        return null;
    }
}
