package agsamkin.code.service.impl;

import agsamkin.code.model.Language;
import agsamkin.code.model.job.Job;
import agsamkin.code.model.job.JobType;
import agsamkin.code.repository.JobRepository;
import agsamkin.code.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class JobServiceImpl implements JobService {
    private final JobRepository jobServiceRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<Job> getJobByJobTypeAndLanguage(JobType jobType, Language language) {
        return jobServiceRepository.findByJobTypeAndLanguage(jobType, language);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Job> getAllJobsByJobType(JobType jobType) {
        return jobServiceRepository.findByJobType(jobType);
    }

    @Override
    public Job updateJob(Job job) {
        return jobServiceRepository.save(
                getJobByJobTypeAndLanguage(job.getJobType(), job.getLanguage())
                        .map(existingJob -> {
                            existingJob.setCompletedAt(job.getCompletedAt());
                            return existingJob;
                        })
                        .orElse(job));
    }

    @Override
    public List<Job> resetAllJobsByScheduleType(JobType jobType) {
        List<Job> existingJobs = getAllJobsByJobType(jobType);
        for (Job existingJob : existingJobs) {
            existingJob.setCompletedAt(null);
        }
        return existingJobs;
    }
}
