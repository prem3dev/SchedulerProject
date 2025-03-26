package com.example.scheduler.dto;

import com.example.scheduler.entity.Author;
import com.example.scheduler.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@Getter
public class SchedulerResponseDto {
    private Long id;

    private String name;

    private String password;

    private String email;

    private String todo;

    private LocalDateTime creationTimestamp;

    private LocalDateTime modificationTimestamp;

    public SchedulerResponseDto(ResponseAuthorDto responseAuthorDto, ResponseScheduleDto responseScheduleDto) {
        this.id = responseAuthorDto.getId();
        this.name = responseAuthorDto.getName();
        this.password = responseScheduleDto.getPassword();
        this.email = responseAuthorDto.getEmail();
        this.todo = responseScheduleDto.getTodo();
        this.creationTimestamp = responseAuthorDto.getCreationTimestamp();
        this.modificationTimestamp = responseAuthorDto.getModificationTimestamp();
    }
}
