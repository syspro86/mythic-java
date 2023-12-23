package net.zsoo.mythic.mythicweb.battlenet.wow.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DungeonIndex {
    private List<KeyIdName> dungeons;
}
