package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.*;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository mealRepo;
    private final CrudUserRepository userRepo;

    public DataJpaMealRepository(CrudMealRepository mealRepo, CrudUserRepository userRepo) {
        this.mealRepo = mealRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        }

        User user = userRepo.getOne(userId);
        meal.setUser(user);

        return mealRepo.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return mealRepo.deleteByIdAndUserId(id, userId) > 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return mealRepo.findByIdAndUserId(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return mealRepo.findByUserIdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetweenInclusive(LocalDate startDate, LocalDate endDate, int userId) {
        return mealRepo.findByUserIdAndDateTimeGreaterThanEqualAndDateTimeLessThanOrderByDateTimeDesc(
                userId, getStartInclusive(startDate), getEndExclusive(endDate)
        );
    }
}
