package com.projectpulse.backend.rubric.controller;

import com.projectpulse.backend.rubric.dto.CreateRubricRequest;
import com.projectpulse.backend.rubric.dto.RubricResponse;
import com.projectpulse.backend.rubric.service.RubricService;
import com.projectpulse.backend.shared.response.ApiResponse;
import com.projectpulse.backend.shared.response.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rubrics")
public class RubricController {

    private final RubricService rubricService;

    public RubricController(RubricService rubricService) {
        this.rubricService = rubricService;
    }

    @PostMapping
    public ApiResponse<RubricResponse> createRubric(@RequestBody CreateRubricRequest request) {
        return Result.success("Rubric created successfully", rubricService.createRubric(request));
    }
}
