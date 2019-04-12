package de.heavenhr.hireme.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import de.heavenhr.hireme.exception.ResourceAlreadyExistsException;
import de.heavenhr.hireme.exception.ResourceNotFoundException;
import de.heavenhr.hireme.model.JobOffer;
import de.heavenhr.hireme.repository.JobOfferRepository;
import de.heavenhr.hireme.repository.model.JobOfferEntity;
import de.heavenhr.hireme.utils.ModelConversionUtils;

@Service
public class JobOfferService {

    @Autowired
    private JobOfferRepository jobOfferRepository;
    
    public JobOffer getJobOffer(final UUID offerId) throws ResourceNotFoundException {
        Preconditions.checkNotNull(offerId, "offerId cannot be null");
        Optional<JobOfferEntity> jobOfferEntity = jobOfferRepository.findById(offerId);
        if (!jobOfferEntity.isPresent()) {
            throw new ResourceNotFoundException(offerId, "Job Offer");
        }
        return ModelConversionUtils.convertToJobOffer(jobOfferEntity.get());
    }
    
    public JobOffer getJobOffer(final String jobTitle) throws ResourceNotFoundException {
        Preconditions.checkNotNull(jobTitle, "jobTitle cannot be null");
        Optional<JobOfferEntity> jobOfferEntity = jobOfferRepository.findByJobTitle(jobTitle);
        if (!jobOfferEntity.isPresent()) {
            throw new ResourceNotFoundException("Job Offer with this title not found");
        }
        return ModelConversionUtils.convertToJobOffer(jobOfferEntity.get());
    }
    
    public List<JobOffer> getAllJobOffer() {
        List<JobOffer> result = new ArrayList<JobOffer>();
        jobOfferRepository.findAll()
                          .forEach(eachEntity -> {
                              result.add(ModelConversionUtils.convertToJobOffer(eachEntity));
                          });
        return result;
    }
    
    public JobOffer updateJobOffer(final JobOffer jobOffer) throws ResourceNotFoundException {
        Preconditions.checkNotNull(jobOffer, "jobOffer cannot be null");
        Optional<JobOfferEntity> jobOfferEntity = jobOfferRepository.findById(jobOffer.getOfferId());
        if (!jobOfferEntity.isPresent()) {
            throw new ResourceNotFoundException(jobOffer.getOfferId(), "Job Offer");
        }
        JobOfferEntity newEntity = jobOfferEntity.get().toBuilder()
                                                       .description(jobOffer.getDescription())
                                                       .jobTitle(jobOffer.getJobTitle())
                                                       .startDate(jobOffer.getStartDate().getMillis())
                                                       .build();
        JobOfferEntity savedObject = jobOfferRepository.save(newEntity);
        return ModelConversionUtils.convertToJobOffer(savedObject);
    }
    
    public JobOffer updateJobApplicationCount(final UUID jobOfferId, int newValue) throws ResourceNotFoundException {
        Preconditions.checkNotNull(jobOfferId, "jobOfferId cannot be null");
        Optional<JobOfferEntity> jobOfferEntity = jobOfferRepository.findById(jobOfferId);
        if (!jobOfferEntity.isPresent()) {
            throw new ResourceNotFoundException(jobOfferId, "Job Offer");
        }
        JobOfferEntity newEntity = jobOfferEntity.get().toBuilder()
                                                       .numberOfApplications(newValue)
                                                       .build();
        JobOfferEntity savedObject = jobOfferRepository.save(newEntity);
        return ModelConversionUtils.convertToJobOffer(savedObject);
    }
    
    public JobOffer createJobOffer(final JobOffer jobOffer) throws ResourceAlreadyExistsException {
        Preconditions.checkNotNull(jobOffer, "jobOffer cannot be null");
        Optional<JobOfferEntity> jobOfferEntity = jobOfferRepository.findByJobTitle(jobOffer.getJobTitle());
        if (jobOfferEntity.isPresent()) {
            throw new ResourceAlreadyExistsException("Job Offer for with same title already exists");
        }
        JobOfferEntity newEntity = ModelConversionUtils.convertToJobOfferEntity(jobOffer.toBuilder()
                                                          .offerId(UUID.randomUUID())
                                                          .build());
        jobOfferRepository.save(newEntity);
        return ModelConversionUtils.convertToJobOffer(newEntity);
    }

    public void deleteJobOffer(final UUID jobOfferId) { 
        Preconditions.checkNotNull(jobOfferId, "jobOfferId cannot be null");
        jobOfferRepository.deleteById(jobOfferId);      
    }
}
