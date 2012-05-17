package com.cyanogenmod.cmparts.activities.led;

import com.cyanogenmod.cmparts.R;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

/**
 * for this just use for P990 ,other phone i don not to test may be make bad damage!
 * this is cm's bug i dont know just use color to show blink
 * for my test:
 * forceeventon+blue means blink fast
 * forceeventon+red means blink slow
 * forceoff+green mean led off
 * @author lei.chen
 *
 */
public class ShenduLedSwitch extends PreferenceActivity implements
Preference.OnPreferenceChangeListener{
    
    private static final String TAG = "ShenduLedSwitch";
    
    private static final int DEFAULT_INDEX_FIRST = 0;
    private static final int DEFAULT_INDEX_SECOND = 1;
    
    private static final String DEFAULT_PACKAGE_NAME = "com.cyanogenmod.led.categories_settings.unconf";
    private static final String DEFAULT_BLINK = "default";
    private static final String DEFAULT_LEDOFF = "green";
    
    private ListPreference mSwitchPref;
    private ListPreference mBlinkPref;
    
    private String mSwitch;
    private String mBlink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.led_switch);
        
        mSwitchPref = (ListPreference) findPreference("switch");
        mSwitchPref.setOnPreferenceChangeListener(this);
        
        mBlinkPref = (ListPreference) findPreference("blink");
        mBlinkPref.setOnPreferenceChangeListener(this);
        
        String readFromSettings = Settings.System.getString(getContentResolver(),
                Settings.System.NOTIFICATION_PACKAGE_COLORS);
        if (readFromSettings != null && !readFromSettings.isEmpty() && !readFromSettings.equals("")) {
            String colors[] = LedUtils.arrayFromString(readFromSettings, '=');
            if (TextUtils.equals(colors[3], "forceoff")) {
                mBlinkPref.setEnabled(false);
                mSwitchPref.setValue(colors[3]);
                mSwitch = mSwitchPref.getValue();
                mBlink = DEFAULT_LEDOFF;
            }else {
                mSwitchPref.setValue(colors[3]);
                mBlinkPref.setValue(colors[1]);
                mSwitch = mSwitchPref.getValue();
                mBlink = mBlinkPref.getValue();
            }
        }else {
            setDefaultValue();
        }
        //setDefaultValue();
        
        saveToSystem();
    }
    
    private void setDefaultValue(){
        mSwitchPref.setValueIndex(DEFAULT_INDEX_FIRST);
        mBlinkPref.setValueIndex(DEFAULT_INDEX_FIRST);
        mSwitch = mSwitchPref.getValue();
        mBlink = mBlinkPref.getValue();
    }
    
    private void saveToSystem(){
        StringBuilder builder = new StringBuilder();
        builder.append(DEFAULT_PACKAGE_NAME);
        builder.append("=");
        builder.append(mBlink);
        builder.append("=");
        builder.append(DEFAULT_BLINK);
        builder.append("=");
        builder.append(mSwitch);
        builder.append("=");
        Log.d(TAG, "value------"+builder.toString());
        Settings.System.putString(getContentResolver(),
                Settings.System.NOTIFICATION_PACKAGE_COLORS, builder.toString());
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mSwitchPref) {
            mSwitch = newValue.toString();
            if (TextUtils.equals(mSwitch, "forceoff")) {
                mBlinkPref.setEnabled(false);
                mBlink = DEFAULT_LEDOFF;
            }else {
                mBlinkPref.setEnabled(true);
                mBlinkPref.setValueIndex(DEFAULT_INDEX_FIRST);
                mBlink = mBlinkPref.getValue();
            }
        } else if (preference == mBlinkPref) {
            mBlink = newValue.toString();
        } 
        saveToSystem();
        return true;
    }
}
