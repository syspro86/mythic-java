package net.zsoo.mythic.mythicweb.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MythicDungeon {

    @Id
    private int dungeonId;
    private String dungeonName;
    private String zone;
    @Column(name = "upgrade_1")
    private int upgrade1;
    @Column(name = "upgrade_2")
    private int upgrade2;
    @Column(name = "upgrade_3")
    private int upgrade3;
}
