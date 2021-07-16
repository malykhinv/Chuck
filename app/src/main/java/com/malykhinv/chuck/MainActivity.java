package com.malykhinv.chuck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import com.malykhinv.chuck.databinding.ActivityMainBinding;
import com.malykhinv.chuck.di.App;
import com.malykhinv.chuck.jokes.mvp.view.JokesFragment;
import com.malykhinv.chuck.webview.WebviewFragment;

public class MainActivity extends AppCompatActivity {

    private static final int INITIAL_MENU_ITEM_INDEX = 0;
    private static final int EXIT_ON_BACK_PRESS_WAITING_TIME = 2000;
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private static final String APP_PREFERENCES = "DATA";
    private final Context context = App.getAppComponent().getContext();
    private final SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    private JokesFragment jokesFragment;
    private WebviewFragment webviewFragment;
    private ActivityMainBinding b;
    private boolean isAboutToClose = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            sharedPreferences.edit().clear().apply();
        }

        setStrictModeThreadPolicy();
        bind();
        initializeFragments();
        initializeBottomNavigation();
        showInitialFragment();
    }

    private void setStrictModeThreadPolicy() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void bind() {
        b = ActivityMainBinding.inflate(getLayoutInflater());
        View view = b.getRoot();
        setContentView(view);
    }

    private void initializeFragments() {
        if (jokesFragment == null) {
            jokesFragment = new JokesFragment();
        }
        if (webviewFragment == null) {
            webviewFragment = new WebviewFragment();
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void initializeBottomNavigation() {
        b.bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_item_joke: {
                    showFragment(jokesFragment);
                    return true;
                }
                case R.id.menu_item_web: {
                    showFragment(webviewFragment);
                    return true;
                }
            }
            return false;
        });
    }

    private void showFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(b.fragmentContainer.getId(), fragment)
                .commit();
    }

    private void showInitialFragment() {
        b.bottomNavigationView.setSelectedItemId(
                b.bottomNavigationView.getMenu()
                        .getItem(INITIAL_MENU_ITEM_INDEX)
                        .getItemId());
    }

    @Override
    public void onBackPressed() {
        int initialMenuItemId = b.bottomNavigationView.getMenu().getItem(INITIAL_MENU_ITEM_INDEX).getItemId();
        if (b.bottomNavigationView.getSelectedItemId() != initialMenuItemId) {
            showInitialFragment();
        } else {
            if (isAboutToClose) {
                sharedPreferences.edit().clear().apply();
                finish();
            } else {
                isAboutToClose = true;
                showMessage(getString(R.string.toast_press_back_again_to_quit));
                new Handler().postDelayed(() -> isAboutToClose = false, EXIT_ON_BACK_PRESS_WAITING_TIME);
            }
        }
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}