package de.heavenhr.hireme.model;

import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
public class JobApplication {
    
    private UUID applicationId;
    
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email cannot be empty")
    @NotNull(message = "Email cannot be null")
    private String candidateEmail;
    
    private String resumeText;
    
    private ApplicationStatus applicationStatus;
    
    private UUID relatedOfferId;
}
