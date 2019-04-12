package de.heavenhr.hireme.controller.model;

import java.util.UUID;

import org.joda.time.DateTime;

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
public class JobOfferResponse {

    @ApiModelProperty(notes = "Unique id for the job offer")
    private UUID offerId;
    
    @ApiModelProperty(notes = "The title for the job offer")
    private String jobTitle;
    
    @ApiModelProperty(notes = "The description of the job offer")
    private String description;
    
    @ApiModelProperty(notes = "The start date of the job offer")
    private DateTime startDate;
    
    @ApiModelProperty(notes = "The number of job applications sent for this job offer")
    private int numberOfApplications;
}
