package team.projectpulse.auth.controller;

import team.projectpulse.system.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public Result health() {
        return Result.success("Project Pulse backend is running", Map.of("service", "backend"));
    }
}
