package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class MealsProvider {
    private static final List<Meal> meals = Arrays.asList(
            new Meal(LocalDateTime.of(2019, 10, 07, 21, 00), "Завтрак", 2000),
            new Meal(LocalDateTime.of(2019, 10, 07, 16, 47), "hkhk", 1000),
            new Meal(LocalDateTime.of(2019, 05, 30, 10, 00), "Breakfast", 500)
    );

    public static List<Meal> getMeals() {
        return meals;
    }
}
