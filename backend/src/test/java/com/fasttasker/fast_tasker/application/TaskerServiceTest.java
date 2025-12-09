package com.fasttasker.fast_tasker.application;

import com.fasttasker.fast_tasker.application.dto.tasker.LocationRequest;
import com.fasttasker.fast_tasker.application.dto.tasker.ProfileRequest;
import com.fasttasker.fast_tasker.application.dto.tasker.TaskerRequest;
import com.fasttasker.fast_tasker.application.dto.tasker.TaskerResponse;
import com.fasttasker.fast_tasker.domain.account.IAccountRepository;
import com.fasttasker.fast_tasker.domain.tasker.ITaskerRepository;
import com.fasttasker.fast_tasker.domain.tasker.Location;
import com.fasttasker.fast_tasker.domain.tasker.Profile;
import com.fasttasker.fast_tasker.domain.tasker.Tasker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * integration test for TaskerServiceTest
 *
 * NOTE:
 * for integration test, use @SpringBootTest. Recommended if u use H2
 * for unit test, use @ExtendWith(MockitoExtension.class) and use mockito
 */
@SpringBootTest
@Transactional // for each test, make rollback
class TaskerServiceTest {

    @Autowired
    private TaskerService taskerService;

    @Autowired
    private ITaskerRepository taskerRepository;

    @Autowired
    private IAccountRepository accountRepository;

    private UUID testAccountId;

    @BeforeEach
    void setUp() {

        testAccountId = UUID.randomUUID();
        var defaultLocation = new Location(
                0,
                0,
                "",
                0
        );

        var defaultProfile = new Profile(
                "",
                "",
                "",
                defaultLocation,
                "",
                0,
                0,
                0
        );

        var tasker = new Tasker(
                UUID.randomUUID(),
                testAccountId,
                defaultProfile
        );

        taskerRepository.save(tasker);

    }

    @Test
    void shouldRegisterTaskerSuccess() {
        // 1. GIVEN
        // input DTO

        var locationRequest = new LocationRequest(
                -13.412453,
                -12.158023,
                "address street",
                4141414
        );

        var profileRequest = new ProfileRequest(
                "homer",
                "simpson",
                "https://aws-service-s3.com/photo/141454",
                "about me",
                4,
                14,
                12,
                locationRequest
        );

        var taskerRequest = new TaskerRequest(
                testAccountId.toString(),
                profileRequest
        );

        // 2.  WHEN
        TaskerResponse response = taskerService.registerTasker(taskerRequest);

        // 2. THEN
        // verify the DTO request
        assertThat(response).isNotNull();
        assertThat(response.profile().about()).isEqualTo(profileRequest.about());
        assertThat(response.profile().photo()).isEqualTo(profileRequest.photo());

        // verify that the data was saved in the bd
        Tasker taskerInBd = taskerRepository.findByAccountId(testAccountId).get();

        assertThat(taskerInBd).isNotNull();
        assertThat(taskerInBd.getProfile()).isNotNull();
        assertThat(taskerInBd.getProfile().getAbout()).isEqualTo(profileRequest.about());
        assertThat(taskerInBd.getProfile().getPhoto()).isEqualTo(profileRequest.photo());

    }

    @Test
    void getById() {
    }

    @Test
    void getByAccountId() {
    }
}