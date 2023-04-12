package net.zsoo.mythic.mythicweb.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Data;

@Data
@Entity
@IdClass(MythicSeasonPeriodId.class)
public class MythicSeasonPeriod {
    @Id
    private int season;
    @Id
    private int period;
}
