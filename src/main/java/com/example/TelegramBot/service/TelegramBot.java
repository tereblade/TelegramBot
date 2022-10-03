package com.example.TelegramBot.service;

import com.example.TelegramBot.api.model.DotaHeroesDto;
import com.example.TelegramBot.config.BotConfig;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (message) {
                case "/start":
                    String messageToSend = String.format("Приветствую тебя, %s %s", update.getMessage().getFrom().getFirstName(), update.getMessage().getFrom().getLastName());
                    sendTextMessage(messageToSend, chatId);
                    break;
                case "/choose_hero":
                    HeroesCommand heroesCommand = new HeroesCommand();
                    DotaHeroesDto dotaHeroesDto = heroesCommand.getRandomHero();
                    sendImageMessage(dotaHeroesDto.getLocalized_name(), chatId);
                    messageToSend = String.format("Твой герой на следующую игру: %s." +
                            "\nОсновной аттрибут: %s" +
                            "\nОн мили/ренджевик: %s" +
                            "\nУ него следующие роли: %s", dotaHeroesDto.getLocalized_name(), dotaHeroesDto.getPrimary_attr(), dotaHeroesDto.getAttack_type(), String.join(",", dotaHeroesDto.getRoles()));
                    sendTextMessage(messageToSend, chatId);
                    break;
                default:
                    sendTextMessage("Уууупс, я не знаю, что тебе на это сказать. Попробуй ещё раз!", chatId);
                    break;
            }
        }
    }

    private void sendTextMessage(String text, long chatId) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendImageMessage(String heroName, long chatId) throws FileNotFoundException {
        SendPhoto message = new SendPhoto();
        FileInputStream fileInputStream = new FileInputStream(String.format("src/main/resources/images/%s.png", heroName));
        message.setPhoto(new InputFile(fileInputStream, String.format("%s.png", heroName)));
        message.setChatId(chatId);
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
