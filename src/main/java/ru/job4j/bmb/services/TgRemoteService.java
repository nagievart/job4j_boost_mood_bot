package ru.job4j.bmb.services;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Service
public class TgRemoteService extends TelegramLongPollingBot {

    private final String botName;
    private final String botToken;

    public TgRemoteService(@Value("${telegram.bot.name}") String botName,
                           @Value("${telegram.bot.token}") String botToken,
                           @Value("${telegram.proxy.host:}") String proxyHost,
                           @Value("${telegram.proxy.port:0}") int proxyPort) {
        super(createBotOptions(proxyHost, proxyPort));
        this.botName = botName;
        this.botToken = botToken;
        System.out.println("TgRemoteService создан");
    }

    private static DefaultBotOptions createBotOptions(String proxyHost, int proxyPort) {
        DefaultBotOptions botOptions = new DefaultBotOptions();

        if (proxyHost != null && !proxyHost.isEmpty() && proxyPort > 0) {
            botOptions.setProxyHost(proxyHost);
            botOptions.setProxyPort(proxyPort);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
            System.out.println("Настроен SOCKS5 прокси: " + proxyHost + ":" + proxyPort);
        } else {
            System.out.println("Прокси не настроен");
        }

        return botOptions;
    }

    @PostConstruct
    public void init() {
        System.out.println("Инициализация бота...");
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            System.out.println("Бот успешно зарегистрирован и запущен");
        } catch (TelegramApiException e) {
            System.err.println("Ошибка регистрации бота: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Получен update: " + update);

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            System.out.println("Сообщение от " + chatId + ": " + messageText);

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Вы написали: " + messageText);
            try {
                execute(message);
                System.out.println("Ответ отправлен");
            } catch (TelegramApiException e) {
                System.err.println("Ошибка при отправке: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}