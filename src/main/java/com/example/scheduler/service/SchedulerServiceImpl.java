package com.example.scheduler.service;

import com.example.scheduler.dto.*;
import com.example.scheduler.repository.SchedulerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class SchedulerServiceImpl implements SchedulerService {

    private final SchedulerRepository schedulerRepository;

    public SchedulerServiceImpl(SchedulerRepository schedulerRepository) {
        this.schedulerRepository = schedulerRepository;
    }

    @Transactional
    @Override
    public SchedulerResponseDto createSchedule(String name, String password, String todo, String email) {

        RequestSaveAuthorDto requestSaveAuthorDto = new RequestSaveAuthorDto(name, email);
        RequestSaveScheduleDto requestSaveScheduleDto = new RequestSaveScheduleDto(password, todo);
        Number key = schedulerRepository.saveAuthorsTableAndReturnKey(requestSaveAuthorDto);
        ResponseScheduleDto responseScheduleDto = schedulerRepository.saveSchedulesTable(key, requestSaveScheduleDto);
        ResponseAuthorDto responseAuthorDto = schedulerRepository.findAuthorTableByAuthorIdOrElseThrow(key.longValue());
        return new SchedulerResponseDto(responseAuthorDto, responseScheduleDto);
    }

    @Transactional
    @Override
    public SchedulerResponseDto findScheduleByAuthorIdOrElseThrow(Long authorId) {
        return new SchedulerResponseDto(schedulerRepository.findAuthorTableByAuthorIdOrElseThrow(authorId),
                schedulerRepository.findScheduleTableByAuthorIdOrElseThrow(authorId));
    }

    @Override
    public List<SchedulerResponseDto> findAllSchedules(String name, String modificationDate) {
        return schedulerRepository.findSchedules(name, modificationDate);
    }

    @Transactional
    @Override
    public SchedulerResponseDto updateScheduleByAuthorId(Long authorId, String name, String password, String todo) {

        if (!password.equals(schedulerRepository.findScheduleTableByAuthorIdOrElseThrow(authorId).getPassword())) {
            throw new IllegalArgumentException();
        }
        int updateAuthorTable;
        int updateScheduleTable;
        if (name != null) {
            updateAuthorTable = schedulerRepository.updateAuthorTable(
                    new RequestUpdateAuthorDto(authorId, name));
            if (updateAuthorTable == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }

        if (todo != null) {
            updateScheduleTable = schedulerRepository.updateScheduleTable(
                    new RequestUpdateScheduleDto(authorId, todo));
            if (updateScheduleTable == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }

        return new SchedulerResponseDto(schedulerRepository.findAuthorTableByAuthorIdOrElseThrow(authorId),
                schedulerRepository.findScheduleTableByAuthorIdOrElseThrow(authorId)
        );
    }

    @Transactional
    @Override
    public void deleteScheduleByAuthorId(Long authorId, String password) {
        if (!password.equals(schedulerRepository.findScheduleTableByAuthorIdOrElseThrow(authorId).getPassword())) {
            throw new IllegalArgumentException();
        } else {
            int deletedAuthorRow = schedulerRepository.deleteAuthorFromDbByAuthorId(authorId);
            if (deletedAuthorRow == 0) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
    }
}