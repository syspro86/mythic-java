package net.zsoo.mythic.mythicweb.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@IdClass(MythicPlayerId.class)
@NoArgsConstructor
@AllArgsConstructor
public class MythicPlayer {
    @Id
    private String playerRealm;
    @Id
    private String playerName;
    private int specId;
    private String className;
    private String specName;
    private long lastUpdateTs;
}
