package dev.fullstackcode.currencyexchange.junit.extension;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SampleCustomExtension implements BeforeAllCallback, AfterAllCallback,
        BeforeEachCallback, AfterEachCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static final Logger logger =  LoggerFactory.getLogger(SampleCustomExtension.class);

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        logger.info("After all tests");
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        logger.info("Before all test");
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        logger.info("After each test");

    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        logger.info("Before each test");
    }


    @Override
    public void afterTestExecution(ExtensionContext extensionContext) throws Exception {
        logger.info("After each test - beforeTestExecution");
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) throws Exception {
        logger.info("Before each test - beforeTestExecution");
    }
}
