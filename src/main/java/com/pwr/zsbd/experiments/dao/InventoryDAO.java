package com.pwr.zsbd.experiments.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@PropertySource("classpath:application.properties")
@Slf4j
public class InventoryDAO {

    private final JdbcTemplate jdbcTemplate;

    @Value("${sql.select.inventories}")
    private String query;

    @Autowired
    public InventoryDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> loadXML() {
        log.info("Loading all inventories as xml");
        return jdbcTemplate.query(query, (resultSet, i) -> resultSet.getString(1));
    }

    public void clearBufferCache() {
        jdbcTemplate.execute("alter system flush buffer_cache");
    }

    public void clearSharedPool() {
        jdbcTemplate.execute("ALTER SYSTEM FLUSH SHARED_POOL");
    }

}
