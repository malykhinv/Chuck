package com.malykhinv.chuck.jokes.mvp.presenter;

import android.content.Context;

import com.malykhinv.chuck.R;
import com.malykhinv.chuck.di.App;
import com.malykhinv.chuck.jokes.mvp.model.JokesModel;
import com.malykhinv.chuck.jokes.mvp.view.JokesFragment;

import java.util.ArrayList;

public class JokesPresenter implements JokesModel.Callback {

    private static final int MIN_COUNT_OF_JOKES = 1;
    private static final int MAX_COUNT_OF_JOKES = 50;
    private final Context context = App.getAppComponent().getContext();
    private final JokesFragment view;
    private JokesModel model;

    public JokesPresenter(JokesFragment view) {
        this.view = view;
        if (model == null) {
            this.model = new JokesModel();
        }

        model.registerCallback(this);
    }


    // Call from View:

    public void onViewCreated() {
        view.hideKeyboard();
        view.initializeRecyclerView();

        if (model.hasStoredJokes()) {
            ArrayList<String> listOfJokes = model.readListOfJokesFromMemory();
            view.showJokes(listOfJokes);
        }
    }

    public void onRefreshButtonWasClicked() {
        try {
            int countOfJokes = view.getCountOfJokes();

            if (isCorrectCountOfJokes(countOfJokes)) {
                view.setLoadingTextVisibility(true);
                view.hideKeyboard();
                view.clearJokes();
                model.observeJokes(countOfJokes);
            } else {
                view.setLoadingTextVisibility(false);
                view.showMessage(context.getResources().getString(R.string.toast_incorrect_number));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isCorrectCountOfJokes(int countOfJokes) {
        return countOfJokes >= MIN_COUNT_OF_JOKES && countOfJokes <= MAX_COUNT_OF_JOKES;
    }


    // Call from Model:

    @Override
    public void onListOfJokesReceived(ArrayList<String> listOfJokes) {
        model.writeListOfJokesOnMemory(listOfJokes);
        view.setLoadingTextVisibility(false);
        view.showJokes(listOfJokes);
    }

    @Override
    public void onError(String message) {
        view.showMessage(message);
    }
}
