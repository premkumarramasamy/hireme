package de.heavenhr.hireme.controller.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobOfferRequest {
    
    @NotEmpty(message = "Job Title cannot be empty")
    @NotNull(message = "Job Title cannot be null")
    @ApiModelProperty(notes = "The title of the job offer")
    private String jobTitle;
    
    @ApiModelProperty(notes = "The description for job offer")
    private String description;
    
    @ApiModelProperty(notes = "The start date of the job offer")
    private DateTime startDate;
    
}
