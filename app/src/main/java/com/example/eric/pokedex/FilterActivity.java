package com.example.eric.pokedex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class FilterActivity extends AppCompatActivity {

    EditText minHp;
    EditText minAttack;
    EditText minDefense;

    Button filterButton;
    Button getRandom;

    ImageView randomPoke;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        minHp = (EditText) findViewById(R.id.minHP);
        minAttack = (EditText) findViewById(R.id.minAttack);
        minDefense = (EditText) findViewById(R.id.minDefense);

        filterButton = (Button) findViewById(R.id.submit_filter);
        getRandom = (Button) findViewById(R.id.getRandomPokemon);

        randomPoke = (ImageView) findViewById(R.id.randomPokeImage);
        Intent intent = getIntent();
        String rand_poke_name = intent.getStringExtra("rand_poke");

        String url = "http://img.pokemondb.net/artwork/" + rand_poke_name.toLowerCase() + ".jpg";

        if (rand_poke_name.equalsIgnoreCase("Flabébé")){
            Glide.with(getApplicationContext()).load("http://img.pokemondb.net/artwork/flabebe.jpg").into(randomPoke);

        } else if (rand_poke_name.equalsIgnoreCase("Farfetch'd")){
            Glide.with(getApplicationContext()).load("http://img.pokemondb.net/artwork/farfetchd.jpg").into(randomPoke);

        } else {
            Glide.with(getApplicationContext()).load(url).into(randomPoke);
        }


        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                MainActivity.minHp = Integer.parseInt(minHp.getText().toString());
                MainActivity.minAttack = Integer.parseInt(minAttack.getText().toString());
                MainActivity.minDefense = Integer.parseInt(minDefense.getText().toString());
                MainActivity.code = 1;
                startActivity(intent);
            }
        });

        getRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                MainActivity.code = 2;
                MainActivity.random = 20;
                startActivity(intent);
            }
        });



    }
}
