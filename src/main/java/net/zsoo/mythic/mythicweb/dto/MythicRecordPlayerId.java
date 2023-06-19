package net.zsoo.mythic.mythicweb.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MythicRecordPlayerId {
    @Id
    private String recordId;
    @Id
    @Column(name = "player_realm")
    private String playerRealm;
    @Id
    @Column(name = "player_name")
    private String playerName;
}
