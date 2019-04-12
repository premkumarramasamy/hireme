package de.heavenhr.hireme.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Preconditions;

import de.heavenhr.hireme.exception.ResourceAlreadyExistsException;
import de.heavenhr.hireme.exception.ResourceNotFoundException;
import de.heavenhr.hireme.model.ApplicationStatus;
import de.heavenhr.hireme.model.JobApplication;
import de.heavenhr.hireme.model.JobOffer;
import de.heavenhr.hireme.repository.JobApplicationRepository;
import de.heavenhr.hireme.repository.model.JobApplicationEntity;
import de.heavenhr.hireme.utils.ModelConversionUtils;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;
    
    @Autowired
    private JobOfferService jobOfferService;
    
    public List<JobApplication> getJobApplicationsByJobOfferId(final UUID jobOfferId) {
        Preconditions.checkNotNull(jobOfferId, "JobOfferId cannot be null");
        List<JobApplicationEntity> JobApplicationEntityList = jobApplicationRepository.findByRelatedOfferId(jobOfferId);
        return JobApplicationEntityList.stream()
                                       .map(ModelConversionUtils::convertToJobApplication)
                                       .collect(Collectors.toList());
    }
    
    public JobApplication getJobApplication(final UUID applicationId) throws ResourceNotFoundException {
        Preconditions.checkNotNull(applicationId, "applicationId cannot be null");
        Optional<JobApplicationEntity> jobApplicationEntity = jobApplicationRepository.findById(applicationId);
        if (!jobApplicationEntity.isPresent()) {
            throw new ResourceNotFoundException(applicationId, "JobApplication");
        }
        return ModelConversionUtils.convertToJobApplication(jobApplicationEntity.get());
    }
    
    public JobApplication createJobApplication(final JobApplication jobApplication) throws ResourceAlreadyExistsException, ResourceNotFoundException {
        Preconditions.checkNotNull(jobApplication, "jobApplication cannot be null");
        List<JobApplicationEntity> findByCandidateEmail = jobApplicationRepository.findByCandidateEmail(jobApplication.getCandidateEmail());
        List<JobApplicationEntity> existingRecords = findByCandidateEmail.stream()
                            .filter(eachJobApplication -> jobApplication.getRelatedOfferId().equals(eachJobApplication.getRelatedOfferId()))
                            .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(existingRecords)) {
            throw new ResourceAlreadyExistsException("Job Application for this user already exists for the job offer");
        }
        JobApplication newJobApplication = jobApplication.toBuilder()
                                                        .applicationId(UUID.randomUUID())
                                                        .applicationStatus(ApplicationStatus.APPLIED)
                                                        .build();
        
        jobApplicationRepository.save(ModelConversionUtils.convertToJobApplicationEntity(newJobApplication));
        /*
         * Update Job offer service once after successfully saving to job application entity.
         */
        JobOffer jobOffer = jobOfferService.getJobOffer(newJobApplication.getRelatedOfferId());
        jobOfferService.updateJobApplicationCount(jobOffer.getOfferId(), jobOffer.getNumberOfApplications() + 1);
        return newJobApplication;
    }
    
    public JobApplication patchJobApplication(final JobApplication jobApplication) throws ResourceNotFoundException {
        Preconditions.checkNotNull(jobApplication, "jobApplication cannot be null");
        final JobApplication currentJobApplication = getJobApplication(jobApplication.getApplicationId());
        
        final JobApplication updatedEntity = new JobApplication().toBuilder()
                         .applicationId(jobApplication.getApplicationId())
                         .applicationStatus(null != jobApplication.getApplicationStatus()? jobApplication.getApplicationStatus() : currentJobApplication.getApplicationStatus())
                         .candidateEmail(null != jobApplication.getCandidateEmail()? jobApplication.getCandidateEmail() : currentJobApplication.getCandidateEmail())
                         .relatedOfferId(null != jobApplication.getRelatedOfferId()? jobApplication.getRelatedOfferId() : currentJobApplication.getRelatedOfferId())
                         .resumeText(null != jobApplication.getResumeText()? jobApplication.getResumeText() : currentJobApplication.getResumeText())
                         .build();
        jobApplicationRepository.save(ModelConversionUtils.convertToJobApplicationEntity(jobApplication));
        return updatedEntity;
    }
    
    public void deleteJobApplication(final UUID applicationId) {
        jobApplicationRepository.deleteById(applicationId);
    }
}
