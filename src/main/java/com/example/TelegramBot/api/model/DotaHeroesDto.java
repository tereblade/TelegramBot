package com.example.TelegramBot.api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class DotaHeroesDto {
    public int id;
    public String name;
    public String localized_name;
    public String primary_attr;
    public String attack_type;
    public ArrayList<String> roles;
    public int legs;
}
