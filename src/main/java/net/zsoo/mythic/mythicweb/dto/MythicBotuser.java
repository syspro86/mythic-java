package net.zsoo.mythic.mythicweb.dto;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class MythicBotuser {
    @Id
    private String userId;
    private String webSessionId;

    @OneToMany(mappedBy = "userId", fetch = FetchType.EAGER)
    List<MythicBotuserPlayer> players;
}
