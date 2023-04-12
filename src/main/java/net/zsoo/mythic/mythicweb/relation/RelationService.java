package net.zsoo.mythic.mythicweb.relation;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface RelationService {

    List<RelationResult> findRelationList(String realm, String name, int run);

}
