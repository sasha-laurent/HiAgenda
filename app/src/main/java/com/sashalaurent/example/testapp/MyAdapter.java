package com.sashalaurent.example.testapp;

/**
 * Created by Sasha on 01/03/2017.
 */

import android.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements JSONAsyncTask.JSONConsumer {

    private JSONObject jsonObject = null;

    private final List<Pair<String, String>> characters = Arrays.asList(
            Pair.create("0:00", "Pas d'événement"),
            Pair.create("1:00", "Pas d'événement"),
            Pair.create("2:00", "Pas d'événement"),
            Pair.create("3:00", "Pas d'événement"),
            Pair.create("4:00", "Pas d'événement"),
            Pair.create("5:00", "Pas d'événement"),
            Pair.create("6:00", "Pas d'événement"),
            Pair.create("7:00", "Pas d'événement"),
            Pair.create("8:00", "Pas d'événement pour l'instant et ce texte est trop long mais quand on clique sur moi on peut m'afficher"),
            Pair.create("9:00", "Pas d'événement"),
            Pair.create("10:00", "Pas d'événement"),
            Pair.create("11:00", "Pas d'événement"),
            Pair.create("12:00", "Pas d'événement"),
            Pair.create("13:00", "Pas d'événement"),
            Pair.create("14:00", "Pas d'événement"),
            Pair.create("15:00", "Pas d'événement"),
            Pair.create("16:00", "Pas d'événement"),
            Pair.create("17:00", "Pas d'événement"),
            Pair.create("18:00", "Pas d'événement"),
            Pair.create("19:00", "Pas d'événement"),
            Pair.create("20:00", "Pas d'événement"),
            Pair.create("21:00", "Pas d'événement"),
            Pair.create("22:00", "Pas d'événement"),
            Pair.create("23:00", "Pas d'événement")
    );

    @Override
    public int getItemCount() {
        if(jsonObject != null){
            return characters.size();
        } else {
            return 0;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_cell, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            String eventHour = jsonObject.getString("hour");
            String event = jsonObject.getString("event");
            Pair<String, String> pair = characters.get(position);
            if(eventHour.equals(pair.first) ){
                pair = new Pair<String, String>(eventHour, event);
            }
            holder.display(pair);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setJSONObject(JSONObject object) {
        jsonObject = object;
        Log.i("json_object", jsonObject.toString());
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView hour;
        private final TextView event;

        private Pair<String, String> currentPair;

        public MyViewHolder(final View itemView) {
            super(itemView);

            hour = ((TextView) itemView.findViewById(R.id.hour));
            event = ((TextView) itemView.findViewById(R.id.event));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle(currentPair.first)
                            .setMessage(currentPair.second)
                            .show();
                }
            });
        }

        public void display(Pair<String, String> pair) {
            currentPair = pair;
            hour.setText(pair.first);
            event.setText(pair.second);
        }
    }

    public void setCharacters(int position, String description){
        characters.set(position, new Pair<String, String>(characters.get(position).first, description));
    }

    public String getDescription(int position){
        Pair event = characters.get(position);
        return (event.second).toString();
    }

}
