package de.heavenhr.hireme.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import de.heavenhr.hireme.controller.model.ApplicationsCountResponse;
import de.heavenhr.hireme.controller.model.JobApplicationRequest;
import de.heavenhr.hireme.controller.model.JobApplicationResponse;
import de.heavenhr.hireme.controller.model.JobOfferRequest;
import de.heavenhr.hireme.controller.model.JobOfferResponse;
import de.heavenhr.hireme.model.JobApplication;
import de.heavenhr.hireme.model.JobOffer;
import de.heavenhr.hireme.repository.model.JobApplicationEntity;
import de.heavenhr.hireme.repository.model.JobOfferEntity;

public class ModelConversionUtils {

    public static JobApplicationResponse convertToJobApplicationResponse(final JobApplication jobApplication, final JobOffer relatedJobOffer) {
        return new JobApplicationResponse().toBuilder()
                                         .candidateEmail(jobApplication.getCandidateEmail())
                                         .resumeText(jobApplication.getResumeText())
                                         .applicationId(jobApplication.getApplicationId())
                                         .applicationStatus(jobApplication.getApplicationStatus())
                                         .relatedJobOffer(convertToJobOfferResponse(relatedJobOffer))
                                         .build();
    }
    
    public static JobOfferResponse convertToJobOfferResponse(final JobOffer jobOffer) {
        return new JobOfferResponse().toBuilder()
                                     .jobTitle(jobOffer.getJobTitle())
                                     .numberOfApplications(jobOffer.getNumberOfApplications())
                                     .description(jobOffer.getDescription())
                                     .offerId(jobOffer.getOfferId())
                                     .startDate(jobOffer.getStartDate())
                                     .build();
    }
    
    public static ApplicationsCountResponse getApplicationsCountResponse(final JobOffer jobOffer) {
        return new ApplicationsCountResponse().toBuilder()
                                     .numberOfApplications(jobOffer.getNumberOfApplications())
                                     .jobOfferId(jobOffer.getOfferId())
                                     .build();
    }
    
    public static JobOffer convertToJobOffer(final JobOfferRequest jobOfferRequest) {
        return new JobOffer().toBuilder()
                             .jobTitle(jobOfferRequest.getJobTitle())
                             .description(jobOfferRequest.getDescription())
                             .startDate(jobOfferRequest.getStartDate())
                             .build();
    }
    
    public static JobApplication convertToJobApplication(final JobApplicationRequest jobApplicationRequest) {
        return new JobApplication().toBuilder()
                                     .candidateEmail(jobApplicationRequest.getCandidateEmail())
                                     .resumeText(jobApplicationRequest.getResumeText())
                                     .build();
    }
    
    public static JobApplicationEntity convertToJobApplicationEntity(final JobApplication jobApplication) {
        return new JobApplicationEntity().toBuilder()
                                         .applicationId(jobApplication.getApplicationId())
                                         .applicationStatus(jobApplication.getApplicationStatus())
                                         .candidateEmail(jobApplication.getCandidateEmail())
                                         .relatedOfferId(jobApplication.getRelatedOfferId())
                                         .resumeText(jobApplication.getResumeText())
                                         .build();
    }
    
    public static JobApplication convertToJobApplication(final JobApplicationEntity jobApplicationEntity) {
        return new JobApplication().toBuilder()
                                .applicationId(jobApplicationEntity.getApplicationId())
                                .applicationStatus(jobApplicationEntity.getApplicationStatus())
                                .candidateEmail(jobApplicationEntity.getCandidateEmail())
                                .relatedOfferId(jobApplicationEntity.getRelatedOfferId())
                                .resumeText(jobApplicationEntity.getResumeText())
                                .build();
    }
    
    public static JobOfferEntity convertToJobOfferEntity(final JobOffer jobOffer) {
        return new JobOfferEntity().toBuilder()
                                   .jobTitle(jobOffer.getJobTitle())
                                   .numberOfApplications(jobOffer.getNumberOfApplications())
                                   .description(jobOffer.getDescription())
                                   .offerId(jobOffer.getOfferId())
                                   .startDate(jobOffer.getStartDate().getMillis())
                                   .build();
    }
    
    public static JobOffer convertToJobOffer(final JobOfferEntity jobOfferEntity) {
        return new JobOffer().toBuilder()
                             .jobTitle(jobOfferEntity.getJobTitle())
                             .numberOfApplications(jobOfferEntity.getNumberOfApplications())
                             .offerId(jobOfferEntity.getOfferId())
                             .description(jobOfferEntity.getDescription())
                             .startDate(new DateTime(jobOfferEntity.getStartDate(), DateTimeZone.UTC))
                             .build();
    }

}
