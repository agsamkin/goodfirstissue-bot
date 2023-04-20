package agsamkin.code.model.setting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IssueSort {
    COMMENTS("Comments", "commentsCount"),
    UPDATED("Updated", "updatedAt");

    private final String name;
    private final String sortProperty;
}
