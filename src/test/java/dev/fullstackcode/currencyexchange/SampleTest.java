package dev.fullstackcode.currencyexchange;

import dev.fullstackcode.currencyexchange.junit.extension.SampleCustomExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(SampleCustomExtension.class)
public class SampleTest {

    private static final Logger logger =  LoggerFactory.getLogger(SampleTest.class);

    @BeforeEach
    public void beforeEach(){
        logger.info("before each test in test class");
    }

    @AfterEach
    public void afterEach(){
        logger.info("after each test in test class");
    }
    @Test
    public void sampleTest() {
        logger.info("in sample test");
    }

    @Test
    public void sampleTest2() {
        logger.info("in sample test - 2");
    }
}
