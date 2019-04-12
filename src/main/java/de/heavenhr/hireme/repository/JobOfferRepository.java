package de.heavenhr.hireme.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.heavenhr.hireme.repository.model.JobOfferEntity;

@Repository
public interface JobOfferRepository extends CrudRepository<JobOfferEntity, UUID> {

    public Optional<JobOfferEntity> findByJobTitle(final String jobTitle);
    
}
