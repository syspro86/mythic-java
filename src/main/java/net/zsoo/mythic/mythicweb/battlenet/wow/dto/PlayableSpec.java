package net.zsoo.mythic.mythicweb.battlenet.wow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PlayableSpec extends KeyIdName {
    @JsonProperty("playable_class")
    private KeyIdName playableClass;
    private TypeName role;
}
