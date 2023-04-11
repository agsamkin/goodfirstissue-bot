package agsamkin.code.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "languages")
public class Language implements Comparable<Language> {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @EqualsAndHashCode.Include
    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "enable")
    private Boolean enable;

    @ManyToMany(mappedBy = "languages")
    private List<User> users;

    @Override
    public int compareTo(Language other) {
        return StringUtils.compare(this.getName(), other.getName());
    }
}
