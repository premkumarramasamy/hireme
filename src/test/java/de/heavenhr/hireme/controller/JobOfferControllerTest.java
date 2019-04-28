package de.heavenhr.hireme.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.google.common.reflect.TypeToken;

import de.heavenhr.hireme.exception.ResourceAlreadyExistsException;
import de.heavenhr.hireme.model.JobApplication;
import de.heavenhr.hireme.model.JobOffer;
import de.heavenhr.hireme.service.JobApplicationService;
import de.heavenhr.hireme.service.JobOfferService;
import de.heavenhr.hireme.util.TestHelper;

@RunWith(MockitoJUnitRunner.class)
public class JobOfferControllerTest {
    private MockMvc mockMvc;
    private UUID jobOfferId;
    private UUID jobApplicationId;

    @Captor
    private ArgumentCaptor<UUID> jobOfferIdCaptor;

    @Captor
    private ArgumentCaptor<UUID> jobApplicationIdCaptor;

    @Captor
    private ArgumentCaptor<JobOffer> offerCaptor;
    
    @Captor
    private ArgumentCaptor<JobApplication> applicationCaptor;
    
    @Mock
    private JobOfferService jobOfferService;
    
    @Mock
    private JobApplicationService jobApplicationService;

    @InjectMocks
    private JobOfferController jobOfferController;
    
    private TestHelper testHelper = TestHelper.INSTANCE;
    private static final String URL_ALL_OFFER = "/joboffer";
    private static final String URL_ALL_OFFER_BY_ID = "/joboffer/{jobOfferId}";
    private static final String URL_ALL_OFFER_NOOFAPPS_ID = "/joboffer/{jobOfferId}/numberofapplications";
    private static final String URL_ALL_OFFER_APPLY_ID = "/joboffer/{jobOfferId}/apply";
    private static final String URL_ALL_OFFER_APPLICATION_ID = "/joboffer/{jobOfferId}/application/{applicationId}";
    @Before
    public void setup() {
        jobOfferId = UUID.fromString("30239cae-13e8-43fd-8ebc-a77a123ecfa9");
        jobApplicationId = UUID.fromString("b122b2f2-b374-4506-bb73-94f0be98c556");
        mockMvc = MockMvcBuilders.standaloneSetup(jobOfferController).build();
    }

