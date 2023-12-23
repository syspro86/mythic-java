package net.zsoo.mythic.mythicweb.battlenet.wow.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Dungeon extends KeyIdName {
    private KeyIdName map;
    private Zone zone;
    private KeyIdName dungeon;
    @JsonProperty("keystone_upgrades")
    private List<KeystoneUpgrade> keystoneUpgrades;
    @JsonProperty("is_tracked")
    private boolean isTracked;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeystoneUpgrade {
        @JsonProperty("upgrade_level")
        private int upgradeLevel;
        @JsonProperty("qualifying_duration")
        private int qualifyingDuration;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Zone {
        private String slug;
    }
}
