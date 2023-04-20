package agsamkin.code.service;

import agsamkin.code.model.Language;
import agsamkin.code.model.job.Job;
import agsamkin.code.model.job.JobType;

import java.util.List;
import java.util.Optional;

public interface JobService {
    Optional<Job> getJobByJobTypeAndLanguage(JobType jobType, Language language);
    List<Job> getAllJobsByJobType(JobType jobType);

    Job updateJob(Job job);
    List<Job> resetAllJobsByScheduleType(JobType jobType);
}
