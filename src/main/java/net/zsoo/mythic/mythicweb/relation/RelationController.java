package net.zsoo.mythic.mythicweb.relation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/char/relation")
@RequiredArgsConstructor
public class RelationController {

    private final RelationService relationService;

    @GetMapping("/{realm}/{name}/{run}")
    public ResponseEntity<List<RelationResponse>> findRelations(
            @PathVariable("realm") String realm,
            @PathVariable("name") String name,
            @PathVariable("run") int run) {

        List<RelationResult> relations = relationService.findRelationList(realm, name, run);
        List<RelationResponse> collect = relations.stream()
                .map(r -> new RelationResponse(r.getPlayerName(), r.getPlayerRealm(), r.getPlayCount()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(collect, HttpStatus.OK);
    }

    record RelationResponse(String name, String realm, int value) {
    }
}
