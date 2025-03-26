package com.example.scheduler.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SchedulerDeleteRequestDto {

   @NotNull(message = "비밀번호를 필수로 입력해주셔야 합니다.")
   private String password;
}