    @Test
    public void test_getAllJobOffers() throws Exception {
    
        final List<JobOffer> allOffers = testHelper.getJsonFromResource("/controller/joboffer_response_list.json", new TypeToken<List<JobOffer>>(){}.getType());
        
        when(jobOfferService.getAllJobOffer()).thenReturn(allOffers);

        mockMvc.perform(get(URL_ALL_OFFER))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].jobTitle").value("Lead Engineer"));
        verify(jobOfferService, times(1)).getAllJobOffer();
    }
    
    @Test
    public void test_getJobOfferById() throws Exception {
    
        final JobOffer jobOfferByIdResponse = testHelper.getJsonFromResource("/controller/joboffer_response.json", JobOffer.class);
        
        when(jobOfferService.getJobOffer(jobOfferId)).thenReturn(jobOfferByIdResponse);

        mockMvc.perform(get(URL_ALL_OFFER_BY_ID, jobOfferId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.jobTitle").value("Lead Engineer"))
               .andExpect(jsonPath("$.offerId").value(jobOfferId.toString()));

        verify(jobOfferService, times(1)).getJobOffer(jobOfferIdCaptor.capture());
        assertEquals(jobOfferId, jobOfferIdCaptor.getValue());
    }
    
    @Test
    public void test_createJobOffer() throws Exception {
    
        JobOffer jobOfferByIdResponse = testHelper.getJsonFromResource("/controller/joboffer_response.json", JobOffer.class);
        jobOfferByIdResponse = jobOfferByIdResponse.toBuilder().offerId(null).numberOfApplications(0).build();
        
        String convertObjectToJson = testHelper.convertObjectToJson(jobOfferByIdResponse, JobOffer.class);
        
        when(jobOfferService.createJobOffer(any(JobOffer.class))).thenReturn(jobOfferByIdResponse);

        mockMvc.perform(post(URL_ALL_OFFER).contentType(MediaType.APPLICATION_JSON).content(convertObjectToJson))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.jobTitle").value("Lead Engineer"));

        verify(jobOfferService, times(1)).createJobOffer(offerCaptor.capture());
        JobOffer actual = offerCaptor.getValue();
        assertEquals(jobOfferByIdResponse.getDescription(), actual.getDescription());
        assertEquals(jobOfferByIdResponse.getJobTitle(), actual.getJobTitle());
        assertEquals(jobOfferByIdResponse.getNumberOfApplications(), actual.getNumberOfApplications());
    }
    
    @Test
    public void test_createJobOfferAlreadyExists() throws Exception {
    
        JobOffer jobOfferByIdResponse = testHelper.getJsonFromResource("/controller/joboffer_response.json", JobOffer.class);
        jobOfferByIdResponse = jobOfferByIdResponse.toBuilder().offerId(null).numberOfApplications(0).build();
        
        String convertObjectToJson = testHelper.convertObjectToJson(jobOfferByIdResponse, JobOffer.class);
        
        when(jobOfferService.createJobOffer(any(JobOffer.class)))
                                   .thenThrow(new ResourceAlreadyExistsException("Resource Already Exists"));

        mockMvc.perform(post(URL_ALL_OFFER).contentType(MediaType.APPLICATION_JSON).content(convertObjectToJson))
                .andExpect(status().isNotAcceptable());
    }
    
    @Test
    public void test_updateJobOffer() throws Exception {
        JobOffer updatedOffer = testHelper.getJsonFromResource("/controller/joboffer_request_update.json", JobOffer.class);
        
        String convertedString = testHelper.convertToJsonString(updatedOffer, JobOffer.class);
        when(jobOfferService.updateJobOffer(any(JobOffer.class))).thenReturn(updatedOffer);

        mockMvc.perform(put(URL_ALL_OFFER_BY_ID, jobOfferId)
               .contentType(MediaType.APPLICATION_JSON)
               .content(convertedString))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.jobTitle").value("Lead Engineer-Updated"));

        verify(jobOfferService, times(1)).updateJobOffer(offerCaptor.capture());
        JobOffer actual = offerCaptor.getValue();
        assertEquals(updatedOffer.getDescription(), actual.getDescription());
        assertEquals(updatedOffer.getJobTitle(), actual.getJobTitle());
        assertEquals(0, actual.getNumberOfApplications());
    }
    
    @Test
    public void test_deleteJobOffer() throws Exception {
    
        mockMvc.perform(delete(URL_ALL_OFFER_BY_ID, jobOfferId))
               .andExpect(status().isNoContent());
        
        verify(jobOfferService, times(1)).deleteJobOffer(jobOfferIdCaptor.capture());
        assertEquals(jobOfferId, jobOfferIdCaptor.getValue());
    }
    
    @Test
    public void test_getNumberOfApplications() throws Exception {
        final JobOffer jobOfferByIdResponse = testHelper.getJsonFromResource("/controller/joboffer_response.json", JobOffer.class);
        
        when(jobOfferService.getJobOffer(jobOfferId)).thenReturn(jobOfferByIdResponse);

        mockMvc.perform(get(URL_ALL_OFFER_NOOFAPPS_ID, jobOfferId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.numberOfApplications").value("2"));
        
        verify(jobOfferService, times(1)).getJobOffer(jobOfferIdCaptor.capture());
        assertEquals(jobOfferId, jobOfferIdCaptor.getValue());
    }
    
    @Test
    public void test_applyForAJobOffer() throws Exception {
        final JobOffer jobOfferByIdResponse = testHelper.getJsonFromResource("/controller/joboffer_response.json", JobOffer.class);
        when(jobOfferService.getJobOffer(jobOfferId)).thenReturn(jobOfferByIdResponse);

        JobApplication jobApplicationResponse = testHelper.getJsonFromResource("/controller/status_change.json", JobApplication.class);
        jobApplicationResponse = jobApplicationResponse.toBuilder().applicationId(null).build();
        when(jobApplicationService.createJobApplication(any(JobApplication.class))).thenReturn(jobApplicationResponse);

        mockMvc.perform(post(URL_ALL_OFFER_APPLY_ID, jobOfferId).contentType(MediaType.APPLICATION_JSON).content(testHelper.convertObjectToJson(jobApplicationResponse, JobApplication.class)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.candidateEmail").value("r.prem90@gmail.com"));

        verify(jobApplicationService, times(1)).createJobApplication(applicationCaptor.capture());
        JobApplication actual = applicationCaptor.getValue();
        assertEquals(jobApplicationResponse.getCandidateEmail(), actual.getCandidateEmail());
        assertEquals(jobApplicationResponse.getResumeText(), actual.getResumeText());
        assertEquals(jobApplicationResponse.getRelatedOfferId(), actual.getRelatedOfferId());
    }
    
    @Test
    public void test_getJobApplication() throws Exception {
    
        final JobOffer jobOfferByIdResponse = testHelper.getJsonFromResource("/controller/joboffer_response.json", JobOffer.class);
        
        when(jobOfferService.getJobOffer(jobOfferId)).thenReturn(jobOfferByIdResponse);
        
        JobApplication jobApplicationResponse = testHelper.getJsonFromResource("/controller/status_change.json", JobApplication.class);
        when(jobApplicationService.getJobApplication(any(UUID.class))).thenReturn(jobApplicationResponse);

        mockMvc.perform(get(URL_ALL_OFFER_APPLICATION_ID, jobOfferId, jobApplicationId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.candidateEmail").value("r.prem90@gmail.com"))
               .andExpect(jsonPath("$.relatedJobOffer.offerId").value(jobOfferId.toString()));

        verify(jobOfferService, times(1)).getJobOffer(jobOfferIdCaptor.capture());
        verify(jobApplicationService, times(1)).getJobApplication(jobApplicationIdCaptor.capture());
        assertEquals(jobOfferId, jobOfferIdCaptor.getValue());
        assertEquals(jobApplicationId, jobApplicationIdCaptor.getValue());
    }
}
