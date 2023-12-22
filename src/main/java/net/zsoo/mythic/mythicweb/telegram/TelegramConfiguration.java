package net.zsoo.mythic.mythicweb.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class TelegramConfiguration {
    @Value("${mythic.telegram.enabled:false}")
    private boolean enabled;

    @Value("${mythic.telegram.name:}")
    private String botName;

    @Value("${mythic.telegram.token:}")
    private String botToken;

    private final TelegramService telegramService;

    @Bean
    public TelegramBotsApi getBotsApi() {
        if (!enabled) {
            return null;
        }
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new MythicBot());
            return api;
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    class MythicBot extends TelegramLongPollingBot {
        public MythicBot() {
            super(botToken);
        }

        @Override
        public void onUpdateReceived(Update update) {
            long chatId = update.getMessage().getChat().getId();
            String text = update.getMessage().getText();
            String reply = telegramService.onMessage(chatId, text);
            if (reply == null || reply.equals("")) {
                return;
            }
            try {
                execute(new SendMessage(Long.toString(chatId), reply));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String getBotUsername() {
            return botName;
        }
    }
}
