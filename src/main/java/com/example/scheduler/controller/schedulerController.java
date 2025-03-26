package com.example.scheduler.controller;

import com.example.scheduler.dto.SchedulerDeleteRequestDto;
import com.example.scheduler.dto.SchedulerCreationRequestDto;
import com.example.scheduler.dto.SchedulerResponseDto;
import com.example.scheduler.dto.SchedulerUpdateRequestDto;
import com.example.scheduler.service.SchedulerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/schedules")
@RestController
public class schedulerController {

    private final SchedulerService schedulerService;

    public schedulerController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @PostMapping
    public ResponseEntity<SchedulerResponseDto> createSchedule(
            @Valid @RequestBody SchedulerCreationRequestDto schedulerCreationRequestDto) {
        return new ResponseEntity<>(
                schedulerService.createSchedule(
                        schedulerCreationRequestDto.getName(),
                        schedulerCreationRequestDto.getPassword(),
                        schedulerCreationRequestDto.getTodo(),
                        schedulerCreationRequestDto.getEmail()
                ), HttpStatus.CREATED
        );
    }

    @Validated
    @GetMapping("/{authorId}")
    public ResponseEntity<SchedulerResponseDto> findScheduleByAuthorIdOrElseThrow(
            @NotNull(message = "작성자 식별 번호를 필수로 입력해주셔야 합니다.") @PathVariable Long authorId) {
        return new ResponseEntity<>(schedulerService.findScheduleByAuthorIdOrElseThrow(authorId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SchedulerResponseDto>> findAllSchedules(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String modificationDate) {

        return new ResponseEntity<>(schedulerService.findAllSchedules(name, modificationDate), HttpStatus.OK);
    }

    @Validated
    @PatchMapping("/{authorId}")
    public ResponseEntity<SchedulerResponseDto> updateScheduleByAuthorId(
            @NotNull(message = "작성자 식별 번호를 필수로 입력해주셔야 합니다.") @PathVariable Long authorId,
            @Valid @RequestBody SchedulerUpdateRequestDto schedulerUpdateRequestDto) {
        return new ResponseEntity<>(schedulerService.updateScheduleByAuthorId(
                authorId,
                schedulerUpdateRequestDto.getName(),
                schedulerUpdateRequestDto.getPassword(),
                schedulerUpdateRequestDto.getTodo()),
                HttpStatus.OK);
    }

    @Validated
    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> deleteScheduleByAuthorId(@NotNull(message = "작성자 식별 번호를 필수로 입력해주셔야 합니다.")
                                                         @PathVariable Long authorId,
                                                         @Valid @RequestBody SchedulerDeleteRequestDto schedulerDeleteRequestDto) {
        schedulerService.deleteScheduleByAuthorId(authorId, schedulerDeleteRequestDto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}