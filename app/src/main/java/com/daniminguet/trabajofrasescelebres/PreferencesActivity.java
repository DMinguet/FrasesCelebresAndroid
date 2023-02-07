package com.daniminguet.trabajofrasescelebres;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.daniminguet.trabajofrasescelebres.fragments.FragmentPreferencias;

public class PreferencesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new FragmentPreferencias())
                .commit();
    }
}
