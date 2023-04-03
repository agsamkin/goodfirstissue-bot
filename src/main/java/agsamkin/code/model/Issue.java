package agsamkin.code.model;

import agsamkin.code.model.repo.Repo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "issues")
public class Issue {
    @Id
    @Column(name = "issue_id")
    private Long issueId;

    @Column(name = "number")
    private Integer number;

    @Column(name = "title")
    private String title;

    @Column(name = "html_url")
    private String htmlUrl;

    @Column(name = "url")
    private String url;

    @Column(name = "locked")
    private Boolean locked;

    @Column(name = "comments_count")
    private Integer commentsCount;

    @Temporal(TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Temporal(TIMESTAMP)
    @Column(name = "closed_at")
    private Date closedAt;

    @ManyToOne
    @JoinColumn(name = "repo_id", referencedColumnName = "repo_id")
    private Repo repo;
}
