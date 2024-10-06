package net.zsoo.mythic.mythicweb.battlenet.wow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Asset extends KeyValue {
    @JsonProperty("file_data_id")
    private int fileDataId;
}
