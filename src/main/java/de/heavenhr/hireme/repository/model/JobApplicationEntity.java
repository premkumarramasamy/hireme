package de.heavenhr.hireme.repository.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import de.heavenhr.hireme.model.ApplicationStatus;
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
@Entity
@Table(name = "job_application")
public class JobApplicationEntity {
    @Id
    private UUID applicationId;
    
    @Column
    private String candidateEmail;
    
    @Column
    private String resumeText;
    
    @Column
    private ApplicationStatus applicationStatus;
    
    @Column
    private UUID relatedOfferId;

}
