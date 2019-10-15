package ru.javawebinar.topjava.repository.inmemory;

import org.junit.Before;
import org.junit.Test;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class InMemoryMealRepositoryTest {

    private InMemoryMealRepository rep;

    @Before
    public void setUp() {
        rep = new InMemoryMealRepository();
    }

    @Test
    public void delete() {
        // empty repository
        assertFalse(rep.delete(1, 1));

        rep.save(new Meal(LocalDateTime.of(2019, 10, 10, 13, 0), "hey", 1000), 12);

        // non-existing user
        assertFalse(rep.delete(1, 21));

        // non-existing meal
        assertFalse(rep.delete(10, 12));

        // existing meal and user
        assertTrue(rep.delete(1, 12));
    }

    @Test
    public void get() {
        rep.save(new Meal(LocalDateTime.of(2019, 10, 10, 13, 0), "hey", 1000), 12);
        // Non-existing meal id
        assertNull(rep.get(123, 12));
        // Non-existing user id
        assertNull(rep.get(1, 21));
        // Existing both meal and user id
        assertNotNull(rep.get(1, 12));
    }

    @Test
    public void getAll() {
        rep.save(new Meal(LocalDateTime.of(2019, 10, 10, 13, 0), "hey", 1000), 12);
        rep.save(new Meal(LocalDateTime.of(2019, 10, 11, 13, 0), "hop", 1000), 12);

        {
            List<Meal> meals = (List<Meal>)rep.getAll(20);
            assertEquals(0, meals.size());
        }
        {
            List<Meal> meals = (List<Meal>)rep.getAll(12);
            assertEquals(2, meals.size());
            assertEquals(LocalDate.of(2019, 10, 11), meals.get(0).getDate());
            assertEquals(LocalDate.of(2019, 10, 10), meals.get(1).getDate());
        }
    }
}