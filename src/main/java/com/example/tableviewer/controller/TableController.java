package com.example.tableviewer.controller;

import com.example.tableviewer.service.TableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tables")
@CrossOrigin(origins = "*")
public class TableController {

    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllTables() {
        return ResponseEntity.ok(tableService.getAllTables());
    }

    @GetMapping("/{tableName}/data")
    public ResponseEntity<?> getTableData(
            @PathVariable String tableName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "10") int maxColumns
    ) {
        try {
            return ResponseEntity.ok(tableService.getTableData(tableName, page, size, sort, direction, maxColumns));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
