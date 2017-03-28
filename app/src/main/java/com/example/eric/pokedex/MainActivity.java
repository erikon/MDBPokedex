package com.example.eric.pokedex;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    PokedexAdapter pokedexAdapter;
    ArrayList<Pokedex.Pokemon> copyPokemons;
    ArrayList<Pokedex.Pokemon> filteredModelListType = new ArrayList<>();
    Pokedex pokedex;
    int mSelectedItem;
    boolean isGridView = false;
    private List<Pokedex.Pokemon> mModels = new ArrayList<>();
    private String[] typeArray = {"Clear Filter", "Normal", "Fire", "Water", "Fighting", "Flying", "Grass", "Poison", "Electric", "Ground", "Psychic", "Rock", "Ice", "Bug", "Dragon", "Ghost", "Dark", "Steel", "Fairy"};
    private ListView typerDrawer;
    private ArrayAdapter<String> typeAdapter;
    static int minHp = 0, minAttack = 0, minDefense = 0, random, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pokedex = new Pokedex();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        typerDrawer = (ListView) findViewById(R.id.navList);
        typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, typeArray);
        typerDrawer.setAdapter(typeAdapter);

        typerDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SparseBooleanArray clickedItemPositions = typerDrawer.getCheckedItemPositions();
                mSelectedItem = i;
                typeAdapter.notifyDataSetChanged();

                for(int j = 0;j < clickedItemPositions.size(); j ++){
                    boolean checked = clickedItemPositions.valueAt(j);

                    if(checked){
                        int key = clickedItemPositions.keyAt(j);
                        String item = (String) typerDrawer.getItemAtPosition(key);

                        if (item.equalsIgnoreCase("Clear Filter")){
                            for (int k = 0; k < clickedItemPositions.size(); k++){
                                typerDrawer.getChildAt(k).setBackgroundColor(Color.WHITE);
                            }
                            typerDrawer.setAdapter(typeAdapter);
                            pokedexAdapter.pokemons = copyPokemons;
                            pokedexAdapter.notifyDataSetChanged();
                            filteredModelListType.clear();

                        } else {
                            try {
                                boolean isInList = false;
                                typerDrawer.getChildAt(key).setBackgroundColor(Color.LTGRAY);
                                ArrayList<Pokedex.Pokemon> intermediateAdd = filterType(mModels, item);
                                for (int k = 0; k < intermediateAdd.size(); k++){
                                    Pokedex.Pokemon pokemon = intermediateAdd.get(k);
                                    for(int q = 0; q < filteredModelListType.size(); q++){
                                        if(pokemon == filteredModelListType.get(q)){
                                            isInList = true;
                                        }
                                    }
                                    if (isInList == false) {
                                        filteredModelListType.add(pokemon);
                                    }
                                    isInList = false;
                                }
                                pokedexAdapter.pokemons = filteredModelListType;
                                pokedexAdapter.notifyDataSetChanged();
                                recyclerView.scrollToPosition(0);
                            } catch (Exception e){

                            }


                        }

                    }
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        pokedexAdapter = new PokedexAdapter(getApplicationContext(), pokedex.getPokemon(), new Comparator<Pokedex.Pokemon>() {
            @Override
            public int compare(Pokedex.Pokemon pokemon, Pokedex.Pokemon t1) {
                return pokemon.name.compareTo(t1.name);
            }
        });
        copyPokemons = pokedexAdapter.pokemons;

        recyclerView.setAdapter(pokedexAdapter);

        mModels = new ArrayList<>();
        for (Pokedex.Pokemon k: pokedex.getPokemon()) {
            mModels.add(k);
        }
        pokedexAdapter.add(mModels);

        filterReturn();
    }

    public void filterReturn(){
        if (code == Utils.FILTER_STATS_CONSTANT) {
            int[]stats = new int[3];
            stats[0] = minHp;
            stats[1] = minAttack;
            stats[2] = minDefense;
            ArrayList<Pokedex.Pokemon> filteredModelList = filterStats(mModels, stats);
            pokedexAdapter.pokemons = filteredModelList;
            pokedexAdapter.notifyDataSetChanged();
            this.recyclerView.scrollToPosition(0);

        } else if (code == Utils.FILTER_RANDOM_CONSTANT){
            ArrayList<Pokedex.Pokemon> filteredModelList = filterRandom(mModels, random);
            pokedexAdapter.pokemons = filteredModelList;
            pokedexAdapter.notifyDataSetChanged();
            this.recyclerView.scrollToPosition(0);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchbar, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query){
        try {
            int x = Integer.parseInt(query);
            final ArrayList<Pokedex.Pokemon> filteredModelList = filterNum(mModels, x);
            pokedexAdapter.pokemons = filteredModelList;
            pokedexAdapter.notifyDataSetChanged();
            this.recyclerView.scrollToPosition(0);
            return true;

        } catch (Throwable e) {
            final ArrayList<Pokedex.Pokemon> filteredModelList = filterName(mModels, query);
            pokedexAdapter.pokemons = filteredModelList;
            pokedexAdapter.notifyDataSetChanged();
            this.recyclerView.scrollToPosition(0);
            return true;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query){
        return false;
    }

    private static ArrayList<Pokedex.Pokemon> filterName(List<Pokedex.Pokemon> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final ArrayList<Pokedex.Pokemon> filteredModelList = new ArrayList<>();
        for (Pokedex.Pokemon model : models) {
            final String text = model.name.toLowerCase();
            if (text.startsWith(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private static ArrayList<Pokedex.Pokemon> filterNum(List<Pokedex.Pokemon> models, int query) {
        String numberString = Integer.toString(query);
        final ArrayList<Pokedex.Pokemon> filteredModelList = new ArrayList<>();
        for (Pokedex.Pokemon model : models) {
            final String text = model.number;
            if (text.startsWith(numberString)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private ArrayList<Pokedex.Pokemon> filterType(List<Pokedex.Pokemon> models, String type) {
        final String lowercaseType = type.toLowerCase();
        ArrayList<Pokedex.Pokemon> filteredList = new ArrayList<>();
        for (Pokedex.Pokemon model : models) {
            final String[] typearr = model.types;
            for (int i = 0; i < typearr.length; i++){
                if (lowercaseType.equalsIgnoreCase(typearr[i])){
                    filteredList.add(model);
                }
            }
        }
        return filteredList;
    }

    private ArrayList<Pokedex.Pokemon> filterStats(List<Pokedex.Pokemon> models, int[]stats) {
        ArrayList<Pokedex.Pokemon> filteredList = new ArrayList<>();
        for (Pokedex.Pokemon model : models) {
            int modelHp = Integer.parseInt(model.hp);
            int modelAttack = Integer.parseInt(model.attack);
            int modelDefense = Integer.parseInt(model.defense);
            if(modelHp >= stats[0] && modelAttack >= stats[1] && modelDefense >= stats[2]){
                filteredList.add(model);
            }
        }
        return filteredList;
    }

    private ArrayList<Pokedex.Pokemon> filterRandom(List<Pokedex.Pokemon> models, int random) {
        ArrayList<Pokedex.Pokemon> randomList = new ArrayList<>();
        for (Pokedex.Pokemon model : models) {
            randomList.add(model);
        }
        Collections.shuffle(randomList);
        ArrayList<Pokedex.Pokemon> filteredList = new ArrayList<>();
        for(int i = 0; i < random; i ++){
            filteredList.add(randomList.get(i));
            Log.i("this is weird", randomList.get(i).name);
        }
        return filteredList;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_gridView:
                if (!isGridView){
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
                    pokedexAdapter.notifyDataSetChanged();
                    isGridView = true;

                } else {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    pokedexAdapter.notifyDataSetChanged();
                    isGridView = false;
                }
                return true;
            case R.id.action_search:
                return true;
            case R.id.action_filterView:
                Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
                Pokedex.Pokemon single_poke = filterRandom(mModels, 1).get(0);
                intent.putExtra(getString(R.string.rand_poke), single_poke.name);
                startActivity(intent);
            case R.id.action_slide:
                drawerLayout.openDrawer(Gravity.LEFT);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

}
