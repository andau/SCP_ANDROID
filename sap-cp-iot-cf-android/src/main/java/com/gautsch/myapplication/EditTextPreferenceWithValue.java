package com.gautsch.myapplication;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

//custom view, which enables to show the actual value of a preference
public class EditTextPreferenceWithValue extends EditTextPreference  {
    private TextView textValue;

    public EditTextPreferenceWithValue(Context context) {
        super(context);
        setLayoutResource(R.layout.preference_with_value);
    }

    public EditTextPreferenceWithValue(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.preference_with_value);
    }

    public EditTextPreferenceWithValue(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutResource(R.layout.preference_with_value);
    }

    @Override
    protected final void onBindView(View view) {
        super.onBindView(view);
        textValue = (TextView) view.findViewById(R.id.pref_value);
        if (textValue != null) {
            textValue.setText(getText());
        }
    }

    @Override
    public final void setText(String text) {
        super.setText(text);
        if (textValue != null) {
            textValue.setText(getText());
        }
    }
}