package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        List<FilterWithExceededMethod> methods = Arrays.asList(
                UserMealsUtil::getFilteredWithExceeded,
                UserMealsUtil::getFilteredWithExceededUsingStreams
        );

        for (FilterWithExceededMethod method : methods) {
            List<UserMealWithExceed> result = method.apply(
                    mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000
            );

            assert result.size() == 2;
            assert result.get(0).getDescription().compareTo("Завтрак") == 0;
            assert !result.get(0).isExceed();

            assert result.get(1).getDescription().compareTo("Завтрак") == 0;
            assert result.get(1).isExceed();
        }
    }

    private static List<UserMealWithExceed> getFilteredWithExceeded(
            List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        HashMap<LocalDate, Integer> dateTotalCalories = new HashMap<>();

        for (UserMeal userMeal : mealList) {
            dateTotalCalories.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
        }

        List<UserMealWithExceed> result = new ArrayList<>();

        for (UserMeal userMeal : mealList) {
            LocalTime userMealTime = userMeal.getDateTime().toLocalTime();
            LocalDate userMealDate = userMeal.getDateTime().toLocalDate();
            if (TimeUtil.isBetween(userMealTime, startTime, endTime)) {
                result.add(
                        new UserMealWithExceed(
                                userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
                                dateTotalCalories.get(userMealDate) > caloriesPerDay
                        )
                );
            }
        }

        return result;
    }

    private static List<UserMealWithExceed>
    getFilteredWithExceededUsingStreams(
            List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> dateTotalCalories = mealList.stream().collect(toMap(
                userMeal -> userMeal.getDateTime().toLocalDate(),
                UserMeal::getCalories,
                Integer::sum
        ));

        return mealList.stream()
                .filter(userMeal -> TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExceed(
                        userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
                        dateTotalCalories.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(toList());
    }

    private interface FilterWithExceededMethod {
        List<UserMealWithExceed>
        apply(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay);
    }
}
