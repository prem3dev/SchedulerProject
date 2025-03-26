package com.example.scheduler.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SchedulerUpdateRequestDto {

    String name;

    @NotNull(message = "비밀번호를 필수로 입력해주셔야 합니다.")
    String password;

    String todo;
}
