package net.zsoo.mythic.mythicweb.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.zsoo.mythic.mythicweb.dto.MythicDungeon;
import net.zsoo.mythic.mythicweb.dto.PlayerRealm;

@RestController
@RequestMapping("/form")
@RequiredArgsConstructor
public class PageCommonController {

    private final MythicCommonService commonService;

    @GetMapping("/realms")
    public ResponseEntity<Map<Integer, String>> realms() {
        final List<PlayerRealm> result = commonService.findAllRealms();
        final Map<Integer, String> map = new HashMap<>();
        result.forEach(r -> map.put(r.getRealmId(), r.getRealmName()));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/dungeons")
    public ResponseEntity<Map<Integer, String>> dungeons() {
        final List<MythicDungeon> result = commonService.findAllDungeons();
        final Map<Integer, String> map = new HashMap<>();
        result.forEach(d -> map.put(d.getDungeonId(), d.getDungeonName()));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
