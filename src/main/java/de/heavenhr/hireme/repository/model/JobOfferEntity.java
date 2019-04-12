package de.heavenhr.hireme.repository.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "job_offer")
public class JobOfferEntity {

    @Id
    private UUID offerId;
    
    @Column
    private String jobTitle;
    
    @Column
    private String description;
    
    @Column
    private long startDate;
    
    @Column
    private int numberOfApplications;
}
