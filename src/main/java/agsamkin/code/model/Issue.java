package agsamkin.code.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "issues")
public class Issue {
//    @EqualsAndHashCode.Include
    @Id
    @Column(name = "issue_id", unique = true)
    private Long issueId;

    @Column(name = "number")
    private Integer number;

    @Column(name = "title")
    private String title;

    @Column(name = "html_url")
    private String htmlUrl;

    @Column(name = "url")
    private String url;

    @Column(name = "is_locked")
    private Boolean isLocked;

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
}
