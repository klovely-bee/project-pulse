package edu.tcu.cs.projectpulsebackend.controller;

import edu.tcu.cs.projectpulsebackend.dto.SectionCreateRequest;
import edu.tcu.cs.projectpulsebackend.dto.SectionResponse;
import edu.tcu.cs.projectpulsebackend.dto.SectionUpdateRequest;
import edu.tcu.cs.projectpulsebackend.service.SectionService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping
    public List<SectionResponse> findSections() {
        return sectionService.findSections();
    }

    @GetMapping("/{id}")
    public SectionResponse getSectionById(@PathVariable Long id) {
        return sectionService.getSectionById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SectionResponse createSection(@RequestBody SectionCreateRequest request) {
        return sectionService.createSection(request);
    }

    @PutMapping("/{id}")
    public SectionResponse updateSection(@PathVariable Long id, @RequestBody SectionUpdateRequest request) {
        return sectionService.updateSection(id, request);
    }
}
