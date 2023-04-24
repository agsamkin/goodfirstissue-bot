package agsamkin.code.controller;

import agsamkin.code.config.SpringConfigForIT;
import agsamkin.code.model.Language;
import agsamkin.code.model.User;
import agsamkin.code.model.setting.IssueOrder;
import agsamkin.code.model.setting.IssueSort;
import agsamkin.code.model.setting.RepoOrder;
import agsamkin.code.model.setting.RepoSort;
import agsamkin.code.model.setting.Setting;
import agsamkin.code.repository.UserRepository;
import agsamkin.code.telegram.handler.CallbackQueryHandler;
import agsamkin.code.telegram.handler.MessageHandler;
import agsamkin.code.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static agsamkin.code.util.TestUtil.DEFAULT_USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureMockMvc
@ActiveProfiles(SpringConfigForIT.TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public final class WebhookControllerTest {
    @Autowired
    private TestUtil testUtils;

    @Autowired
    private MessageHandler messageHandler;

    @Autowired
    private CallbackQueryHandler callbackQueryHandler;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void clear() {
        testUtils.clear();
        testUtils.initLanguages();
    }

    @Test
    public void startUpdateTest() {
        Update update = testUtils.getStartUpdate();
        messageHandler.handleMessage(update.getMessage());

        var expectedUser = update.getMessage().getFrom();
        User user = userRepository.findByUserId(DEFAULT_USER_ID).get();

        assertEquals(expectedUser.getId(), user.getUserId());
        assertEquals(expectedUser.getUserName(), user.getUserName());
    }

    @Test
    public void setAndRemoveMyLangTest() {
        Update startUpdate = testUtils.getStartUpdate();
        messageHandler.handleMessage(startUpdate.getMessage());

        Language expectedLanguage = testUtils.getLanguage();
        assertNotNull(expectedLanguage);

        Update setMyLangUpdate = testUtils.getSetMyLangUpdate(expectedLanguage);
        callbackQueryHandler.handleCallback(setMyLangUpdate.getCallbackQuery());

        var expectedUser = startUpdate.getMessage().getFrom();
        User user = userRepository.findByUserId(expectedUser.getId()).get();

        assertNotEquals(0, user.getLanguages());

        Language language = user.getLanguages().get(0);
        assertEquals(expectedLanguage, language);

        Update removeMyLangUpdate = testUtils.getRemoveMyLangUpdate(language);
        callbackQueryHandler.handleCallback(removeMyLangUpdate.getCallbackQuery());
        user = userRepository.findByUserId(expectedUser.getId()).get();

        List<Language> languages = user.getLanguages();
        assertEquals(0, languages.size());
    }

    @Test
    public void setSortReposTest() {
        Update startUpdate = testUtils.getStartUpdate();
        messageHandler.handleMessage(startUpdate.getMessage());

        RepoSort repoSort = RepoSort.STARS;
        Update setMyLangUpdate = testUtils.getSetSortReposUpdate(repoSort);
        callbackQueryHandler.handleCallback(setMyLangUpdate.getCallbackQuery());

        var expectedUser = startUpdate.getMessage().getFrom();
        User user = userRepository.findByUserId(expectedUser.getId()).get();
        Setting setting = user.getSetting();

        assertEquals(repoSort, setting.getRepoSort());
    }

    @Test
    public void setOrderReposTest() {
        Update startUpdate = testUtils.getStartUpdate();
        messageHandler.handleMessage(startUpdate.getMessage());

        RepoOrder repoOrder = RepoOrder.ASC;
        Update setMyLangUpdate = testUtils.getSetOrderReposUpdate(repoOrder);
        callbackQueryHandler.handleCallback(setMyLangUpdate.getCallbackQuery());

        var expectedUser = startUpdate.getMessage().getFrom();
        User user = userRepository.findByUserId(expectedUser.getId()).get();
        Setting setting = user.getSetting();

        assertEquals(repoOrder, setting.getRepoOrder());
    }

    @Test
    public void setSortIssuesTest() {
        Update startUpdate = testUtils.getStartUpdate();
        messageHandler.handleMessage(startUpdate.getMessage());

        IssueSort issueSort = IssueSort.COMMENTS;
        Update setMyLangUpdate = testUtils.getSetSortIssuesUpdate(issueSort);
        callbackQueryHandler.handleCallback(setMyLangUpdate.getCallbackQuery());

        var expectedUser = startUpdate.getMessage().getFrom();
        User user = userRepository.findByUserId(expectedUser.getId()).get();
        Setting setting = user.getSetting();

        assertEquals(issueSort, setting.getIssueSort());
    }

    @Test
    public void setOrderIssuesTest() {
        Update startUpdate = testUtils.getStartUpdate();
        messageHandler.handleMessage(startUpdate.getMessage());

        IssueOrder issueOrder = IssueOrder.ASC;
        Update setMyLangUpdate = testUtils.getSetOrderIssuesUpdate(issueOrder);
        callbackQueryHandler.handleCallback(setMyLangUpdate.getCallbackQuery());

        var expectedUser = startUpdate.getMessage().getFrom();
        User user = userRepository.findByUserId(expectedUser.getId()).get();
        Setting setting = user.getSetting();

        assertEquals(issueOrder, setting.getIssueOrder());
    }
}
