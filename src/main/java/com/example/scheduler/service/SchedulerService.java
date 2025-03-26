package com.example.scheduler.service;

import com.example.scheduler.dto.SchedulerResponseDto;

import java.util.List;

public interface SchedulerService {

    SchedulerResponseDto createSchedule (String name, String password, String todo, String email);

    SchedulerResponseDto findScheduleByAuthorIdOrElseThrow(Long authorId);

    List<SchedulerResponseDto> findAllSchedules(String name, String modificationDate);

    SchedulerResponseDto updateScheduleByAuthorId(Long authorId, String name, String password, String todo);

    void deleteScheduleByAuthorId(Long authorId, String password);
}
