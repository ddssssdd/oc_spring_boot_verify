package com.example.tableviewer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TableService {

    private static final Logger log = LoggerFactory.getLogger(TableService.class);

    private final JdbcTemplate jdbcTemplate;

    public TableService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> getAllTables() {
        String sql = """
            SELECT table_name
            FROM information_schema.tables
            WHERE table_schema = 'public'
              AND table_type = 'BASE TABLE'
            ORDER BY table_name
            """;
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public Map<String, Object> getTableData(String tableName, int page, int size, String sortColumn, String direction, int maxColumns) {
        Map<String, Object> result = new LinkedHashMap<>();

        if (!isValidTableName(tableName)) {
            throw new IllegalArgumentException("Invalid table name");
        }

        List<String> columns = getColumns(tableName, maxColumns);
        result.put("columns", columns);

        String countSql = "SELECT COUNT(*) FROM " + tableName;
        Long total = jdbcTemplate.queryForObject(countSql, Long.class);
        result.put("total", total);

        String sortCol = columns.contains(sortColumn) ? sortColumn : columns.get(0);
        String dir = "asc".equalsIgnoreCase(direction) ? "ASC" : "DESC";
        int offset = page * size;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(String.join(", ", columns));
        sql.append(" FROM ").append(tableName);
        sql.append(" ORDER BY ").append(sortCol).append(" ").append(dir);
        sql.append(" LIMIT ").append(size).append(" OFFSET ").append(offset);

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql.toString());
        result.put("rows", rows);

        return result;
    }

    public List<String> getColumns(String tableName, int limit) {
        String sql = """
            SELECT column_name
            FROM information_schema.columns
            WHERE table_schema = 'public' AND table_name = ?
            ORDER BY ordinal_position
            """;
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, tableName);
        return result.stream()
                .limit(limit)
                .map(row -> (String) row.get("column_name"))
                .toList();
    }

    private boolean isValidTableName(String tableName) {
        return tableName != null && tableName.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
    }
}
