package net.zsoo.mythic.mythicweb.battlenet.wow.dto;

import lombok.Data;

@Data
public class RunMemberProfile {
    private String id;
    private String name;
    private KeyIdName realm;
    private KeyIdName specialization;
}
