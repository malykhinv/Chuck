package com.malykhinv.chuck.mvp.view.fragments.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.malykhinv.chuck.R;
import com.malykhinv.chuck.databinding.ItemSingleJokeBinding;
import com.malykhinv.chuck.di.App;

import java.util.ArrayList;

public class JokesScrollAdapter extends RecyclerView.Adapter<JokesScrollAdapter.ViewHolder> {

    private final Context context = App.getAppComponent().getContext();
    public ArrayList<String> listOfJokes = new ArrayList<>();

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemSingleJokeBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull JokesScrollAdapter.ViewHolder holder, int position) {
        holder.updateJokeText(listOfJokes, position);
    }

    @Override
    public int getItemCount() {
        return listOfJokes.size();
    }

    public void setItems(ArrayList<String> listOfJokes) {
        this.listOfJokes = listOfJokes;
        notifyDataSetChanged();
    }

    public void clearItems() {
        this.listOfJokes.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemSingleJokeBinding b;
        private final Context context = App.getAppComponent().getContext();

        public ViewHolder(@NonNull ItemSingleJokeBinding b) {
            super(b.getRoot());
            this.b = b;
        }

        @SuppressLint("DefaultLocale")
        public void updateJokeText(ArrayList<String> listOfJokes, int position) {
            try {
                b.textNumberOfJoke.setText(String.format("%s%d", context.getResources().getString(R.string.joke_number), position + 1));
                b.textJoke.setText(listOfJokes.get(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
