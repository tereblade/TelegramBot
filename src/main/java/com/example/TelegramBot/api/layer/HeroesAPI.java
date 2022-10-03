package com.example.TelegramBot.api.layer;

import com.example.TelegramBot.api.API;
import com.example.TelegramBot.api.model.Response;

public class HeroesAPI extends API {
    private final String HEROES_ENDPOINT = "/heroes";

    public Response getAllHeroes() {
        return doGet(BASE_URL.concat(HEROES_ENDPOINT));
    }
}
