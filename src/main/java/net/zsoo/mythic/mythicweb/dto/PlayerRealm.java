package net.zsoo.mythic.mythicweb.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class PlayerRealm {
    @Id
    private int realmId;
    private String realmSlug;
    private String realmName;
    private boolean isConnectedRealm;
}
