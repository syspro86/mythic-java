package net.zsoo.mythic.mythicweb.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MythicPeriod {

    @Id
    private int period;
    private long startTimestamp;
    private long endTimestamp;
}
