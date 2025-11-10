package com.fasttasker.fast_tasker.domain.notification;

import com.fasttasker.fast_tasker.domain.account.Account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface INotificationRepository {

    Notification save(Notification notification);

    Optional<Notification> findById(UUID id);

    List<Notification> findAll();

    List<Notification> findByAccount(Account account);

}