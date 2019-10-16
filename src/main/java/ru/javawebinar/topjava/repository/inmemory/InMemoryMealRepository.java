package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.TimeFilter;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private class MealWithUid {
        private Meal meal;
        private int uid;

        Meal getMeal() {
            return meal;
        }

        void setMeal(Meal meal) {
            this.meal = meal;
        }

        int getUid() {
            return uid;
        }

        MealWithUid(Meal meal, int uid) {
            this.meal = meal;
            this.uid = uid;
        }
    }

    private AtomicInteger counter = new AtomicInteger(0);
    private Map<Integer, MealWithUid> repository = new ConcurrentHashMap<>();

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), new MealWithUid(meal, userId));
            return meal;
        }

        MealWithUid mealWithUid = repository.get(meal.getId());
        if (mealWithUid.getUid() != userId) {
            return null;
        }
        mealWithUid.setMeal(meal);

        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {

        MealWithUid mealWithUid = repository.get(id);
        if (mealWithUid == null || mealWithUid.getUid() != userId) {
            return false;
        }

        if (repository.remove(id) == null) {
            throw new ConcurrentModificationException("Meal with id " + id + " was deleted in parallel request");
        }

        return true;
    }

    @Override
    public Meal get(int id, int userId) {
        MealWithUid mealWithUid = repository.get(id);
        if (mealWithUid == null) {
            return null;
        }
        return mealWithUid.getUid() == userId
                ? mealWithUid.getMeal()
                : null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return getFiltered(userId, new TimeFilter());
    }

    @Override
    public Collection<Meal> getFiltered(int userId, TimeFilter timeFilter) {
        return repository
                .values()
                .stream()
                .filter(mealWithUid -> mealWithUid.getUid() == userId)
                .filter(
                        mealWithUid -> DateTimeUtil.isBetween(
                            mealWithUid.getMeal().getDate(),
                            timeFilter.getFromDate(),
                            timeFilter.getToDate()
                        )
                )
                .filter(
                        mealWithUid -> DateTimeUtil.isBetween(
                            mealWithUid.getMeal().getTime(),
                            timeFilter.getFromTime(),
                            timeFilter.getToTime()
                        )
                )
                .map(MealWithUid::getMeal)
                .sorted(Comparator.comparing(Meal::getDate).reversed().thenComparing(Meal::getId))
                .collect(Collectors.toList());
    }
}

