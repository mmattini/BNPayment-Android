/*
 * Copyright (c) 2016 Bambora ( http://bambora.com/ )
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.bambora.nativepayment.handlers;

import android.content.Context;

import com.bambora.nativepayment.logging.BNLog;
import com.threatmetrix.TrustDefender.Config;
import com.threatmetrix.TrustDefender.EndNotifier;
import com.threatmetrix.TrustDefender.ProfilingOptions;
import com.threatmetrix.TrustDefender.ProfilingResult;
import com.threatmetrix.TrustDefender.THMStatusCode;
import com.threatmetrix.TrustDefender.TrustDefender;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link TrustDefenderHandler} handles the responsibilities of managing the Trust Defender
 * integration. <br><br>
 */

public class TrustDefenderHandler {

    private static TrustDefenderHandler ourInstance = new TrustDefenderHandler();

    private static final String LOG_TAG = "TrustDefenderHandler";
    private static final String ORGANIZATION_ID = "1fu9nc10";
    private static final int MAX_PROFILING_ATTEMPTS = 5;

    /**
     * A boolean indicating whether setup is done.
     */
    private boolean mIsSetup = false;

    /**
     * A string containing a Trust Defender session ID
     */
    private String mSessionId;

    /**
     * Trust defender instance
     */
    private TrustDefender mTrustDefender;

    /**
     * Number of profiling retries made
     */
    private int mProfilingAttempts = 0;


    /**
     * Profiling options
     */
    private ProfilingOptions mProfilingOptions;

    private String mMerchantAccount;

    public static TrustDefenderHandler getInstance() {
        return ourInstance;
    }

    /**
     * Method for setting up {@link TrustDefenderHandler}. Must be called before using the handler.
     * This method is required to set context and merchantId.
     *
     * @param merchantAccount required
     * @param context required
     * @param trustDefender optional
     */
    public void setupTrustDefender(
            String merchantAccount,
            Context context,
            TrustDefender trustDefender) {

        if (!mIsSetup) {
            mMerchantAccount = merchantAccount;
            mTrustDefender = trustDefender != null ? trustDefender : TrustDefender.getInstance();
            mProfilingOptions = getProfilingOptions(mProfilingCompletionHandler);

            Config trustDefenderConfig = new Config()
                    .setOrgId(ORGANIZATION_ID)
                    .setContext(context);

            THMStatusCode status = mTrustDefender.init(trustDefenderConfig);

            BNLog.d(LOG_TAG, "Initialization result: " + status.getDesc());

            if (status == THMStatusCode.THM_OK || status == THMStatusCode.THM_Already_Initialised) {
                mIsSetup = true;
                startProfiling();
            }
        }

    }

    /**
     * Getter for Session ID
     *
     * @return Session ID
     */
    public String getSessionId() {
        return mSessionId;
    }

    private TrustDefenderHandler() {}


    /**
     * Method for starting a Trust Defender profiling session
     */
    private void startProfiling() {

        mProfilingAttempts++;

        if (mProfilingAttempts > MAX_PROFILING_ATTEMPTS) {
            return;
        }

        try {
            Thread.sleep(1000 * mProfilingAttempts);
        } catch (InterruptedException ignored) {
        }

        THMStatusCode status = mTrustDefender.doProfileRequest(mProfilingOptions);

        BNLog.d(LOG_TAG, "Trust defender profiling profile init result:" + status.getDesc());

    }

    /**
     * Get profiling option for Trust Defender
     *
     * @param endNotifier callback
     * @return Profiling option
     */
    private ProfilingOptions getProfilingOptions(EndNotifier endNotifier) {
        List<String> customAttributes = new ArrayList<>();
        customAttributes.add(mMerchantAccount);

        return new ProfilingOptions()
                .setCustomAttributes(customAttributes)
                .setEndNotifier(endNotifier);
    }

    private EndNotifier mProfilingCompletionHandler = new EndNotifier() {
        @Override
        public void complete(ProfilingResult profilingResult) {
            BNLog.d(LOG_TAG, "Trust defender profiling ended: " + profilingResult.getStatus().getDesc());

            if (profilingResult.getStatus() == THMStatusCode.THM_OK) {
                mTrustDefender.doPackageScan(0);
                mSessionId = profilingResult.getSessionID();

                BNLog.d(LOG_TAG, "Threat metrix session ID: " + mSessionId);

            } else {
                startProfiling();
            }
        }
    };

    /**
     * Get method for profiling completion handler
     *
     * @return Profiling completion handler
     */
    public EndNotifier getProfilingCompletionHandler() {
        return this.mProfilingCompletionHandler;
    }

}