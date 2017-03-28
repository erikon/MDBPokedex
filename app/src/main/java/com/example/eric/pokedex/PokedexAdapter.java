package com.example.eric.pokedex;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by eric on 2/11/17.
 */

public class PokedexAdapter extends RecyclerView.Adapter<PokedexAdapter.CustomViewHolder> {

    private final SortedList<Pokedex.Pokemon> mSortedList = new SortedList<>(Pokedex.Pokemon.class, new SortedList.Callback<Pokedex.Pokemon>() {
        @Override
        public int compare(Pokedex.Pokemon a, Pokedex.Pokemon b) {
            return mComparator.compare(a, b);
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(Pokedex.Pokemon oldItem, Pokedex.Pokemon newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(Pokedex.Pokemon item1, Pokedex.Pokemon item2) {
            return item1 == item2;
        }
    });

    private final Comparator<Pokedex.Pokemon> mComparator;
    Context context;
    ArrayList<Pokedex.Pokemon> pokemons;

    public PokedexAdapter(Context context, ArrayList<Pokedex.Pokemon> pokemons, Comparator<Pokedex.Pokemon> comparator) {
        this.context = context;
        this.pokemons = pokemons;
        mComparator = comparator;
    }

    @Override
    public PokedexAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PokedexAdapter.CustomViewHolder holder, int position) {
        final Pokedex.Pokemon poke = pokemons.get(position);

//        class DownloadFilesTask extends AsyncTask<String, Void, Bitmap> {
//            protected Bitmap doInBackground(String... strings) {
//                try {
//                    return Glide.
//                            with(context).
//                            load(strings[0]).
//                            asBitmap().
//                            into(100, 100). // Width and height
//                            get();
//                }
//                catch (Exception e) {
//                    return null;
//                }
//            }
//
//            protected void onProgressUpdate(Void... progress) {}
//
//            protected void onPostExecute(Bitmap result) {
//                holder.pokemonImage.setImageBitmap(result);
//            }
//        }

        AsyncTask<Void, Void, Bitmap> asyncTask = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
//                Display display = getWindowManager().getDefaultDisplay();
                int width = 100;
                int height = 100;
                Bitmap bit = null;
                try {
                    String url = "http://img.pokemondb.net/artwork/" + poke.name.toLowerCase() + ".jpg";
                    if (poke.name.equalsIgnoreCase("Flabébé")){
//                        Glide.with(context).load("http://img.pokemondb.net/artwork/flabebe.jpg").into(holder.pokemonImage);
//                        new DownloadFilesTask().execute("http://img.pokemondb.net/artwork/flabebe.jpg");
                        bit = BitmapFactory.decodeStream((InputStream) new URL("http://img.pokemondb.net/artwork/flabebe.jpg").getContent());

                    } else if (poke.name.equalsIgnoreCase("Farfetch'd")){
//                        Glide.with(context).load("http://img.pokemondb.net/artwork/farfetchd.jpg").into(holder.pokemonImage);
//                        new DownloadFilesTask().execute("http://img.pokemondb.net/artwork/farfetchd.jpg");
                        bit = BitmapFactory.decodeStream((InputStream) new URL("http://img.pokemondb.net/artwork/farfetchd.jpg").getContent());

                    } else {
//                        Glide.with(context).load(url).into(holder.pokemonImage);
//                        new DownloadFilesTask().execute(url);
                        bit = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());

                    }
                } catch (Exception e) {}
                Bitmap sc = Bitmap.createScaledBitmap(bit,width,height,true);
                return sc;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                holder.pokemonImage.setImageBitmap(bitmap);
            }
        };

        asyncTask.execute();

        holder.pokemonName.setText(poke.name);

        holder.pokemonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PokemonActivity.class);
                intent.putExtra("name", poke.name);
                intent.putExtra("number", poke.number);
                intent.putExtra("hp", poke.hp);
                intent.putExtra("attack", poke.attack);
                intent.putExtra("defense", poke.defense);
                intent.putExtra("species", poke.species);
                intent.putExtra("type", poke.types);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pokemons.size();
    }

    public void add(Pokedex.Pokemon model) {
        mSortedList.add(model);
    }

    public void remove(Pokedex.Pokemon model) {
        mSortedList.remove(model);
    }

    public void add(List<Pokedex.Pokemon> models) {
        mSortedList.addAll(models);
    }

    public void remove(List<Pokedex.Pokemon> models) {
        mSortedList.beginBatchedUpdates();
        for (Pokedex.Pokemon model : models) {
            mSortedList.remove(model);
        }
        mSortedList.endBatchedUpdates();
    }

    public void replaceAll(List<Pokedex.Pokemon> models) {

        mSortedList.beginBatchedUpdates();
        for (int i = mSortedList.size() - 1; i >= 0; i--) {
            final Pokedex.Pokemon model = mSortedList.get(i);
            if (!models.contains(model)) {
                mSortedList.remove(model);
            }
        }
        mSortedList.addAll(models);
        mSortedList.endBatchedUpdates();

        pokemons = new ArrayList<Pokedex.Pokemon>();

        for (int i = 0; i < mSortedList.size(); i++) {
            pokemons.add(i, mSortedList.get(i));
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView pokemonName;
        ImageView pokemonImage;

        public CustomViewHolder(View view){
            super(view);

            pokemonName = (TextView) view.findViewById(R.id.pokemonName);
            pokemonImage = (ImageView) view.findViewById(R.id.pokemonImage);
        }
    }
}


