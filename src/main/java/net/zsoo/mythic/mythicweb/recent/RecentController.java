package net.zsoo.mythic.mythicweb.recent;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.zsoo.mythic.mythicweb.common.MythicCommonService;
import net.zsoo.mythic.mythicweb.dto.MythicPlayer;
import net.zsoo.mythic.mythicweb.dto.MythicRecord;

@RestController
@RequestMapping("/char/recent")
@RequiredArgsConstructor
public class RecentController {
    private final MythicCommonService commonService;
    private final RecentService recentService;

    @GetMapping("/{realm}/{name}")
    public ResponseEntity<RecentResponse> findTimelineData(
            @PathVariable("realm") String realm,
            @PathVariable("name") String name,
            @RequestParam(name = "timestamp", defaultValue = "0") int timestamp) {

        MythicPlayer player = commonService.findPlayer(realm, name).orElse(null);
        List<MythicRecord> records = recentService.findRecord(realm, name, timestamp);
        List<RecentResponseData> collect = records.stream().map(r -> new RecentResponseData(r.getCompletedTimestamp(),
                r.getDungeonId(), r.getKeystoneLevel(), r.getKeystoneUpgrade())).collect(Collectors.toList());
        return new ResponseEntity<>(new RecentResponse(player, collect), HttpStatus.OK);
    }

    record RecentResponse(MythicPlayer player, List<RecentResponseData> data) {
    }

    record RecentResponseData(long completedTimestamp, int dungeonId, int keystoneLevel, int keystoneUpgrade) {
    }
}
