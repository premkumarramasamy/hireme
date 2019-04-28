package de.heavenhr.hireme.controller.model;

import org.springframework.lang.NonNull;

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
public class JobApplicationStatusUpdateRequest {

    @NonNull
    @ApiModelProperty(notes = "The job application status")
    private ApplicationStatus applicationStatus;
}
