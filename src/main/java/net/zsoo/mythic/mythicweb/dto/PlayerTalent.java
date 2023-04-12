package net.zsoo.mythic.mythicweb.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

@Data
@Entity
@IdClass(PlayerTalentId.class)
public class PlayerTalent {
    @Id
    private String playerRealm;
    @Id
    private String playerName;
    @Id
    private int specId;
    private String talentCode;
    private long lastUpdateTs;
}
