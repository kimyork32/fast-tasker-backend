package com.fasttasker.fast_tasker.persistence;

import com.fasttasker.fast_tasker.domain.notification.INotificationRepository;
import com.fasttasker.fast_tasker.domain.notification.Notification;
import com.fasttasker.fast_tasker.domain.notification.NotificationStatus;
import com.fasttasker.fast_tasker.domain.notification.NotificationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = NotificationRepositoryImpl.class))
class JpaNotificationRepositoryTest {

    @Autowired
    private INotificationRepository notificationRepository;

    @Test
    void shouldSaveAndFindByAccountId() {
        // 1. ARRANGE
        UUID uuid = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        LocalDateTime time = LocalDateTime.now();

        Notification newNotification = new Notification(
                uuid,
                accountId,
                NotificationType.QUESTION
        );

        // 2. ACT
        // save
        notificationRepository.save(newNotification);
        // find
        Optional<Notification> notificationFoundOpt = notificationRepository.findById(uuid);

        // 3. ASSERT
        assertThat(notificationFoundOpt).isPresent();

        Notification foundNotification = notificationFoundOpt.get();

        assertThat(foundNotification).isEqualTo(newNotification);

    }

    @Test
    void shouldSaveAndFindAll() {
        // 1. ARRANGE
        UUID uuid = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        String message = "test notification";
        LocalDateTime time = LocalDateTime.now();

        Notification newNotification = new Notification(
                uuid,
                accountId,
                NotificationType.QUESTION
        );

        // 2. ACT
        // save
        notificationRepository.save(newNotification);
        // find all
        List<Notification> foundNotifications = notificationRepository.findAll();

        // 3. ASSERT
        assertThat(foundNotifications)
                .withFailMessage("the array found is empty")
                .isNotEmpty();

        assertThat(foundNotifications)
                .withFailMessage("the array found does not have the expected size (size=1)")
                .hasSize(1);

        Notification foundNotification = foundNotifications.getFirst();
        assertThat(foundNotification)
                .withFailMessage("the found notification is not equal to expected")
                .isEqualTo(newNotification);
    }

    @Test
    void shouldSaveAndFindByReceiverTaskerId() {
        // 1. ARRANGE
        UUID uuid = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        String message = "test notification";
        LocalDateTime time = LocalDateTime.now();

        Notification newNotification = new Notification(
                uuid,
                accountId,
                NotificationType.QUESTION
        );

        // 2. ACT
        // save
        notificationRepository.save(newNotification);
        // find with receiver tasker ID
        Optional<Notification> notificationFoundOpt = notificationRepository.findByReceiverTaskerId(accountId);

        // 3. ASSERT
        assertThat(notificationFoundOpt)
                .withFailMessage("no found notification")
                .isPresent();

        Notification foundNotification = notificationFoundOpt.get();

        assertThat(foundNotification)
                .withFailMessage("the found notification is not equal to expected")
                .isEqualTo(newNotification);

    }
}