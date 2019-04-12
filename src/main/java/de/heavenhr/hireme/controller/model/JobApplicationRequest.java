package de.heavenhr.hireme.controller.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
public class JobApplicationRequest {

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email cannot be empty")
    @NotNull(message = "Email cannot be null")
    @ApiModelProperty(notes = "The candidate email address")
    private String candidateEmail;
    
    @ApiModelProperty(notes = "The candidate's resume")
    private String resumeText;
    
}
