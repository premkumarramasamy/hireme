package de.heavenhr.hireme.model;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;

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
public class JobOffer {

    private UUID offerId;
    
    @NotEmpty(message = "Job Title cannot be empty")
    @NotNull(message = "Job Title cannot be null")
    private String jobTitle;
    
    private String description;
    
    private DateTime startDate;
    
    private int numberOfApplications;
}
