package agsamkin.code.model.setting;

import agsamkin.code.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "settings")
public class Setting {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "repo_sort")
    private RepoSort repoSort;

    @Enumerated(EnumType.STRING)
    @Column(name = "repo_order")
    private RepoOrder repoOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "issue_sort")
    private IssueSort issueSort;

    @Enumerated(EnumType.STRING)
    @Column(name = "issue_order")
    private IssueOrder issueOrder;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
}
