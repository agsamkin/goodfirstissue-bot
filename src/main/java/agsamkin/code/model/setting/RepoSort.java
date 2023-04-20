package agsamkin.code.model.setting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RepoSort {
    STARS("Stars", "stargazersCount"),
    FORKS("Forks", "forksCount"),
    UPDATED("Updated", "updatedAt");

    private final String name;
    private final String sortProperty;
}
