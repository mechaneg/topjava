package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private AtomicInteger counter = new AtomicInteger(0);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private Map<Integer, Set<Integer>> uidToIds = new ConcurrentHashMap<>();

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);

            Set<Integer> ids = uidToIds.computeIfAbsent(userId, uid -> new HashSet<>());
            ids.add(meal.getId());

            return meal;
        }

        if (!getUserMealsIds(userId).contains(meal.getId())) {
            return null;
        }

        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (!getUserMealsIds(userId).contains(id)) {
            return false;
        }

        repository.remove(id);
        uidToIds.get(userId).remove(id);

        return true;
    }

    @Override
    public Meal get(int id, int userId) {
        if (!getUserMealsIds(userId).contains(id)) {
            return null;
        }
        return repository.get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        Set<Integer> userMealsIds = getUserMealsIds(userId);
        return repository
                .values()
                .stream()
                .filter(meal -> userMealsIds.contains(meal.getId()))
                .sorted(Comparator.comparing(Meal::getDate).reversed().thenComparing(Meal::getId))
                .collect(Collectors.toList());
    }

    private Set<Integer> getUserMealsIds(int userId) {
        return uidToIds.getOrDefault(userId, new HashSet<>());
    }
}

