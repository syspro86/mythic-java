package net.zsoo.mythic.mythicweb.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MythicSeasonPeriodId {
    @Id
    private int season;
    @Id
    private int period;
}
