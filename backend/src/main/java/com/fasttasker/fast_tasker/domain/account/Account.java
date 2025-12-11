package com.fasttasker.fast_tasker.domain.account;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * represents the root of the Aggregate Root for user's account (Tasker).
 * This entity manages the central identity, credentials and account status.
 */
@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class Account {

    /**
     * unique ID generates for the application. Primary Key (PK)
     */
    @Id
    private UUID taskerId;

    /**
     * Email for account, mapped as an  immutable VO.
     * The column email in the BD must be unique.
     */
    @Embedded
    private Email email;

    @Embedded
    private Password passwordHash;

    /**
     * The account state of the account.
     * its stored as a string in the BD greater readability.
     */
    @Enumerated(EnumType.STRING)
    @Column(name ="status", nullable = false)
    private AccountStatus status;

    // Note: the taskerId UUID is assumed to be generated in the application layer (services) before saved.
    // if a generated here, so we used @GeneratedValue.
}