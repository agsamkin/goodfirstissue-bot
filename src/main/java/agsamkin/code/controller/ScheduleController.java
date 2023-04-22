package agsamkin.code.controller;

import agsamkin.code.service.ScheduleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ScheduleController {
    private static final int _1_MIN = 60000;
    private static final int _1_HOUR = 3600000;

    private final ScheduleService scheduleService;

    @Scheduled(initialDelay = _1_MIN * 5, fixedDelay = _1_HOUR * 4)
    public void uploadRepos() {
        scheduleService.uploadRepos();
    }

    @Scheduled(initialDelay = _1_HOUR, fixedDelay = _1_HOUR * 2)
    public void updateRepos() {
        scheduleService.updateRepos();
    }

    @Scheduled(initialDelay = _1_HOUR * 12, fixedDelay = _1_HOUR * 12)
    public void deleteRepos() {
        scheduleService.deleteRepos();
    }
}
