package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_1_ID = UserTestData.OTHER_USER_ID + 1;
    public static final int MEAL_2_ID = MEAL_1_ID + 1;
    public static final int MEAL_3_ID = MEAL_2_ID + 1;
    public static final int MEAL_4_ID = MEAL_3_ID + 1;
    public static final int MEAL_5_ID = MEAL_4_ID + 1;
    public static final int MEAL_6_ID = MEAL_5_ID + 1;
    public static final int NON_EXISTING_MEAL_ID = START_SEQ + 100500;

    public static final Meal MEAL_1 = new Meal(MEAL_1_ID, LocalDateTime.of(2019, 6, 6, 8, 0, 0), "kashki bahnul", 500);
    public static final Meal MEAL_2 = new Meal(MEAL_2_ID, LocalDateTime.of(2019, 6, 6, 13, 0, 0), "supchik", 1500);
    public static final Meal MEAL_3 = new Meal(MEAL_3_ID, LocalDateTime.of(2019, 6, 6, 19, 0, 0), "burgerok", 2500);
    public static final Meal MEAL_4 = new Meal(MEAL_4_ID, LocalDateTime.of(2019, 6, 7, 0, 0, 0), "kashki bahnul", 500);
    public static final Meal MEAL_5 = new Meal(MEAL_5_ID, LocalDateTime.of(2019, 6, 7, 15, 1, 0), "mars", 300);
    public static final Meal MEAL_6 = new Meal(MEAL_6_ID, LocalDateTime.of(2019, 6, 7, 15, 2, 0), "bounty", 300);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatchWithoutIds(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "id");
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
