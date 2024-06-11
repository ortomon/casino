package org.javaacademy.casino.repository;

import lombok.RequiredArgsConstructor;
import org.javaacademy.casino.entity.Game;
import org.springframework.data.relational.core.sql.SQL;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GameRepository {
    private final JdbcTemplate jdbcTemplate;

    public void create(Game game) {
        String sql = """
                insert into game ("1_sym", "2_sym", "3_sym") values (?, ?, ?)
                """;
        jdbcTemplate.update(sql, game.getSym1(), game.getSym2(), game.getSym3());
    }

    public List<Game> findAllByPortion(int portion) {
        String sql = """
                select * from game order by id desc limit ?
                """;
        return jdbcTemplate.queryForStream(sql, this::gameRowMapper, portion).toList();
    }

    private Game gameRowMapper(ResultSet resultSet, int rowNum) {
        try {
            Game game = new Game();
            game.setId(resultSet.getInt("id"));
            game.setSym1(resultSet.getString("1_sym"));
            game.setSym2(resultSet.getString("2_sym"));
            game.setSym3(resultSet.getString("3_sym"));
            return game;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
