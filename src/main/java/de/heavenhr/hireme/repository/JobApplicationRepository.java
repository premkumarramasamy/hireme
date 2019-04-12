package de.heavenhr.hireme.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heavenhr.hireme.repository.model.JobApplicationEntity;

@Repository
public interface JobApplicationRepository extends CrudRepository<JobApplicationEntity, UUID> {
    
    public List<JobApplicationEntity> findByCandidateEmail(final String candidateEmail);
    
    public List<JobApplicationEntity> findByRelatedOfferId(final UUID relatedOfferId);
}
