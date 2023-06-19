package net.zsoo.mythic.mythicweb.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@IdClass(MythicRecordPlayerId.class)
@NoArgsConstructor
@AllArgsConstructor
public class MythicRecordPlayer {
    @Id
    private String recordId;
    @Id
    private String playerRealm;
    @Id
    private String playerName;
    private int specId;
    private String className;
    private String specName;
    private String roleName;
    private String playerId;

    @ManyToOne
    @JoinColumn(name = "player_realm", updatable = false, insertable = false)
    @JoinColumn(name = "player_name", updatable = false, insertable = false)
    private MythicPlayer player;
}
