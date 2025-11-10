package com.fasttasker.fast_tasker.domain.account;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.UUID;


@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor

public class Account {

    @Id
    private UUID taskerId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", unique = true, nullable = false))
    private Email email;


    private Password passwordHash;

    /**
     * 
     */
    private AccountStatus status;


    /**
     * 
     */
    public Password has;


}