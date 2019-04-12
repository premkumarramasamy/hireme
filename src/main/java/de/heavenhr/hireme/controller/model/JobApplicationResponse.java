package de.heavenhr.hireme.controller.model;

import java.util.UUID;

import de.heavenhr.hireme.model.ApplicationStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(includeFieldNames = true)
public class JobApplicationResponse {

    @ApiModelProperty(notes = "The job application id")
    private UUID applicationId;
    
    @ApiModelProperty(notes = "The candidate's email address")
    private String candidateEmail;
    
    @ApiModelProperty(notes = "The candidate's resume as text")
    private String resumeText;
    
    @ApiModelProperty(notes = "The job application status")
    private ApplicationStatus applicationStatus;
    
    @ApiModelProperty(notes = "The related job offer details")
    private JobOfferResponse relatedJobOffer;
}
