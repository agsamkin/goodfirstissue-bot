package agsamkin.code.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.TemporalType.TIMESTAMP;

//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "repos")
public class Repo {
//    @EqualsAndHashCode.Include
    @Id
    @Column(name = "repo_id")
    private Long repoId;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "is_archived")
    private Boolean isArchived;

    @Column(name = "is_template")
    private Boolean isTemplate;

    @Column(name = "is_disabled")
    private Boolean isDisabled;

    @Column(name = "name")
    private String name;

    @Column(name = "full_name")
    private String fullName;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "html_url")
    private String htmlUrl;

    @Column(name = "url")
    private String url;

    @Column(name = "forks_count")
    private Integer forksCount;

    @Column(name = "stargazers_count")
    private Integer stargazersCount;

    @Column(name = "watchers_count")
    private Integer watchersCount;

    @Temporal(TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Temporal(TIMESTAMP)
    @Column(name = "pushed_at")
    private Date pushedAt;

    @OneToOne
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;

    @Builder.Default
    @OneToMany(cascade = ALL, fetch = EAGER, orphanRemoval = true)
    @JoinTable(name = "repo_issue_mapping",
            joinColumns = {@JoinColumn(name = "repo_id", referencedColumnName = "repo_id")},
            inverseJoinColumns = {@JoinColumn(name = "issue_id", referencedColumnName = "issue_id")}
    )
    @MapKey(name = "issueId")
    private Map<Long, Issue> issues = new HashMap();
}
