package ru.javawebinar.topjava.repository.inmemory;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.*;

public class InMemoryUserRepositoryTest {

    private InMemoryUserRepository rep;

    @Before
    public void setUp() {
        rep = new InMemoryUserRepository();
    }

    @Test
    public void deleteExisting() {
        rep.save(new User(null, "mike", "mike@ya.ru", "qqwerty", Role.ROLE_ADMIN));
        assertTrue(rep.delete(1));
    }

    @Test
    public void deleteNonExisting() {
        assertFalse(rep.delete(123));
    }

    @Test
    public void saveNew() {
        User saved = rep.save(new User(null, "mike", "mike@ya.ru", "qqwerty", Role.ROLE_ADMIN));
        assertNotNull(saved);
    }

    @Test
    public void saveNonExisting() {
        assertNull(rep.save(new User(100, "mike", "mike@ya.ru", "qqwerty", Role.ROLE_ADMIN)));
    }

    @Test
    public void get() {
        rep.save(new User(null, "mike", "mike@ya.ru", "qqwerty", Role.ROLE_ADMIN));
        assertNotNull(rep.get(1));
    }

    @Test
    public void getNonExisting() {
        assertNull(rep.get(100500));
    }

    @Test
    public void getAll() {
        rep.save(new User(null, "mike", "mike@ya.ru", "aaaa", Role.ROLE_ADMIN));
        rep.save(new User(null, "john", "john@ya.ru", "bbbb", Role.ROLE_ADMIN));

        List<User> users = rep.getAll();
        assertEquals(2, users.size());
        assertEquals("john", users.get(0).getName());
        assertEquals("mike", users.get(1).getName());
    }

    @Test
    public void getByEmail() {
        rep.save(new User(null, "mike", "mike@ya.ru", "aaaa", Role.ROLE_ADMIN));
        rep.save(new User(null, "john", "john@ya.ru", "bbbb", Role.ROLE_ADMIN));
        assertEquals("john@ya.ru", rep.getByEmail("john@ya.ru").getEmail());
    }

    @Test
    public void getByEmailNonExisting() {
        rep.save(new User(null, "mike", "mike@ya.ru", "aaaa", Role.ROLE_ADMIN));
        assertNull(rep.getByEmail("wowwowwow"));
    }
}
