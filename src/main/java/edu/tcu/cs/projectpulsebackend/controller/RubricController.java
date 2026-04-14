package edu.tcu.cs.projectpulsebackend.controller;

import edu.tcu.cs.projectpulsebackend.dto.RubricCreateRequest;
import edu.tcu.cs.projectpulsebackend.dto.RubricResponse;
import edu.tcu.cs.projectpulsebackend.service.RubricService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rubrics")
public class RubricController {

    private final RubricService rubricService;

    public RubricController(RubricService rubricService) {
        this.rubricService = rubricService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RubricResponse createRubric(@RequestBody RubricCreateRequest request) {
        return rubricService.createRubric(request);
    }

    @GetMapping
    public List<RubricResponse> getAllRubrics() {
        return rubricService.getAllRubrics();
    }

    @GetMapping("/{id}")
    public RubricResponse getRubricById(@PathVariable Long id) {
        return rubricService.getRubricById(id);
    }
}
