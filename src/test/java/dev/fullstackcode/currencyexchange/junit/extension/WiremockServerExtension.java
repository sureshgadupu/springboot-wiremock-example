package dev.fullstackcode.currencyexchange.junit.extension;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class WiremockServerExtension  implements BeforeAllCallback, AfterAllCallback , ParameterResolver {

    private static final WireMockServer currencyServer =
            new WireMockServer(WireMockConfiguration.options().port(4141));

    private static final WireMockServer countryServer =
            new WireMockServer(WireMockConfiguration.options().port(4040));
    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        if(currencyServer.isRunning()) {
            currencyServer.stop();
        }
        if(countryServer.isRunning()) {
            countryServer.stop();
        }

    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        if(!currencyServer.isRunning()) {
            currencyServer.start();
        }
        if(!countryServer.isRunning()) {
            countryServer.start();
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(WireMockServer.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if(parameterContext.getParameter().getType().equals(WireMockServer.class) && parameterContext.getParameter().getName().equals("currencyServer")) {
            return currencyServer;
        }
        if(parameterContext.getParameter().getType().equals(WireMockServer.class) && parameterContext.getParameter().getName().equals("countryServer")) {
            return countryServer;
        }
        return null;
    }

   public static WireMockServer currencyWireMock() {
        return currencyServer;
    }

    public static WireMockServer countryWireMock() {
        return countryServer;
    }
}
