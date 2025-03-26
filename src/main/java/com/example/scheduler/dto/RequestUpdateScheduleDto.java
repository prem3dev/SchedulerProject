package com.example.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RequestUpdateScheduleDto {
    Long authorId;
    String todo;
}
