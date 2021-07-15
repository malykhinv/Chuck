package com.malykhinv.chuck.mvp.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.malykhinv.chuck.databinding.FragmentJokesBinding;
import com.malykhinv.chuck.mvp.presenter.JokesPresenter;
import com.malykhinv.chuck.mvp.view.fragments.adapters.JokesScrollAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class JokesFragment extends Fragment {

    private final JokesScrollAdapter jokesScrollAdapter = new JokesScrollAdapter();
    private FragmentJokesBinding b;
    private View view;
    private JokesPresenter presenter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentJokesBinding.inflate(inflater, container, false);

        b.fabReloadJokes.setOnClickListener(v -> presenter.onRefreshButtonWasClicked());

        if (view == null) {
            view = b.getRoot();
        } else if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (presenter == null) {
            presenter = new JokesPresenter(this);
        }

        presenter.onViewCreated();
    }

    public void initializeRecyclerView() {
        b.recyclerViewJokes.setLayoutManager(new LinearLayoutManager(this.getContext()));
        b.recyclerViewJokes.setAdapter(jokesScrollAdapter);
    }

    public void clearJokes() {
        jokesScrollAdapter.clearItems();
    }

    public void showJokes(ArrayList<String> listOfJokes) {
        jokesScrollAdapter.setItems(listOfJokes);
    }

    public Integer getCountOfJokes() {
        return Integer.parseInt(String.valueOf(b.editTextCountOfJokes.getText()));
    }

    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}