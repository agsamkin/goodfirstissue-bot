package agsamkin.code.controller;

import agsamkin.code.service.ScheduleService;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ScheduleController {
    private static final int ONE_MIN = 60000;
    private static final int ONE_HOUR = 3600000;

    private final ScheduleService scheduleService;

    @EventListener(ApplicationReadyEvent.class)
    public void initBot() {
        scheduleService.initBot();
    }

    @Scheduled(initialDelay = ONE_MIN * 5, fixedDelay = ONE_HOUR * 4)
    public void uploadRepos() {
        scheduleService.uploadRepos();
    }

    @Scheduled(initialDelay = ONE_HOUR, fixedDelay = ONE_HOUR * 2)
    public void updateRepos() {
        scheduleService.updateRepos();
    }

    @Scheduled(initialDelay = ONE_HOUR * 12, fixedDelay = ONE_HOUR * 12)
    public void deleteRepos() {
        scheduleService.deleteRepos();
    }
}
