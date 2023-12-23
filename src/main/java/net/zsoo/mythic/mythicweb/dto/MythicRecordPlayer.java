package net.zsoo.mythic.mythicweb.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MythicRecordPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String playerRealm;
    private String playerName;
    private int specId;
    private String className;
    private String specName;
    private String roleName;
    private String playerId;

    @ManyToOne
    @JoinColumn(name = "recordId", referencedColumnName = "recordId")
    private MythicRecord record;

    // @ManyToOne
    // @JoinColumn(name = "playerRealm", updatable = false, insertable = false)
    // @JoinColumn(name = "playerName", updatable = false, insertable = false)
    // private MythicPlayer player;
}
