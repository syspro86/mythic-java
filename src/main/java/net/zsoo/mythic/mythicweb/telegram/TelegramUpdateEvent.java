package net.zsoo.mythic.mythicweb.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelegramUpdateEvent {
    private Update update;
}
