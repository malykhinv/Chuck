package com.malykhinv.chuck.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.malykhinv.chuck.databinding.FragmentWebviewBinding;

import org.jetbrains.annotations.NotNull;

public class WebviewFragment extends Fragment {

    private static final String DEFAULT_URL = "http://www.icndb.com/api/";
    private FragmentWebviewBinding b;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        b = FragmentWebviewBinding.inflate(inflater, container, false);

        if (view == null) {
            view = b.getRoot();
        } else if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }

        return view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        b.webView.getSettings().setJavaScriptEnabled(true);
        b.webView.setWebViewClient(new CustomWebViewClient());
        b.webView.loadUrl(DEFAULT_URL);
    }
}