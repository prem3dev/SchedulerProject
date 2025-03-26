package com.example.scheduler.repository;

import com.example.scheduler.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcTemplateSchedulerRepository implements SchedulerRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateSchedulerRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Number saveAuthorsTableAndReturnKey(RequestSaveAuthorDto requestSaveAuthorDto) {
        SimpleJdbcInsert authorSimpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        authorSimpleJdbcInsert.withTableName("authors")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "email");
        Map<String, Object> authorParameters = new HashMap<>();
        authorParameters.put("name", requestSaveAuthorDto.getName());
        authorParameters.put("email", requestSaveAuthorDto.getEmail());
        Number key = authorSimpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(authorParameters));
        return key;
    }

    @Transactional
    @Override
    public ResponseAuthorDto saveAuthorsTable(RequestSaveAuthorDto requestSaveAuthorDto) {
        SimpleJdbcInsert authorSimpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        authorSimpleJdbcInsert.withTableName("authors")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "email");
        Map<String, Object> authorParameters = new HashMap<>();
        authorParameters.put("name", requestSaveAuthorDto.getName());
        authorParameters.put("email", requestSaveAuthorDto.getEmail());
        Number key = authorSimpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource(authorParameters));

        return new ResponseAuthorDto(key.longValue(),
                requestSaveAuthorDto.getName(),
                requestSaveAuthorDto.getEmail(),
                findAuthorTableByAuthorIdOrElseThrow(key.longValue()).getCreationTimestamp(),
                findAuthorTableByAuthorIdOrElseThrow(key.longValue()).getModificationTimestamp()
        );
    }

    @Override
    public ResponseScheduleDto saveSchedulesTable(Number key, RequestSaveScheduleDto requestSaveScheduleDto) {

        SimpleJdbcInsert scheduleSimpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);

        scheduleSimpleJdbcInsert.withTableName("schedules").usingGeneratedKeyColumns("id");

        Map<String, Object> scheduleParameters = new HashMap<>();

        scheduleParameters.put("password", requestSaveScheduleDto.getPassword());
        scheduleParameters.put("todo", requestSaveScheduleDto.getTodo());
        scheduleParameters.put("author_id", key.longValue());

        scheduleSimpleJdbcInsert.execute(new MapSqlParameterSource(scheduleParameters));
        return new ResponseScheduleDto(requestSaveScheduleDto.getPassword(), requestSaveScheduleDto.getTodo());
    }

    @Override
    public ResponseAuthorDto findAuthorTableByAuthorIdOrElseThrow(Long authorId) {
        List<ResponseAuthorDto> result = jdbcTemplate.query(
                "select * from authors where id = ?", authorTableRowMapper(), authorId
        );
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseScheduleDto findScheduleTableByAuthorIdOrElseThrow(Long authorId) {
        List<ResponseScheduleDto> result = jdbcTemplate.query(
                "select password, todo " +
                        "from schedules " +
                        "where  author_id = ?",
                scheduleTableRowMapper(),
                authorId);
        return result.stream().findAny().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<SchedulerResponseDto> findSchedules(String name, String modificationDate) {

        StringBuilder query = new StringBuilder("select * " +
                "from authors " +
                "left join schedules " +
                "on authors.id = schedules.author_id ");

        List<String> conditions = new ArrayList<>();
        List<Object> queryArgs = new ArrayList<>();

        if (name != null) {
            conditions.add("authors.name = ?");
            queryArgs.add(name);
        }

        if(modificationDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime localModificationDate = LocalDate.parse(modificationDate, formatter).atStartOfDay();
            Timestamp modificationTimestamp = Timestamp.valueOf(localModificationDate);
            conditions.add("authors.modification_timestamp <= ?");
            queryArgs.add(modificationTimestamp);
        }

        if(!conditions.isEmpty()){
            query.append("where ").append(String.join(" and ", conditions)).append(" ");
        }
        query.append("order by authors.modification_timestamp desc");

        List<SchedulerResponseDto> result = jdbcTemplate.query(
                query.toString(), schedulerRowMapperToDto(), queryArgs.toArray()
        );
        return result;
    }

    @Override
    public int updateAuthorTable(RequestUpdateAuthorDto requestUpdateAuthorDto) {
        return jdbcTemplate.update("update authors set name = ? where id = ?",
                requestUpdateAuthorDto.getName(),
                requestUpdateAuthorDto.getId());
    }

    @Override
    public int updateScheduleTable(RequestUpdateScheduleDto requestUpdateScheduleDto) {
        return jdbcTemplate.update("update schedules set todo = ? where author_id = ?",
                requestUpdateScheduleDto.getTodo(),
                requestUpdateScheduleDto.getAuthorId());
    }

    @Override
    public int deleteAuthorFromDbByAuthorId(Long authorId) {
        return jdbcTemplate.update("delete from authors where id = ?",
                authorId);
    }

    public RowMapper<ResponseAuthorDto> authorTableRowMapper() {
        return new RowMapper<ResponseAuthorDto>() {
            @Override
            public ResponseAuthorDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ResponseAuthorDto(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getTimestamp("creation_timestamp").toLocalDateTime(),
                        rs.getTimestamp("modification_timestamp").toLocalDateTime());
            }
        };
    }

    public RowMapper<ResponseScheduleDto> scheduleTableRowMapper() {
        return new RowMapper<ResponseScheduleDto>() {
            @Override
            public ResponseScheduleDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ResponseScheduleDto(rs.getString("password"),
                        rs.getString("todo"));
            }
        };
    }

    public RowMapper<SchedulerResponseDto> schedulerRowMapperToDto() {
        return new RowMapper<SchedulerResponseDto>() {
            @Override
            public SchedulerResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new SchedulerResponseDto(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("todo"),
                        rs.getTimestamp("creation_timestamp").toLocalDateTime(),
                        rs.getTimestamp("modification_timestamp").toLocalDateTime()
                );
            }
        };
    }
}