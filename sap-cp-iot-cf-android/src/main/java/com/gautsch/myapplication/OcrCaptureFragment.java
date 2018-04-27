/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gautsch.myapplication;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gautsch.domain.ISensorData;
import com.gautsch.util.ScpConnectionInfoFactory;
import com.gautsch.util.SensorDataFactory;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.samples.vision.ocrreader.OcrFilter;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * recognizes text.
 */
public class  OcrCaptureFragment extends Fragment implements View.OnClickListener {

    // Use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView filter_by_prefix;
    private TextView statusMessage;
    private TextView textValue;
    private String filterByPrefix;


    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "OcrCaptureFragment";


    public OcrCaptureFragment() {
    }

    public static OcrCaptureFragment newInstance() {
        OcrCaptureFragment fragment = new OcrCaptureFragment();
        Bundle args = new Bundle();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ocr_capture, container, false);

        statusMessage = (TextView)rootView.findViewById(R.id.status_message);
        textValue = (TextView)rootView.findViewById(R.id.text_value);

        autoFocus = (CompoundButton) rootView.findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) rootView.findViewById(R.id.use_flash);
        filter_by_prefix = (TextView) rootView.findViewById(R.id.filter_by_prefix);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
         try {
            filterByPrefix = preferences.getString("settings_filterByPrefix", "");
        } catch (Exception ex) {
             filterByPrefix = "";
        }

        if (filterByPrefix.isEmpty())
        {
            filter_by_prefix.setText("No text filter set");
        }
        else
        {
            filter_by_prefix.setText("Filter prefix: " + filterByPrefix);
        }
        rootView.findViewById(R.id.read_text).setOnClickListener(this);

        return rootView;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_text) {
            // launch Ocr capture activity.
            Intent intent = new Intent(getActivity(), OcrCaptureActivity.class);
            intent.putExtra(OcrCaptureActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());
            //TODO
            intent.putExtra(OcrCaptureActivity.FilterByPrefix, filterByPrefix);

            startActivityForResult(intent, RC_OCR_CAPTURE);
        }
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    statusMessage.setText(R.string.ocr_success);
                    String filteredText = filterByPrefix.isEmpty() ? text : OcrFilter.filter(text, filterByPrefix);
                    textValue.setText(filteredText);

                    sendSerialnumberToScp(filteredText);
                    Log.d(TAG, "Text read: " + text);
                } else {
                    statusMessage.setText(R.string.ocr_failure);

                    sendSerialnumberToScp("read failed");
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void sendSerialnumberToScp(String serialnumber) {
        try {
            ScpCommunication scpCommunication = ScpCommunication.getInstance(ScpConnectionInfoFactory.createConnectionInfoFromSettings(getContext()));
            if (!scpCommunication.isInitialized())
            {
                scpCommunication.initialize();
            }

            ISensorData sensorData = SensorDataFactory.create("SERIALNUMBER", serialnumber);
            scpCommunication.setSensorData(sensorData);
            scpCommunication.sendSensorData();

        }
        catch (Exception ex)
        {
            //TODO Exception handling
        }
        }
}
