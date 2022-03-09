package account.buiseness;

import account.model.entity.SecurityEventEntity;
import account.repository.SecurityEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class Logger {
    private final SecurityEventRepository eventRepository;
    private final HttpServletRequest request;

    @Autowired
    public Logger(SecurityEventRepository eventRepository, HttpServletRequest request) {
        this.eventRepository = eventRepository;
        this.request = request;
    }

    public void log(EventType type, String subject, String object, String path) {
        SecurityEventEntity event = new SecurityEventEntity(type, subject, object, path);
        eventRepository.save(event);
    }

    public void log(EventType type, String subject, String object) {
        log(type, subject, object, request.getServletPath());
    }

    public void log(EventType type, String subject) {
        log(type, subject, request.getServletPath(), request.getServletPath());
    }
}
