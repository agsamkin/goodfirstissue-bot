package agsamkin.code.controller;

import agsamkin.code.telegram.handler.UpdateHandler;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequiredArgsConstructor
public class WebhookController {
    public static final String WEBHOOK_CONTROLLER_PATH = "/";

    private final UpdateHandler updateHandler;

    @PostMapping(path = WEBHOOK_CONTROLLER_PATH)
    public ResponseEntity<?> onUpdateReceived(@RequestBody Update update) {
        updateHandler.handleUpdate(update);
        return ResponseEntity.ok().build();
    }
}
