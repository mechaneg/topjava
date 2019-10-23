package ru.javawebinar.topjava.repository.jdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@ContextConfiguration({"classpath:spring/spring-repository.xml", "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JdbcMealRepositoryTest {

    @Autowired
    private JdbcMealRepository repository;

    @Test
    public void save() {
        {
            // saving new meal
            Meal newMeal = new Meal(LocalDateTime.now(), "huge pizza", 3000);
            Meal savedMeal = repository.save(new Meal(newMeal), UserTestData.USER_ID);
            MealTestData.assertMatchWithoutIds(newMeal, savedMeal);
        }
        {
            // updating existing meal
            Meal meal = MealTestData.MEAL_1;
            meal.setCalories(30000);
            Meal updatedMeal = repository.save(new Meal(meal), UserTestData.USER_ID);
            MealTestData.assertMatchWithoutIds(meal, updatedMeal);
        }
        {
            // Trying update meal belonging to other user
            assertNull(repository.save(MealTestData.MEAL_1, UserTestData.ADMIN_ID));
        }
    }

    @Test
    public void delete() {
        assertTrue(repository.delete(MealTestData.MEAL_1_ID, UserTestData.USER_ID));
        assertFalse(repository.delete(MealTestData.MEAL_2_ID, UserTestData.ADMIN_ID));
        assertFalse(repository.delete(MealTestData.NON_EXISTING_MEAL_ID, UserTestData.USER_ID));
    }

    @Test
    public void get() {
        {
            Meal meal = repository.get(MealTestData.MEAL_1_ID, UserTestData.USER_ID);
            MealTestData.assertMatch(MealTestData.MEAL_1, meal);
        }
        {
            // Meal doesn't belong to user
            Meal meal = repository.get(MealTestData.MEAL_1_ID, UserTestData.ADMIN_ID);
            assertNull(meal);
        }
        {
            // Non-existing meal
            Meal meal = repository.get(MealTestData.NON_EXISTING_MEAL_ID, UserTestData.USER_ID);
            assertNull(meal);
        }
    }

    @Test
    public void getAll() {
        {
            List<Meal> meals = repository.getAll(UserTestData.USER_ID);
            MealTestData.assertMatch(meals, MealTestData.MEAL_3, MealTestData.MEAL_2, MealTestData.MEAL_1);
        }
        {
            // Admin has no meals
            List<Meal> meals = repository.getAll(UserTestData.ADMIN_ID);
            MealTestData.assertMatch(meals);
        }
    }

    @Test
    public void getBetween() {
        {
            // filtering only lunch
            LocalDateTime beforeLunch = LocalDateTime.of(2019, 6, 6, 12, 0, 0);
            LocalDateTime afterLunch = LocalDateTime.of(2019, 6, 6, 15, 0, 0);
            List<Meal> meals = repository.getBetween(beforeLunch, afterLunch, UserTestData.USER_ID);
            MealTestData.assertMatch(meals, MealTestData.MEAL_2);
        }
        {
            // check inclusive time ends
            LocalDateTime leftTime = LocalDateTime.of(2019, 6, 6, 8, 0, 0);
            LocalDateTime rightTime = LocalDateTime.of(2019, 6, 6, 13, 0, 0);
            List<Meal> meals = repository.getBetween(leftTime, rightTime, UserTestData.USER_ID);
            MealTestData.assertMatch(meals, MealTestData.MEAL_2, MealTestData.MEAL_1);
        }
        {
            // Admin has no meals
            LocalDateTime dayStart = LocalDateTime.of(2019, 6, 6, 0, 0, 0);
            LocalDateTime dayEnd = LocalDateTime.of(2019, 6, 7, 0, 0, 0);
            List<Meal> meals = repository.getBetween(dayStart, dayEnd, UserTestData.ADMIN_ID);
            MealTestData.assertMatch(meals);
        }
    }
}