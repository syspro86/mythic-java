package net.zsoo.mythic.mythicweb.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class PlayerTalentId {
    @Id
    private String playerRealm;
    @Id
    private String playerName;
    @Id
    private int specId;
}
