package com.malykhinv.chuck.jokes.mvp.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.malykhinv.chuck.api.ApiResponse;
import com.malykhinv.chuck.api.IcndbApi;
import com.malykhinv.chuck.api.Value;
import com.malykhinv.chuck.di.App;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class JokesModel {

    private static final String QUOTE_CODE = "&quot;";
    private static final String QUOTE_SIGN = "\"";
    private static final String APP_PREFERENCES = "DATA";
    private static final String DEFAULT_SP_VALUE = "empty";
    private final Context context = App.getAppComponent().getContext();
    private final SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    private final IcndbApi api = App.getAppComponent().getApi();
    private Callback callback;

    public interface Callback {
        void onListOfJokesReceived(ArrayList<String> listOfJokes);
        void onError(String message);
    }

    public void registerCallback(Callback callback) {
        this.callback = callback;
    }


    // Web

    public void loadJokes(int countOfJokes) {

        api.getJokes(countOfJokes)
                .enqueue(new retrofit2.Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (callback != null) {
                            ArrayList<String> listOfJokes = new ArrayList<>();

                            List<Value> values = response.body().getValue();
                            for (Value value : values) {
                                String joke = prepareQuotes(value.getJoke());
                                listOfJokes.add(joke);
                            }

                            callback.onListOfJokesReceived(listOfJokes);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        if (callback != null) {
                            callback.onError(t.getMessage());
                        }
                    }

                    private String prepareQuotes(String joke) {
                        if (joke.contains(QUOTE_CODE)) {
                            return joke.replaceAll(QUOTE_CODE, QUOTE_SIGN);
                        } else {
                            return joke;
                        }
                    }
                });
    }


    // Internal storage

    public boolean hasStoredJokes() {
        return !sharedPreferences.getAll().isEmpty();
    }

    public void writeListOfJokesOnMemory(ArrayList<String> listOfJokes) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < listOfJokes.size(); i++) {
            editor.putString(String.valueOf(i), listOfJokes.get(i));
        }
        editor.apply();
    }

    public ArrayList<String> readListOfJokesFromMemory() {
        ArrayList<String> listOfJokes = new ArrayList<>();

        for (int i = 0; i < sharedPreferences.getAll().size(); i++) {
            if (sharedPreferences.contains(String.valueOf(i))) {
                listOfJokes.add(sharedPreferences.getString(String.valueOf(i), DEFAULT_SP_VALUE));
            }
        }

        return listOfJokes;
    }
}
