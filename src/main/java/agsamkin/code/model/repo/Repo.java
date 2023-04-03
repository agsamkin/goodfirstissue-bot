package agsamkin.code.model.repo;

import agsamkin.code.model.Issue;
import agsamkin.code.model.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import java.util.Date;
import java.util.List;

import static javax.persistence.TemporalType.TIMESTAMP;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "repos")
public class Repo {
    @Id
    @Column(name = "repo_id")
    private Long repoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private Visibility visibility;

    @Column(name = "archived")
    private Boolean archived;

    @Column(name = "is_template")
    private Boolean isTemplate;

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

    @OneToMany(mappedBy = "repo", cascade = CascadeType.ALL)
    private List<Issue> issues;
}
