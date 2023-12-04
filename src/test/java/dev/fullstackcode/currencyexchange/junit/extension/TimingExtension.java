package dev.fullstackcode.currencyexchange.junit.extension;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import static  org.junit.jupiter.api.extension.ExtensionContext.Store;

public class TimingExtension  implements BeforeTestExecutionCallback, AfterTestExecutionCallback {
    @Override
    public void beforeTestExecution(ExtensionContext context) {
        getStore(context).put(context.getRequiredTestMethod(), System.currentTimeMillis());
    }

        private Store getStore(ExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.create(getClass(), context));
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        long startTime = getStore(context).remove(context.getRequiredTestMethod(), long.class);
        long duration = System.currentTimeMillis() - startTime;

        System.out.println("Test method [" + context.getRequiredTestMethod() + "] took " + duration + " ms.");

    }


}
