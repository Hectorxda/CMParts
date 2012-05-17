package com.cyanogenmod.cmparts.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.cyanogenmod.cmparts.R;

public class MyExtraToolsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.extra_settings);
    }
}
