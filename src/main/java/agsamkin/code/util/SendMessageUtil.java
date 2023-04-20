package agsamkin.code.util;

import agsamkin.code.model.Issue;
import agsamkin.code.model.Repo;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.text.SimpleDateFormat;
import java.util.List;

import static agsamkin.code.service.SendMessageService.ISSUES_MESSAGE;
import static agsamkin.code.service.SendMessageService.LINE_BREAK;
import static agsamkin.code.service.SendMessageService.REPOS_MESSAGE;

@Component
public class SendMessageUtil {
    public static final String WHITESPACE = " ";
    public static final String MARK_DOWN_INLINE_STALE_LINK_TEMPLATE = "[%s](%s)";

    public static final String STAR_EMOJI = "⭐";
    public static final String FORK_EMOJI = "\uD83D\uDD31";

    public static final String UPDATED_EMOJI = "\uD83D\uDD04";
    public static final String COMMENT_EMOJI = "\uD83D\uDCAC";

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public SendMessage getSimpleMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }

    public String getMarkDownTextForReposMessage(List<Repo> repos) {
        StringBuilder sb = new StringBuilder(REPOS_MESSAGE);
        for (Repo repo : repos) {
            sb.append(LINE_BREAK);
            sb.append(getMarkDownInlineStileLink(repo.getFullName(), repo.getHtmlUrl()));
            sb.append(LINE_BREAK);
            sb.append(STAR_EMOJI).append(WHITESPACE).append(repo.getStargazersCount()).append(" ");
            sb.append(FORK_EMOJI).append(WHITESPACE).append(repo.getForksCount()).append(" ");
            sb.append(UPDATED_EMOJI).append(WHITESPACE).append(formatter.format(repo.getUpdatedAt()));
        }
        return sb.toString();
    }

    public String getMarkDownTextForIssuesMessage(List<Issue> issues) {
        StringBuilder sb = new StringBuilder(ISSUES_MESSAGE);
        for (Issue issue : issues) {
            Repo repo = issue.getRepo();
            sb.append(LINE_BREAK);
            sb.append(getMarkDownInlineStileLink(
                    issue.getRepo().getFullName() + "/issues/" + issue.getNumber()
                    , issue.getHtmlUrl()));
            sb.append(LINE_BREAK);
//            sb.append(issue.getTitle());
//            sb.append(LINE_BREAK);
            sb.append(COMMENT_EMOJI).append(WHITESPACE).append(issue.getCommentsCount()).append(" ");
            sb.append(UPDATED_EMOJI).append(WHITESPACE).append(formatter.format(issue.getUpdatedAt()));
        }
        return sb.toString();
    }

    public String getMarkDownInlineStileLink(String name, String url) {
        return MARK_DOWN_INLINE_STALE_LINK_TEMPLATE.formatted(name, url);
    }

}
