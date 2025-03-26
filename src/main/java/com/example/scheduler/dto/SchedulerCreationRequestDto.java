package com.example.scheduler.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class SchedulerCreationRequestDto {

    @NotNull(message = "작성자 명 필수로 입력해주셔야 합니다.")
    private String name;

    @NotNull(message = "비밀번호를 필수로 입력해주셔야 합니다.")
    private String password;

    @Email(message = "email 양식에 맞지 않습니다.")
    @NotNull(message = "Email을 필수로 입력해주셔야 합니다.")
    private String email;

    @NotNull(message = "일정을 필수로 작성 해주셔야 합니다.")
    @Size(max = 200, message = "일정 작성 제한은 200자입니다.")
    private String todo;
}