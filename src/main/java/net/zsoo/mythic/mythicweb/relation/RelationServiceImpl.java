package net.zsoo.mythic.mythicweb.relation;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RelationServiceImpl implements RelationService {

    private final RelationRepository realtionRepo;

    @Override
    public List<RelationResult> findRelationList(String realm, String name, int run) {
        return realtionRepo.findGroupByRelationList(realm, name, run);
    }

}
