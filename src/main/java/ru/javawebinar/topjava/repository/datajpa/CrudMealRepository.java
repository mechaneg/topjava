package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    Meal findByIdAndUserId(int id, int userId);

    @Transactional
    Long deleteByIdAndUserId(int id, int userId);

    List<Meal> findByUserIdOrderByDateTimeDesc(int userId);

    List<Meal> findByUserIdAndDateTimeGreaterThanEqualAndDateTimeLessThanOrderByDateTimeDesc(
            int userId, LocalDateTime startDate, LocalDateTime endDate);
}