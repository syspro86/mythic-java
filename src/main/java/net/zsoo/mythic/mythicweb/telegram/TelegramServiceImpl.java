package net.zsoo.mythic.mythicweb.telegram;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.zsoo.mythic.mythicweb.crawler.RecordSaveEvent;
import net.zsoo.mythic.mythicweb.dto.MythicBotuser;
import net.zsoo.mythic.mythicweb.dto.MythicBotuserPlayer;
import net.zsoo.mythic.mythicweb.dto.MythicBotuserRepository;
import net.zsoo.mythic.mythicweb.dto.MythicDungeon;
import net.zsoo.mythic.mythicweb.dto.MythicDungeonRepository;
import net.zsoo.mythic.mythicweb.dto.MythicRecord;
import net.zsoo.mythic.mythicweb.dto.MythicRecordPlayer;
import net.zsoo.mythic.mythicweb.dto.MythicRecordRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {
    private final MythicBotuserRepository botUserRepo;
    private final MythicRecordRepository recordRepo;
    private final MythicDungeonRepository dungeonRepo;
    private final TelegramLongPollingBot telegramBot;

    @Value("${mythic.telegram.sessionPrefix:}")
    private String sessionPrefix;

    @Value("${mythic.telegram.sessionSuffix:}")
    private String sessionSuffix;

    @Override
    public String onMessage(long chatId, String message) {
        if (message.startsWith("/add ") || message.startsWith("/추가 ")) {
            return addOrRemove(chatId, message.substring(message.indexOf(" ") + 1), true);
        } else if (message.startsWith("/remove ") || message.startsWith("/삭제 ")) {
            return addOrRemove(chatId, message.substring(message.indexOf(" ") + 1), false);
        }

        switch (message) {
            case "/me":
                return me(chatId);

            case "/주차":
            case "/report":
                return report(chatId);

            default:
                return search(message);
        }
    }

    private String me(long chatId) {
        MythicBotuser botUser = botUserRepo.findById(Long.toString(chatId)).orElse(null);
        if (botUser == null) {
            botUser = new MythicBotuser();
            botUser.setId(Long.toString(chatId));
            botUser.setPlayers(new ArrayList<>());
            botUserRepo.save(botUser);
        }
        if (botUser.getWebSessionId() == null || botUser.getWebSessionId().equals("")) {
            String sid = sessionPrefix + chatId + sessionSuffix;
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(sid.getBytes(StandardCharsets.UTF_8));
                sid = Base64.getUrlEncoder().encodeToString(hash);
                botUser.setWebSessionId(sid);
                botUserRepo.save(botUser);
            } catch (NoSuchAlgorithmException e) {
                log.error("sha256 error", e);
                return null;
            }
        }
        return "https://mythic.zsoo.net/?user=" + botUser.getWebSessionId();
    }

    private String report(long chatId) {
        return null;
    }

    private String addOrRemove(long chatId, String rn, boolean add) {
        if (rn.indexOf("-") <= 0) {
            return "이름-서버 형태로 입력하세요.";
        }
        MythicBotuser botUser = botUserRepo.findById(Long.toString(chatId)).orElse(null);
        if (botUser == null) {
            if (!add) {
                return "일치하는 정보가 없습니다.";
            }
            botUser = new MythicBotuser();
            botUser.setId(Long.toString(chatId));
            botUser.setPlayers(new ArrayList<>());
        }
        String realm = rn.substring(rn.indexOf("-") + 1);
        String name = rn.substring(0, rn.indexOf("-"));
        if (add) {
            MythicBotuserPlayer player = new MythicBotuserPlayer();
            player.setBotuser(botUser);
            player.setPlayerRealm(realm);
            player.setPlayerName(name);
            botUser.getPlayers().add(player);
            botUserRepo.save(botUser);
            return "추가되었습니다.";
        } else {
            boolean removed = botUser.getPlayers()
                    .removeIf(p -> p.getPlayerRealm().equals(realm) && p.getPlayerName().equals(name));
            if (!removed) {
                return "일치하는 정보가 없습니다.";
            }
            botUserRepo.save(botUser);
            return "삭제되었습니다.";
        }
    }

    private String search(String rn) {
        String realm = (rn.indexOf("-") > 0) ? rn.substring(rn.indexOf("-") + 1) : null;
        String name = (rn.indexOf("-") > 0) ? rn.substring(0, rn.indexOf("-")) : rn;

        List<MythicRecord> records = recordRepo.findRecentRecords(realm, name, 0, 10);
        return records.stream().map(this::recordToMessage).collect(Collectors.joining("\n\n"));
    }

    @EventListener
    public void telegramUpdate(TelegramUpdateEvent event) throws InterruptedException {
        var update = event.getUpdate();
        long chatId = update.getMessage().getChat().getId();
        String text = update.getMessage().getText();
        String reply = onMessage(chatId, text);
        if (reply == null || reply.equals("")) {
            return;
        }
        try {
            telegramBot.execute(new SendMessage(Long.toString(chatId), reply));
            log.info("message sent! {} {}", chatId, reply);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @EventListener
    public void recordSaved(RecordSaveEvent event) throws InterruptedException {
        MythicRecord record = event.getRecord();
        Set<String> chatIds = new HashSet<>();
        for (MythicRecordPlayer player : record.getPlayers()) {
            List<MythicBotuser> users = botUserRepo.findByPlayerRealmAndPlayerName(player.getPlayerRealm(),
                    player.getPlayerName());
            chatIds.addAll(users.stream().map(MythicBotuser::getId).toList());
        }
        if (chatIds.size() == 0) {
            return;
        }

        String message = recordToMessage(record);
        if (event.isUpdate()) {
            message += "\n";
            message += event.getUpdatedProperties().entrySet().stream().map(kv -> kv.getKey() + ": " + kv.getValue())
                    .collect(Collectors.joining(", "));
        }
        try {
            for (String chatId : chatIds) {
                telegramBot.execute(new SendMessage(chatId, message));
                log.info("message sent! {} {}", chatId, message);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String recordToMessage(MythicRecord r) {
        MessageFormat mf = new MessageFormat("{0}+{1} {2}\n({3}) {4}분 {5}초");
        MessageFormat mf2 = new MessageFormat("\n{0}-{1} {2} {3}");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String message = mf.format(new Object[] {
                dungeonRepo.findById(r.getDungeonId()).map(MythicDungeon::getDungeonName).orElse("??"),
                r.getKeystoneLevel(),
                df.format(new Date(r.getCompletedTimestamp())),
                r.getKeystoneUpgrade(),
                r.getDuration() / 60000,
                (r.getDuration() / 1000) % 60 });

        message += r.getPlayers().stream()
                .map(p -> mf2.format(
                        new Object[] { p.getPlayerName(), p.getPlayerRealm(), p.getSpecName(), p.getClassName() }))
                .collect(Collectors.joining());
        return message;
    }
}
