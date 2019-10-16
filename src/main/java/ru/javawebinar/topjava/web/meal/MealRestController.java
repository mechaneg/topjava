package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.TimeFilter;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;

import java.util.List;

@Controller
public class MealRestController {
    @Autowired
    private MealService service;

    public List<MealTo> getAll() {
        return MealsUtil.getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public List<MealTo> getFilteredByTime(TimeFilter filter) {
        return MealsUtil.getTos(service.getFilteredByTime(authUserId(), filter), authUserCaloriesPerDay());
    }

    public void delete(int mealId) {
        service.delete(mealId, authUserId());
    }

    public void update(Meal meal, int mealId) {
        assureIdConsistent(meal, mealId);
        service.update(meal, authUserId());
    }

    public Meal create(Meal meal) {
        checkNew(meal);
        return service.create(meal, authUserId());
    }
}