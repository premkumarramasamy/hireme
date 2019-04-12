package de.heavenhr.hireme.controller.model;

import javax.validation.constraints.NotEmpty;

import org.springframework.lang.NonNull;

import de.heavenhr.hireme.model.ApplicationStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JobApplicationStatusUpdateRequest {

    @NonNull
    @NotEmpty
    @ApiModelProperty(notes = "The job application status")
    private ApplicationStatus applicationStatus;
}
