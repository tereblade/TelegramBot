package com.example.TelegramBot.service;

import com.example.TelegramBot.api.layer.HeroesAPI;
import com.example.TelegramBot.api.model.DotaHeroesDto;
import com.example.TelegramBot.api.model.Response;
import com.example.TelegramBot.api.util.JsonConverter;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Getter
@Component
public class HeroesCommand {
    private final List<DotaHeroesDto> recordList;
    private final HeroesAPI api;

    public HeroesCommand() {
        this.api = new HeroesAPI();
        Response response = api.getAllHeroes();
        if (response.getStatusCode() == 200) {
            this.recordList = Arrays.asList(JsonConverter.fromJson(response.getContent(), DotaHeroesDto[].class));
        } else {
            throw new NullPointerException("Response code doesn't equal 200!");
        }
    }

    public DotaHeroesDto getRandomHero() {
        Random random = new Random();
        int value = random.nextInt(((recordList.size() - 1)) + 1);
        return recordList.get(value);
    }
}
