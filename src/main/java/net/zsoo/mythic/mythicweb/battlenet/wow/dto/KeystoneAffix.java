package net.zsoo.mythic.mythicweb.battlenet.wow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class KeystoneAffix {
    @JsonProperty("keystone_affix")
    private KeyIdName keystoneAffix;
    @JsonProperty("starting_level")
    private int startingLevel;
}
