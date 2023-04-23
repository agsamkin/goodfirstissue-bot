package agsamkin.code.util;

import agsamkin.code.model.Issue;
import agsamkin.code.model.Repo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class SendMessageUtil {
    public static final String LINE_BREAK = "\n";
    public static final String WHITESPACE = " ";
    public static final String DASH = " - ";

    public static final String SETTING_EMOJI = "⚙";
    public static final String STAR_EMOJI = "⭐";
    public static final String FORK_EMOJI = "\uD83D\uDD31";
    public static final String UPDATED_EMOJI = "\uD83D\uDD04";
    public static final String COMMENT_EMOJI = "\uD83D\uDCAC";

    public static final String GREETING_MESSAGE =
            "I can help you in finding some good first issues and in contributing to the open source community."
                    + StringUtils.repeat(LINE_BREAK, 2)
                    + "You can manage me by sending these commands:"
                    + StringUtils.repeat(LINE_BREAK, 2);

    public static final String SETTINGS_MESSAGE = SETTING_EMOJI + " Settings";
    public static final String SETUP_MY_LANGUAGES_MESSAGE = "Set up languages from the list below";
    public static final String REPOS_MESSAGE = "Repositories with good-first-issue label:";
    public static final String ISSUES_MESSAGE = "Issues with good-first-issue label:";

    public static final String REPO_SORT_COMMAND_TEXT = "Sort repositories:";
    public static final String REPO_ORDER_COMMAND_TEXT  = "Sort order of the repositories:";
    public static final String ISSUE_SORT_COMMAND_TEXT  = "Sort issues:";
    public static final String ISSUE_ORDER_COMMAND_TEXT  = "Sort order of the issues:";
    public static final String UNSUPPORTED_COMMAND_TEXT = "Unsupported command";

    public static final String MARK_DOWN_INLINE_STALE_LINK_TEMPLATE = "[%s](%s)";

    public static final String ISSUES_PATH_OF_URL = "/issues/";

    public static final int REPOS_PAGE_DEFAULT = 0;
    public static final int REPOS_PAGE_SIZE_DEFAULT = 10;

    public static final int ISSUES_PAGE_DEFAULT = 0;
    public static final int ISSUES_PAGE_SIZE_DEFAULT = 10;

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public SendMessage getSimpleMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }

    public EditMessageText getEditMessageText(
            Message message, String text, InlineKeyboardMarkup replyMarkup) {
        return EditMessageText.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .text(text)
                .disableWebPagePreview(true)
                .parseMode(ParseMode.MARKDOWN)
                .replyMarkup(replyMarkup).build();
    }

    public EditMessageReplyMarkup getEditMessageReplyMarkup(
            Message message, InlineKeyboardMarkup replyMarkup) {
        return EditMessageReplyMarkup.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .replyMarkup(replyMarkup).build();
    }

    public String getMarkDownTextForReposMessage(List<Repo> repos) {
        StringBuilder sb = new StringBuilder(REPOS_MESSAGE);
        for (Repo repo : repos) {
            sb.append(LINE_BREAK);
            sb.append(getMarkDownInlineStileLink(repo.getFullName(), repo.getHtmlUrl()));
            sb.append(LINE_BREAK);
            sb.append(STAR_EMOJI).append(WHITESPACE).append(repo.getStargazersCount()).append(WHITESPACE);
            sb.append(FORK_EMOJI).append(WHITESPACE).append(repo.getForksCount()).append(WHITESPACE);
            sb.append(UPDATED_EMOJI).append(WHITESPACE).append(formatter.format(repo.getUpdatedAt()));
        }
        return sb.toString();
    }

    public String getMarkDownTextForIssuesMessage(List<Issue> issues) {
        StringBuilder sb = new StringBuilder(ISSUES_MESSAGE);
        for (Issue issue : issues) {
            sb.append(LINE_BREAK);
            sb.append(getMarkDownInlineStileLink(
                    issue.getRepo().getFullName() + ISSUES_PATH_OF_URL + issue.getNumber(),
                    issue.getHtmlUrl()));
            sb.append(LINE_BREAK);
            sb.append(COMMENT_EMOJI).append(WHITESPACE).append(issue.getCommentsCount()).append(WHITESPACE);
            sb.append(UPDATED_EMOJI).append(WHITESPACE).append(formatter.format(issue.getUpdatedAt()));
        }
        return sb.toString();
    }

    public String getMarkDownInlineStileLink(String name, String url) {
        return MARK_DOWN_INLINE_STALE_LINK_TEMPLATE.formatted(name, url);
    }
}
