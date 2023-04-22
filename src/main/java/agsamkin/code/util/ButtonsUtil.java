package agsamkin.code.util;

import org.springframework.stereotype.Component;

@Component
public class ButtonsUtil {
    public static final int LANGUAGES_PER_ROW = 3;

    public static final String ACTION_SEPARATOR = ":";

    public static final String SET_LANGUAGE_ACTION = "set_lang";
    public static final String REMOVE_LANGUAGE_ACTION = "remove_lang";
    public static final String MARK_EMOJI = "\uD83D\uDCCC";

    public static final String NEXT_PAGE_TEXT_TEMPLATE = "Next (%s - %s) >>";
    public static final String PREV_PAGE_TEXT_TEMPLATE = "<< Prev (%s - %s)";

    public static final String REPO_NEXT_PAGE_ACTION = "repo_next_page";
    public static final String REPO_PREV_PAGE_ACTION = "repo_prev_page";

    public static final String REPO_SORT_TEXT = "Sort repositories";
    public static final String REPO_SORT_ACTION = "repo_sort";
    public static final String REPO_SORT_OPTIONS_ACTION = "repo_sort_opt";

    public static final String REPO_ORDER_TEXT = "Sort order repositories";
    public static final String REPO_ORDER_ACTION = "repo_order";
    public static final String REPO_ORDER_OPTIONS_ACTION = "repo_order_opt";

    public static final String ISSUE_NEXT_PAGE_ACTION = "issue_next_page";
    public static final String ISSUE_PREV_PAGE_ACTION = "issue_prev_page";

    public static final String ISSUE_SORT_TEXT = "Sort issues";
    public static final String ISSUE_SORT_ACTION = "issue_sort";
    public static final String ISSUE_SORT_OPTIONS_ACTION = "issue_sort_opt";

    public static final String ISSUE_ORDER_TEXT = "Sort order issues";
    public static final String ISSUE_ORDER_ACTION = "issue_order";
    public static final String ISSUE_ORDER_OPTIONS_ACTION = "issue_order_opt";

    public static final String SETTING_ACTION = "settings";

    public static final String SETTING_BACK_TEXT = "<< Back to settings";
    public static final String SETTING_BACK_ACTION = "back";

    public int getPreviousPage(int page) {
        return (page - 1);
    }

    public int getPreviousSizePageFrom(int page, int size) {
        return getPreviousPage(page) * size + 1;
    }

    public int getPreviousSizePageTo(int page, int size) {
        return (getPreviousPage(page) + 1) * size;
    }

    public int getNextPage(int page) {
        return (page + 1);
    }

    public int getNextSizePageFrom(int page, int size) {
        return getNextPage(page) * size + 1;
    }

    public int getNextSizePageTo(int page, int size) {
        return (getNextPage(page) + 1) * size;
    }
}
