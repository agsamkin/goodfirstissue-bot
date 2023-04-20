package agsamkin.code.model.job;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum JobType {
    UPLOAD_REPOS("Upload repos", "Upload new repos and issues")
    , UPDATE_REPOS("Update repos", "Update existing repos and issues")
    , DELETE_REPOS("Delete repos", "Delete old repos and issues");

    private final String name;
    private final String description;
}
