package com.bambora.nativepayment.handlers;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.bambora.nativepayment.base.MockitoInstrumentationTestCase;
import com.threatmetrix.TrustDefender.Config;
import com.threatmetrix.TrustDefender.EndNotifier;
import com.threatmetrix.TrustDefender.ProfilingOptions;
import com.threatmetrix.TrustDefender.ProfilingResult;
import com.threatmetrix.TrustDefender.THMStatusCode;
import com.threatmetrix.TrustDefender.TrustDefender;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

/**
 * Instrumented tests for the {@link TrustDefenderHandler} class.
 */
public class TrustDefenderHandlerTest extends MockitoInstrumentationTestCase {

    @Mock
    private TrustDefender trustDefenderMock;

    @Mock
    private ProfilingResult profilingResult;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void getInstance() throws Exception {
        Assert.assertNotNull(TrustDefenderHandler.getInstance());
    }

    @Test
    public void setupTrustDefenderSuccess() throws Exception {

        // Given
        when(profilingResult.getSessionID()).thenReturn("SessionId");
        when(profilingResult.getStatus()).thenReturn(THMStatusCode.THM_OK);

        when(trustDefenderMock.init(Mockito.any(Config.class))).thenReturn(THMStatusCode.THM_OK);
        when(trustDefenderMock.doProfileRequest(Mockito.any(ProfilingOptions.class))).thenReturn(THMStatusCode.THM_OK);

        TrustDefenderHandler handler = TrustDefenderHandler.getInstance();
        Context context = InstrumentationRegistry.getContext();

        // When
        handler.setupTrustDefender("merchantAccount", context, trustDefenderMock);

        // Then
        EndNotifier endNotifier = handler.getProfilingCompletionHandler();
        Assert.assertNull(handler.getSessionId());

        // When
        endNotifier.complete(profilingResult);

        // Then
        Assert.assertEquals(handler.getSessionId(), "SessionId");

    }

    @Test
    public void setupTrustDefenderError() {

        // Given
        when(trustDefenderMock.init(Mockito.any(Config.class))).thenReturn(THMStatusCode.THM_ConfigurationError);

        TrustDefenderHandler handler = TrustDefenderHandler.getInstance();
        Context context = InstrumentationRegistry.getContext();

        // When
        handler.setupTrustDefender("merchantAccount", context, trustDefenderMock);

        // Then
        Assert.assertNull(handler.getSessionId());
    }

}