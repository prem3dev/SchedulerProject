package com.example.scheduler.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
@AllArgsConstructor
@Getter
public class Author {

    private Long id;

    private String name;

    private String email;

    private LocalDateTime creationTimestamp;

    private LocalDateTime modificationTimestamp;
}
