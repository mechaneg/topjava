package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-repository.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MealTestData.MEAL_1_ID, UserTestData.USER_ID);
        MealTestData.assertMatch(MealTestData.MEAL_1, meal);
    }

    @Test(expected = NotFoundException.class)
    public void getNonExistingMeal() {
        Meal meal = service.get(MealTestData.NON_EXISTING_MEAL_ID, UserTestData.USER_ID);
        MealTestData.assertMatch(MealTestData.MEAL_1, meal);
    }

    @Test(expected = NotFoundException.class)
    public void getByWrongUser() {
        Meal meal = service.get(MealTestData.MEAL_1_ID, UserTestData.OTHER_USER_ID);
        MealTestData.assertMatch(MealTestData.MEAL_1, meal);
    }

    @Test
    public void delete() {
        service.delete(MealTestData.MEAL_1_ID, UserTestData.USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNonExistentMeal() {
        service.delete(MealTestData.NON_EXISTING_MEAL_ID, UserTestData.USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteByWrongUser() {
        service.delete(MealTestData.MEAL_1_ID, UserTestData.OTHER_USER_ID);
    }

    @Test
    public void getBetweenDatesLeftIncluded() {
        List<Meal> meals = service.getBetweenDates(LocalDate.of(2019, 6,7), LocalDate.of(2019, 6,7), UserTestData.OTHER_USER_ID);
        MealTestData.assertMatch(meals, MealTestData.MEAL_6, MealTestData.MEAL_5, MealTestData.MEAL_4);
    }

    @Test
    public void getBetweenDatesRightIncluded() {
        List<Meal> meals = service.getBetweenDates(LocalDate.of(2019, 6,6), LocalDate.of(2019, 6,6), UserTestData.OTHER_USER_ID);
        MealTestData.assertMatch(meals, MealTestData.MEAL_4);
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(UserTestData.USER_ID);
        MealTestData.assertMatch(meals, MealTestData.MEAL_3, MealTestData.MEAL_2, MealTestData.MEAL_1);
    }

    @Test
    public void getAllIfUserHasNoMeal() {
        List<Meal> meals = service.getAll(UserTestData.ADMIN_ID);
        assertTrue(meals.isEmpty());
    }

    @Test
    public void update() {
        Meal meal = new Meal(MealTestData.MEAL_1);
        meal.setCalories(30000);
        service.update(new Meal(meal), UserTestData.USER_ID);
        MealTestData.assertMatch(meal, service.get(MealTestData.MEAL_1_ID, UserTestData.USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void updateForeignMeal() {
        Meal meal = new Meal(MealTestData.MEAL_1);
        meal.setCalories(30000);
        service.update(new Meal(meal), UserTestData.ADMIN_ID);
    }

    @Test(expected = DataAccessException.class)
    public void updateNewMeal() {
        Meal meal = new Meal(MealTestData.MEAL_1);
        meal.setId(null);
        service.update(new Meal(meal), UserTestData.USER_ID);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.now(), "huge pizza", 3000);
        Meal savedMeal = service.create(newMeal, UserTestData.USER_ID);
        MealTestData.assertMatchWithoutIds(newMeal, savedMeal);
    }

    @Test(expected = DataAccessException.class)
    public void createDuplicate() {
        Meal newMeal = new Meal(MealTestData.MEAL_1);
        newMeal.setId(null);
        service.create(newMeal, UserTestData.USER_ID);
    }
}