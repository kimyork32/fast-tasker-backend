package com.fasttasker.fast_tasker.persistence;

import com.fasttasker.fast_tasker.domain.notification.Notification;
import com.fasttasker.fast_tasker.domain.notification.NotificationStatus;
import com.fasttasker.fast_tasker.domain.notification.NotificationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaNotificationRepositoryTest {

    @Autowired
    private JpaNotificationRepository notificationRepository;

    @Test
    void shouldSaveAndFindByAccountId() {
        // 1. ARRANGE
        UUID uuid = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        String message = "test notification";
        LocalDateTime time = LocalDateTime.now();

        Notification newNotification = new Notification(
                uuid,
                accountId,
                NotificationType.QUESTION,
                message,
                time,
                false,
                NotificationStatus.UNREAD
        );

        // 2. ACT
        notificationRepository.save(newNotification);
        Optional<Notification> notificationFoundOpt = notificationRepository.findById(uuid);

        // 3. ASSERT
        assertThat(notificationFoundOpt).isPresent();

        Notification foundNotification = notificationFoundOpt.get();

        assertThat(foundNotification).isEqualTo(newNotification);

    }
}