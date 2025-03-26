package com.example.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ResponseAuthorDto {

    private Long id;

    private String name;

    private String email;

    private LocalDateTime creationTimestamp;

    private LocalDateTime modificationTimestamp;

}
