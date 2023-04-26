package agsamkin.code.util;

import agsamkin.code.model.Language;
import agsamkin.code.model.setting.IssueOrder;
import agsamkin.code.model.setting.IssueSort;
import agsamkin.code.model.setting.RepoOrder;
import agsamkin.code.model.setting.RepoSort;
import agsamkin.code.repository.LanguageRepository;
import agsamkin.code.repository.UserRepository;
import agsamkin.code.telegram.BotCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Set;

@Component
public class TestUtil {
    public static final int DEFAULT_UPDATE_ID = 0;
    public static final int DEFAULT_MESSAGE_ID = 0;
    public static final String DEFAULT_CALLBACK_QUERY_ID = "";

    public static final long DEFAULT_USER_ID = 0L;
    public static final long DEFAULT_CHAT_ID = 0L;

    public static final String DEFAULT_LANG = "Java";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private MockMvc mockMvc;

    public User getFrom() {
        User from = new User();
        from.setId(DEFAULT_USER_ID);
        from.setUserName("UserName");
        from.setFirstName("FirstName");
        from.setLastName("LastName");
        from.setLanguageCode("LanguageCode");
        return from;
    }

    public Chat getChat() {
        Chat chat = new Chat();
        chat.setId(DEFAULT_CHAT_ID);
        return chat;
    }

    public Message getMessage(String text) {
        Message message = new Message();
        message.setMessageId(DEFAULT_MESSAGE_ID);
        message.setText(text);
        message.setChat(getChat());
        message.setFrom(getFrom());

        return message;
    }

    public void initLanguages() {
        Language language = Language.builder()
                .name(DEFAULT_LANG)
                .enable(true).build();
        languageRepository.save(language);
    }

    public Language getLanguage() {
        Set<Language> languages = languageRepository.findByEnable(true);
        return languages.stream().findFirst().get();
    }

    public Update getStartUpdate() {
        Update update = new Update();
        update.setUpdateId(DEFAULT_UPDATE_ID);
        update.setMessage(getMessage(BotCommand.START.getName()));
        return update;
    }

    public Update getSetMyLangUpdate(Language language) {
        Update update = new Update();
        update.setUpdateId(DEFAULT_UPDATE_ID);

        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId(DEFAULT_CALLBACK_QUERY_ID);
        String callbackData = ButtonsUtil.SET_LANGUAGE_ACTION
                + ButtonsUtil.ACTION_SEPARATOR
                + language.getName();

        callbackQuery.setData(callbackData);
        callbackQuery.setFrom(getFrom());
        callbackQuery.setMessage(getMessage(SendMessageUtil.SETUP_MY_LANGUAGES_MESSAGE));

        update.setCallbackQuery(callbackQuery);
        return update;
    }

    public Update getRemoveMyLangUpdate(Language language) {
        Update update = new Update();
        update.setUpdateId(DEFAULT_UPDATE_ID);

        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId(DEFAULT_CALLBACK_QUERY_ID);
        String callbackData = ButtonsUtil.REMOVE_LANGUAGE_ACTION
                + ButtonsUtil.ACTION_SEPARATOR
                + language.getName();

        callbackQuery.setData(callbackData);
        callbackQuery.setFrom(getFrom());
        callbackQuery.setMessage(getMessage(SendMessageUtil.SETUP_MY_LANGUAGES_MESSAGE));

        update.setCallbackQuery(callbackQuery);
        return update;
    }


    public Update getSetSortReposUpdate(RepoSort repoSort) {
        Update update = new Update();
        update.setUpdateId(DEFAULT_UPDATE_ID);

        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId(DEFAULT_CALLBACK_QUERY_ID);
        String callbackData = ButtonsUtil.REPO_SORT_OPTIONS_ACTION
                + ButtonsUtil.ACTION_SEPARATOR
                + repoSort;

        callbackQuery.setData(callbackData);
        callbackQuery.setFrom(getFrom());
        callbackQuery.setMessage(getMessage(SendMessageUtil.REPO_SORT_COMMAND_TEXT));

        update.setCallbackQuery(callbackQuery);
        return update;
    }

    public Update getSetOrderReposUpdate(RepoOrder repoOrder) {
        Update update = new Update();
        update.setUpdateId(DEFAULT_UPDATE_ID);

        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId(DEFAULT_CALLBACK_QUERY_ID);
        String callbackData = ButtonsUtil.REPO_ORDER_OPTIONS_ACTION
                + ButtonsUtil.ACTION_SEPARATOR
                + repoOrder;

        callbackQuery.setData(callbackData);
        callbackQuery.setFrom(getFrom());
        callbackQuery.setMessage(getMessage(SendMessageUtil.REPO_ORDER_COMMAND_TEXT));

        update.setCallbackQuery(callbackQuery);
        return update;
    }


    public Update getSetSortIssuesUpdate(IssueSort issueSort) {
        Update update = new Update();
        update.setUpdateId(DEFAULT_UPDATE_ID);

        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId(DEFAULT_CALLBACK_QUERY_ID);
        String callbackData = ButtonsUtil.ISSUE_SORT_OPTIONS_ACTION
                + ButtonsUtil.ACTION_SEPARATOR
                + issueSort;

        callbackQuery.setData(callbackData);
        callbackQuery.setFrom(getFrom());
        callbackQuery.setMessage(getMessage(SendMessageUtil.ISSUE_SORT_COMMAND_TEXT));

        update.setCallbackQuery(callbackQuery);
        return update;
    }


    public Update getSetOrderIssuesUpdate(IssueOrder issueOrder) {
        Update update = new Update();
        update.setUpdateId(DEFAULT_UPDATE_ID);

        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setId(DEFAULT_CALLBACK_QUERY_ID);
        String callbackData = ButtonsUtil.ISSUE_ORDER_OPTIONS_ACTION
                + ButtonsUtil.ACTION_SEPARATOR
                + issueOrder;

        callbackQuery.setData(callbackData);
        callbackQuery.setFrom(getFrom());
        callbackQuery.setMessage(getMessage(SendMessageUtil.ISSUE_ORDER_COMMAND_TEXT));

        update.setCallbackQuery(callbackQuery);
        return update;
    }

    public ResultActions perform(final MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }

    public void clear() {
        userRepository.deleteAll();
        languageRepository.deleteAll();
    }

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    public static String asJson(final Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }

    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }
}
