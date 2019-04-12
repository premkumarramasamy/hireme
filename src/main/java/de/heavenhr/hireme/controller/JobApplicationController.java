package de.heavenhr.hireme.controller;

import java.util.UUID;

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

import de.heavenhr.hireme.controller.model.JobApplicationResponse;
import de.heavenhr.hireme.controller.model.JobApplicationStatusUpdateRequest;
import de.heavenhr.hireme.model.JobApplication;
import de.heavenhr.hireme.model.JobOffer;
import de.heavenhr.hireme.service.JobApplicationNotificationService;
import de.heavenhr.hireme.service.JobApplicationService;
import de.heavenhr.hireme.service.JobOfferService;
import de.heavenhr.hireme.utils.ModelConversionUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/application")
public class JobApplicationController {
    
    @Autowired
    private JobApplicationService jobApplicationService;
    
    @Autowired 
    private JobApplicationNotificationService jobApplicationNotificationService;
    
    @Autowired
    private JobOfferService jobOfferService;
    
    @ApiOperation(value = "Change the application status")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully deleted a job application"),
        @ApiResponse(code = 404, message = "The job application you were trying to change is not existing")
    })
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/{applicationId}/updatestatus", method = {RequestMethod.PATCH}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<JobApplicationResponse> updateStatus(@ApiParam(value = "Job application id for which application status will be changed", required = true)
                                                               @PathVariable(value = "applicationId") final UUID applicationId,
                                                               @ApiParam(value = "Job application status update request", required = true)
                                                               @RequestBody @Valid final JobApplicationStatusUpdateRequest jobApplicationStatusUpdateRequest) {
        JobApplication jobApplication = jobApplicationService.getJobApplication(applicationId);
        JobApplication patchJobApplication = jobApplicationService.patchJobApplication(jobApplication.toBuilder()
                                                                                    .applicationStatus(jobApplicationStatusUpdateRequest.getApplicationStatus())
                                                                                    .build());
        jobApplicationNotificationService.doStatusChangeNotifications();
        JobOffer jobOffer = jobOfferService.getJobOffer(applicationId);
        return ResponseEntity.ok(ModelConversionUtils.convertToJobApplicationResponse(patchJobApplication, jobOffer));
    }
    
}

