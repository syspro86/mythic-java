package net.zsoo.mythic.mythicweb.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/char/scan")
@RequiredArgsConstructor
public class ScanController {

    private final MythicCommonService commonService;

    @GetMapping("/{realm}/{name}")
    public ResponseEntity<String> scan(
            @PathVariable("realm") String realm,
            @PathVariable("name") String name) {
        commonService.updatePlayerTalentTimestamp(realm, name, 0);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
