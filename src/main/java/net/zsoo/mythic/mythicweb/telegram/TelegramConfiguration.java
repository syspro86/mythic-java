package net.zsoo.mythic.mythicweb.telegram;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
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

    private final ApplicationEventPublisher publisher;
    private MythicBot mythicBot;

    @Bean
    public TelegramBotsApi getBotsApi() {
        if (!enabled) {
            return null;
        }
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(getTelegramBot());
            return api;
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public TelegramLongPollingBot getTelegramBot() {
        if (mythicBot == null) {
            mythicBot = new MythicBot();
        }
        return mythicBot;
    }

    class MythicBot extends TelegramLongPollingBot {
        public MythicBot() {
            super(botToken);
        }

        @Override
        public void onUpdateReceived(Update update) {
            publisher.publishEvent(new TelegramUpdateEvent(update));
        }

        @Override
        public String getBotUsername() {
            return botName;
        }
    }
}
