package net.zsoo.mythic.mythicweb.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class PlayerSpec {
    @Id
    private int id;
    private String name;
    private String className;
    private String role;
}
