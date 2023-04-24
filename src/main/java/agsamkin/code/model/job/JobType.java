package agsamkin.code.model.job;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum JobType {
    UPLOAD_REPOS("Upload repos", "Upload new repositories and issues"),
    UPDATE_REPOS("Update repos", "Update uploaded repositories and issues"),
    DELETE_REPOS("Delete repos", "Delete outdated repositories and issues");

    private final String name;
    private final String description;
}
