package com.fasttasker.fast_tasker.persistence;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.logging.Logger;

/**
 * integration Test
 */
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ConversationRepositoryImpl.class))
class JpaConversationRepositoryTest {
    // TODO
}