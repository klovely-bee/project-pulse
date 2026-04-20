package edu.tcu.cs.projectpulsebackend.controller;

import edu.tcu.cs.projectpulsebackend.dto.ActiveWeekResponse;
import edu.tcu.cs.projectpulsebackend.dto.ActiveWeekSetupRequest;
import edu.tcu.cs.projectpulsebackend.service.ActiveWeekService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sections/{sectionId}/weeks")
public class ActiveWeekController {

    private final ActiveWeekService activeWeekService;

    public ActiveWeekController(ActiveWeekService activeWeekService) {
        this.activeWeekService = activeWeekService;
    }

    @GetMapping
    public List<ActiveWeekResponse> getWeeksForSection(@PathVariable Long sectionId) {
        return activeWeekService.getWeeksForSection(sectionId);
    }

    @PutMapping
    public List<ActiveWeekResponse> setupActiveWeeks(
            @PathVariable Long sectionId,
            @RequestBody ActiveWeekSetupRequest request) {
        return activeWeekService.setupActiveWeeks(sectionId, request);
    }
}
