package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class MealsServlet extends HttpServlet {

    private static final int MAX_CALORIES_PER_DAY = 2000;

    private static final List<Meal> meals = Arrays.asList(
            new Meal(LocalDateTime.of(2019, 10, 7, 21, 0), "Завтрак", 2000),
            new Meal(LocalDateTime.of(2019, 10, 7, 16, 47), "hkhk", 1000),
            new Meal(LocalDateTime.of(2019, 5, 30, 10, 0), "Breakfast", 500)
    );

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("mealsList", MealsUtil.getWithExcessInfo(meals, MAX_CALORIES_PER_DAY));
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }
}
