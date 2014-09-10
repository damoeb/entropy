package org.migor.entropy.web.rest;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.migor.entropy.Application;
import org.migor.entropy.domain.Punishment;
import org.migor.entropy.repository.PunishmentRepository;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PunishmentResource REST controller.
 *
 * @see PunishmentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ActiveProfiles("dev")
public class PunishmentResourceTest {

    private static final Long DEFAULT_ID = new Long(1L);

    private static final LocalDate DEFAULT_SAMPLE_DATE_ATTR = new LocalDate(0L);

    private static final LocalDate UPD_SAMPLE_DATE_ATTR = new LocalDate();

    private static final String DEFAULT_SAMPLE_TEXT_ATTR = "sampleTextAttribute";

    private static final String UPD_SAMPLE_TEXT_ATTR = "sampleTextAttributeUpt";

    @Inject
    private PunishmentRepository punishmentRepository;

    private MockMvc restPunishmentMockMvc;

    private Punishment punishment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PunishmentResource punishmentResource = new PunishmentResource();
        ReflectionTestUtils.setField(punishmentResource, "punishmentRepository", punishmentRepository);

        this.restPunishmentMockMvc = MockMvcBuilders.standaloneSetup(punishmentResource).build();

        punishment = new Punishment();
        punishment.setId(DEFAULT_ID);
    }

    @Test
    public void testCRUDPunishment() throws Exception {

        // Create Punishment
        restPunishmentMockMvc.perform(post("/app/rest/punishments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(punishment)))
                .andExpect(status().isOk());

        // Read Punishment
        restPunishmentMockMvc.perform(get("/app/rest/punishments/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
                .andExpect(jsonPath("$.sampleDateAttribute").value(DEFAULT_SAMPLE_DATE_ATTR.toString()))
                .andExpect(jsonPath("$.sampleTextAttribute").value(DEFAULT_SAMPLE_TEXT_ATTR));

        // Update Punishment

        restPunishmentMockMvc.perform(post("/app/rest/punishments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(punishment)))
                .andExpect(status().isOk());

        // Read updated Punishment
        restPunishmentMockMvc.perform(get("/app/rest/punishments/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
                .andExpect(jsonPath("$.sampleDateAttribute").value(UPD_SAMPLE_DATE_ATTR.toString()))
                .andExpect(jsonPath("$.sampleTextAttribute").value(UPD_SAMPLE_TEXT_ATTR));

        // Delete Punishment
        restPunishmentMockMvc.perform(delete("/app/rest/punishments/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Read nonexisting Punishment
        restPunishmentMockMvc.perform(get("/app/rest/punishments/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }
}
