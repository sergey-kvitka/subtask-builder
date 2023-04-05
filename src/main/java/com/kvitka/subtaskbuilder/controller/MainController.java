package com.kvitka.subtaskbuilder.controller;

import com.kvitka.subtaskbuilder.dto.FinalResultDto;
import com.kvitka.subtaskbuilder.dto.TaskDto;
import com.kvitka.subtaskbuilder.service.SubtaskRegistryService;
import com.kvitka.subtaskbuilder.service.TaskPartitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MainController {

    private final SubtaskRegistryService subtaskRegistryService;
    private final TaskPartitionService taskPartitionService;

    @PutMapping("/sendSubtaskResult")
    public void sendSubtaskResult(@RequestBody List<String> result) {
        // ? 1st arg - answer (index), 2nd - sum, 3rd - time (ms)
        log.info("Subtask result: {} (MS: {})", result, System.currentTimeMillis());
        subtaskRegistryService.saveSubtaskResult(result);
    }

    @GetMapping("/isFree")
    public boolean isFree() {
        return subtaskRegistryService.isFree();
    }

    @GetMapping("/getCurrentCompleteness")
    public long[] getCurrentCompleteness() {
        return subtaskRegistryService.getCurrentCompleteness();
    }

    @PostMapping("/sendTask")
    public ResponseEntity<?> sendTask(@RequestBody TaskDto taskDto) {
        log.info("Sending task... (MS: {})", System.currentTimeMillis());
        if (!subtaskRegistryService.isFree()) return ResponseEntity.status(400).body("В данный момент решается задача");
        return ResponseEntity.ok(taskPartitionService.splitAndSend(taskDto.getJsonMatrixString(), taskDto.getMatrixSize()));
    }

    @GetMapping("/getResult")
    public FinalResultDto getResult() {
        return taskPartitionService.getResult();
    }
}
