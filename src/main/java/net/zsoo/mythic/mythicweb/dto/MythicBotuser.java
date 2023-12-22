package net.zsoo.mythic.mythicweb.dto;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class MythicBotuser {
    @Id
    private String id;
    private String webSessionId;

    @OneToMany(mappedBy = "botuser", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    List<MythicBotuserPlayer> players;
}
