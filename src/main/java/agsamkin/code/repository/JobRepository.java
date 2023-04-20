package agsamkin.code.repository;

import agsamkin.code.model.Language;
import agsamkin.code.model.job.Job;
import agsamkin.code.model.job.JobType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findByJobTypeAndLanguage(JobType jobType, Language language);
    List<Job> findByJobType(JobType jobType);
}
