package com.example.scheduler.repository;

import com.example.scheduler.dto.*;

import java.time.LocalDateTime;
import java.util.List;

public interface SchedulerRepository {

    Number saveAuthorsTableAndReturnKey(RequestSaveAuthorDto requestSaveAuthorDto);

    ResponseAuthorDto saveAuthorsTable(RequestSaveAuthorDto requestSaveAuthorDto);

    ResponseScheduleDto saveSchedulesTable(Number key, RequestSaveScheduleDto requestSaveScheduleDto);

    ResponseAuthorDto findAuthorTableByAuthorIdOrElseThrow(Long authorId);

    ResponseScheduleDto findScheduleTableByAuthorIdOrElseThrow(Long authorId);

    List<SchedulerResponseDto> findSchedules(String name, String modificationDate);

    int updateAuthorTable(RequestUpdateAuthorDto requestUpdateAuthorDto);

    int updateScheduleTable(RequestUpdateScheduleDto requestUpdateScheduleDto);

    int deleteAuthorFromDbByAuthorId(Long authorId);
}
