package de.heavenhr.hireme.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.json.JSONObject;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import de.heavenhr.hireme.controller.model.JobApplicationResponse;
import de.heavenhr.hireme.model.JobApplication;
import de.heavenhr.hireme.model.JobOffer;
import de.heavenhr.hireme.service.JobApplicationNotificationService;
import de.heavenhr.hireme.service.JobApplicationService;
import de.heavenhr.hireme.service.JobOfferService;
import de.heavenhr.hireme.util.TestHelper;

@RunWith(MockitoJUnitRunner.class)
public class JobApplicationControllerTest {
    private MockMvc mockMvc;
    private UUID jobApplicationId;
    private String statusChangeRequestJson;

    @Captor
    private ArgumentCaptor<UUID> jobApplicationIdCaptor;

    @Captor
    private ArgumentCaptor<JobApplication> jobApplicationCaptor;
    
    @Mock
    private JobApplicationService applicationService;
    
    @Mock
    private JobOfferService offerService;
    
    @Mock
    private JobApplicationNotificationService notifyService;

    @InjectMocks
    private JobApplicationController applicationController;
    
    private TestHelper testHelper = TestHelper.INSTANCE;
    private static final String URL_ID = "/application/{applicationId}/updatestatus";

    @Before
    public void setup() {
        jobApplicationId = UUID.fromString("2346c89c-0c0d-4b3e-97ff-cc9c246fd39a");
        mockMvc = MockMvcBuilders.standaloneSetup(applicationController).build();
        statusChangeRequestJson = "{\"applicationStatus\": \"INVITED\"}";
    }

    @Test
    public void patchJobApplicationStatus() throws Exception {
        final JobApplication existingResponse = testHelper.getJsonFromResource("/controller/status_change.json", JobApplication.class);
        final JobApplication updatedResponse = testHelper.getJsonFromResource("/controller/status_change_updated.json", JobApplication.class);
        final JobOffer jobOfferResponse = testHelper.getJsonFromResource("/controller/joboffer_response.json", JobOffer.class);
        final JobApplicationResponse epectedResponse = testHelper.getJsonFromResource("/controller/final_expected_response.json", JobApplicationResponse.class);
        
        when(applicationService.getJobApplication(jobApplicationId)).thenReturn(existingResponse);
        when(applicationService.patchJobApplication(any(JobApplication.class))).thenReturn(updatedResponse);
        when(offerService.getJobOffer(any(UUID.class))).thenReturn(jobOfferResponse);
        final MvcResult actualResponse = mockMvc
                .perform(patch(URL_ID, jobApplicationId).contentType(MediaType.APPLICATION_JSON).content(statusChangeRequestJson))
                .andExpect(status().isOk())
                .andReturn();
        verify(notifyService, times(1)).doStatusChangeNotifications();
        verify(applicationService).patchJobApplication(jobApplicationCaptor.capture());
        JSONObject jsonObject = new JSONObject(actualResponse.getResponse().getContentAsString());
        assertEquals(epectedResponse.getApplicationStatus().toString(), jsonObject.getString("applicationStatus"));
        assertEquals(epectedResponse.getApplicationId().toString(), jsonObject.getString("applicationId"));
        assertEquals(epectedResponse.getResumeText(), jsonObject.getString("resumeText"));
        assertEquals(epectedResponse.getCandidateEmail(), jsonObject.getString("candidateEmail"));
    }
}
