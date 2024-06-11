package org.javaacademy.casino.repository;

import org.javaacademy.casino.entity.FinanceResult;
import org.javaacademy.casino.entity.Game;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Component
public class FinanceResultRepository {
    private JdbcTemplate jdbcTemplate;

    public Optional<FinanceResult> findResult() {
        String sql = """
                select * from finance_result limit 1
                """;
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::financeResultRowMapper));
    }

    public void addOutcome(BigDecimal outcome) {
        String sql = """
                update finance_result set outcome = outcome + ?
                """;
        jdbcTemplate.update(sql, outcome);
    }

    public void addIncome(BigDecimal income) {
        String sql = """
                update finance_result set income = income + ?
                """;
        jdbcTemplate.update(sql, income);
    }

    private FinanceResult financeResultRowMapper(ResultSet resultSet, int rowNum) {
        try {
            FinanceResult financeResult = new FinanceResult();
            financeResult.setIncome(resultSet.getBigDecimal("income"));
            financeResult.setOutcome(resultSet.getBigDecimal("outcome"));
            return financeResult;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
