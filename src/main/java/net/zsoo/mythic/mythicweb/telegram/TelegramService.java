package net.zsoo.mythic.mythicweb.telegram;

public interface TelegramService {

    /**
     * 텔레그램 메시지 수신 시
     * 
     * @param chatId
     * @param message
     * @return 응답 메시지, or null 무응답
     */
    String onMessage(long chatId, String message);
}
