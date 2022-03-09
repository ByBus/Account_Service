package account.presentation;

import account.buiseness.Mapper;
import account.model.dto.SecurityEventDTO;
import account.model.entity.SecurityEventEntity;
import account.repository.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SecurityEventsApiController {
    RepositoryService repository;
    private final Mapper<SecurityEventDTO, SecurityEventEntity> eventMapper;

    @Autowired
    public SecurityEventsApiController(RepositoryService repositoryService,
                                       Mapper<SecurityEventDTO, SecurityEventEntity> eventMapper) {
        this.repository= repositoryService;
        this.eventMapper = eventMapper;
    }

    @GetMapping("/api/security/events")
    public List<SecurityEventDTO> getAllEvents() {
        List<SecurityEventEntity> events = repository.getAllEvents();
        return events.stream()
                .map(eventMapper::mapToDTO)
                .collect(Collectors.toList());
    }
}
