package net.zsoo.mythic.mythicweb.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MythicPlayerId {
    @Id
    private String playerRealm;
    @Id
    private String playerName;
}
