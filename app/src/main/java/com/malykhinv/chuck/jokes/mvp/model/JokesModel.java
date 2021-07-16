package com.malykhinv.chuck.jokes.mvp.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.malykhinv.chuck.di.App;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class JokesModel {

    private static final String URL_TO_PARSE = "https://anekdot-book.com/podborka/anekdoty-pro-chak-norrisa";
    private static final String USER_AGENT = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:89.0) Gecko/20100101 Firefox/89.0";
    private static final String URL_REFERRER = "https://yandex.ru/";
    private static final String HTML_TAG_TO_SELECT = "div";
    private static final String HTML_ATTRIBUTE_VALUE = "ads-color-box";
    private static final String APP_PREFERENCES = "DATA";
    private static final String DEFAULT_SP_VALUE = "empty";
    private final Context context = App.getAppComponent().getContext();
    private final SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    private final ArrayList<String> listOfJokes = new ArrayList<>();
    private Callback callback;

    public interface Callback {
        void onListOfJokesReceived(ArrayList<String> listOfJokes);
        void onError(String message);
    }

    public void registerCallback(Callback callback) {
        this.callback = callback;
    }


    // Web

    public void observeJokes(int countOfJokes) {

        CompositeDisposable compositeDisposable = new CompositeDisposable();

        Observable<ArrayList<String>> observable = Observable
                .fromCallable(() -> loadListOfJokesFromWebsite(countOfJokes))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<ArrayList<String>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull ArrayList<String> listOfJokes) {
                if (callback != null) {
                    callback.onListOfJokesReceived(listOfJokes);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (callback != null) {
                    callback.onError(e.getMessage());
                }
            }

            @Override
            public void onComplete() {
                compositeDisposable.dispose();
            }
        });
    }

    private ArrayList<String> loadListOfJokesFromWebsite(int countOfJokes) {
        try {
            final Document doc = Jsoup.connect(URL_TO_PARSE)
                    .userAgent(USER_AGENT)
                    .referrer(URL_REFERRER)
                    .get();

            final Elements docElements = doc.select(HTML_TAG_TO_SELECT + "." + HTML_ATTRIBUTE_VALUE);

            int limitOfJokes = Math.min(countOfJokes, docElements.size());

            for (int i = 0; i < limitOfJokes; i++) {
                String joke = docElements.get(i).text();
                listOfJokes.add(joke);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listOfJokes;
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
