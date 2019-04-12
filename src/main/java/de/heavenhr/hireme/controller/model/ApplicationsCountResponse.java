package de.heavenhr.hireme.controller.model;

import java.util.UUID;

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
public class ApplicationsCountResponse {

    @ApiModelProperty(notes = "The job offer id")
    private UUID jobOfferId;
    
    @ApiModelProperty(notes = "The number of job applications per job offer")
    private int numberOfApplications;
}
