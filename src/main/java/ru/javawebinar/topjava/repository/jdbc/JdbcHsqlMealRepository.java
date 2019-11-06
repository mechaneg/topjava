package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Profile(Profiles.HSQL_DB)
@Repository
public class JdbcHsqlMealRepository extends JdbcMealRepository<Timestamp> {

    private static final RowMapper<Meal> ROW_MAPPER = (rs, rowNum) -> {

        var meal = new Meal();
        meal.setId(rs.getInt("id"));
        meal.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
        meal.setDescription(rs.getString("description"));
        meal.setCalories(rs.getInt("calories"));
        meal.setUser(new User(rs.getInt("user_id"), null, null, null, Role.ROLE_USER));

        return meal;
    };

    public JdbcHsqlMealRepository(
            JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    RowMapper<Meal> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    Timestamp toSqlDateTime(LocalDateTime dateTime) {
        return Timestamp.valueOf(dateTime);
    }
}
