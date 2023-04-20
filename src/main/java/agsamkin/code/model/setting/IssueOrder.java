package agsamkin.code.model.setting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum IssueOrder {
    ASC("Asc", Sort.Direction.ASC),
    DESC("Desc", Sort.Direction.DESC);

    private final String name;
    private final Sort.Direction direction;
}
