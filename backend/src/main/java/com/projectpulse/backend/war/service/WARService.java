package com.projectpulse.backend.war.service;

import com.projectpulse.backend.war.domain.WAR;
import com.projectpulse.backend.war.dto.CreateWarRequest;
import com.projectpulse.backend.war.dto.WarResponse;
import com.projectpulse.backend.war.repository.WARRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WARService {

    private final WARRepository warRepository;

    public WARService(WARRepository warRepository) {
        this.warRepository = warRepository;
    }

    public WarResponse createWAR(CreateWarRequest request) {
        WAR war = WAR.builder()
                .userId(request.getUserId())
                .teamId(request.getTeamId())
                .weekNumber(request.getWeekNumber())
                .content(request.getContent())
                .build();

        return toWarResponse(warRepository.save(war));
    }

    public List<WarResponse> getWARByStudent(Long userId) {
        return warRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toWarResponse)
                .toList();
    }

    public List<WarResponse> getWARByTeam(Long teamId) {
        return warRepository.findByTeamIdOrderByCreatedAtDesc(teamId).stream()
                .map(this::toWarResponse)
                .toList();
    }

    private WarResponse toWarResponse(WAR war) {
        return WarResponse.builder()
                .id(war.getId())
                .userId(war.getUserId())
                .teamId(war.getTeamId())
                .weekNumber(war.getWeekNumber())
                .content(war.getContent())
                .createdAt(war.getCreatedAt())
                .build();
    }
}
