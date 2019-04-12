package de.heavenhr.hireme.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.heavenhr.hireme.controller.model.ApplicationsCountResponse;
import de.heavenhr.hireme.controller.model.JobApplicationRequest;
import de.heavenhr.hireme.controller.model.JobApplicationResponse;
import de.heavenhr.hireme.controller.model.JobOfferRequest;
import de.heavenhr.hireme.controller.model.JobOfferResponse;
import de.heavenhr.hireme.model.JobApplication;
import de.heavenhr.hireme.model.JobOffer;
import de.heavenhr.hireme.service.JobApplicationService;
import de.heavenhr.hireme.service.JobOfferService;
import de.heavenhr.hireme.utils.ModelConversionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/joboffer")
@Api(value = "/joboffer", description = "All operations about joboffers and applying to a specific offer")
public class JobOfferController {
    
    @Autowired
    private JobOfferService jobOfferService;
    
    @Autowired
    private JobApplicationService jobApplicationService;
    

    @ApiOperation(value = "Get a list of available job offers", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved list of job offers"),
        @ApiResponse(code = 404, message = "The job offer you were trying to reach is not found")
    })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<JobOfferResponse>> getJobOffer() {
        List<JobOffer> allJobOffer = jobOfferService.getAllJobOffer();
        return ResponseEntity.ok(allJobOffer.stream()
                                            .map(ModelConversionUtils::convertToJobOfferResponse)
                                            .collect(Collectors.toList()));
    }

    @ApiOperation(value = "Get a job offer by job offer id", response = JobOfferResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved a job offer"),
        @ApiResponse(code = 404, message = "The job offer you were trying to reach is not found")
    })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/{jobOfferId}", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JobOfferResponse> getJobOffer(@ApiParam(value = "Job Offer id from which Job Offer will be retrieved", required = true)
                                                        @PathVariable(value = "jobOfferId") final UUID jobOfferId) {
        JobOffer jobOffer = jobOfferService.getJobOffer(jobOfferId);
        return ResponseEntity.ok(ModelConversionUtils.convertToJobOfferResponse(jobOffer));
    }

    @ApiOperation(value = "Create a new job offer", response = JobOfferResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully created a job offer"),
        @ApiResponse(code = 406, message = "The job offer you were trying to create is already existing")
    })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @RequestMapping(method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JobOfferResponse> createJobOffer(@ApiParam(value = "Job Offer Request for which Job Offer will be created", required = true)
                                                           @RequestBody @Valid final JobOfferRequest jobOfferRequest) {
        JobOffer createdJobOffer = jobOfferService.createJobOffer(ModelConversionUtils.convertToJobOffer(jobOfferRequest));
        return ResponseEntity.ok(ModelConversionUtils.convertToJobOfferResponse(createdJobOffer));
    }
    
    @ApiOperation(value = "Update an exisiting job offer", response = JobOfferResponse.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully updated a job offer"),
        @ApiResponse(code = 404, message = "The job offer you were trying to update is not existing")
    })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/{jobOfferId}", method = {RequestMethod.PUT}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JobOfferResponse> updateJobOffer(@ApiParam(value = "Job Offer id from which Job Offer will be updated", required = true)
                                                           @PathVariable(value = "jobOfferId") final UUID jobOfferId, 
                                                           @ApiParam(value = "Job Offer Request for which Job Offer will be updated", required = true)
                                                           @RequestBody @Valid final JobOfferRequest jobOfferRequest) {
        JobOffer updatedJobOffer = jobOfferService.updateJobOffer(ModelConversionUtils.convertToJobOffer(jobOfferRequest).toBuilder().offerId(jobOfferId).build());
        return ResponseEntity.ok(ModelConversionUtils.convertToJobOfferResponse(updatedJobOffer));
        
    }
    
    @ApiOperation(value = "delete an exisiting job offer")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully deleted a job offer"),
        @ApiResponse(code = 404, message = "The job offer you were trying to delete is not existing")
    })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{jobOfferId}", method = {RequestMethod.DELETE})
    public ResponseEntity<Void> deleteJobOffer(@ApiParam(value = "Job Offer id from which Job Offer will be deleted", required = true)
                                               @PathVariable(value = "jobOfferId") final UUID jobOfferId) {
        jobOfferService.deleteJobOffer(jobOfferId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/{jobOfferId}/numberofapplications", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ApplicationsCountResponse> getNumberOfApplicationsForJobOffer(@ApiParam(value = "Job Offer id for which number of applications will be tracked", required = true)
                                                                                        @PathVariable(value = "jobOfferId") final UUID jobOfferId) {
        JobOffer jobOffer = jobOfferService.getJobOffer(jobOfferId);
        return ResponseEntity.ok(ModelConversionUtils.getApplicationsCountResponse(jobOffer));
    }
    
    @ResponseBody
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @RequestMapping(value = "/{jobOfferId}/apply", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JobApplicationResponse> applyForJob(@ApiParam(value = "Job Offer id for which new job application will be sent", required = true)
                                                              @PathVariable(value = "jobOfferId") final UUID jobOfferId,
                                                              @RequestBody @Valid final JobApplicationRequest jobApplicationRequest) {
        JobApplication newJobApplication = ModelConversionUtils.convertToJobApplication(jobApplicationRequest);
        JobApplication createdJobApplication = jobApplicationService.createJobApplication(newJobApplication.toBuilder()
                                                                    .relatedOfferId(jobOfferId)
                                                                    .build());
        JobOffer relatedJobOffer = jobOfferService.getJobOffer(jobOfferId);
        return ResponseEntity.ok(ModelConversionUtils.convertToJobApplicationResponse(createdJobApplication, relatedJobOffer));
    }
    
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/{jobOfferId}/application", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<JobApplicationResponse>> getAllJobApplicationsForOfferId(@ApiParam(value = "Job Offer id for which all job applications will be fetched", required = true)
                                                                                        @PathVariable(value = "jobOfferId") final UUID jobOfferId) {
        List<JobApplication> jobApplicationsByJobOffer = jobApplicationService.getJobApplicationsByJobOfferId(jobOfferId);
        JobOffer relatedJobOffer = jobOfferService.getJobOffer(jobOfferId);     
        return ResponseEntity.ok(jobApplicationsByJobOffer.stream()
                                                          .map(eachEntry -> ModelConversionUtils.convertToJobApplicationResponse(eachEntry, relatedJobOffer))
                                                          .collect(Collectors.toList()));
    }

    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/{jobOfferId}/application/{applicationId}", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JobApplicationResponse> getJobApplication(@ApiParam(value = "Job Offer id for which job application will be fetched", required = true)
                                                                    @PathVariable(value = "jobOfferId") final UUID jobOfferId,
                                                                    @ApiParam(value = "Job application id for which job application will be fetched", required = true)
                                                                    @PathVariable(value = "applicationId") final UUID applicationId) {
        JobOffer jobOffer = jobOfferService.getJobOffer(jobOfferId);
        JobApplication jobApplication = jobApplicationService.getJobApplication(applicationId);
        return ResponseEntity.ok(ModelConversionUtils.convertToJobApplicationResponse(jobApplication, jobOffer));
    }
}
