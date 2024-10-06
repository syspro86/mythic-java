package net.zsoo.mythic.mythicweb.battlenet.wow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Media {
    private int id;
    private Asset[] assets;
}
