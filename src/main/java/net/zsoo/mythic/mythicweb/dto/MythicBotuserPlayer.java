package net.zsoo.mythic.mythicweb.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MythicBotuserPlayer {
    @Id
    private String userId;
    @Id
    private String playerRealm;
    @Id
    private String playerName;
}
