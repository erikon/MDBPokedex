package com.example.eric.pokedex;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.R.attr.type;

public class PokemonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        Intent intent = getIntent();
        ImageView pokeImage = (ImageView) findViewById(R.id.pokeImage);
        TextView pokeName = (TextView) findViewById(R.id.name);
        TextView pokeNumber = (TextView) findViewById(R.id.number);
        TextView pokeHp = (TextView) findViewById(R.id.hp);
        TextView pokeAttack = (TextView) findViewById(R.id.attack);
        TextView pokeDefense = (TextView) findViewById(R.id.defense);
        TextView pokeSpecies = (TextView) findViewById(R.id.species);
        TextView pokeTypes = (TextView) findViewById(R.id.types);
        Button webButton = (Button) findViewById(R.id.webSearch);

        final String name = intent.getStringExtra("name");
        String number = intent.getStringExtra("number");
        String hp = intent.getStringExtra("hp");
        String attack = intent.getStringExtra("attack");
        String defense = intent.getStringExtra("defense");
        String species = intent.getStringExtra("species");
        String[] types = intent.getStringArrayExtra("type");

        StringBuilder sb = new StringBuilder();
        if(types.length == 2){
            sb.append(types[0] + ", " + types[1]);
        } else {
            sb.append(types[0]);
        }

        String url = "http://img.pokemondb.net/artwork/" + name.toLowerCase() + ".jpg";
        Glide.with(getApplicationContext()).load(url).into(pokeImage);

        if (name.equalsIgnoreCase("Flabébé")){
            Glide.with(getApplicationContext()).load("http://img.pokemondb.net/artwork/flabebe.jpg").into(pokeImage);
        } else if (name.equalsIgnoreCase("Farfetch'd")){
            Glide.with(getApplicationContext()).load("http://img.pokemondb.net/artwork/farfetchd.jpg").into(pokeImage);
        } else {
            Glide.with(getApplicationContext()).load(url).into(pokeImage);
        }

        pokeName.setText(name);
        pokeNumber.setText("# "  +number);
        pokeHp.setText("HP: " +hp);
        pokeAttack.setText("Attack Points: " + attack);
        pokeDefense.setText("Defense Points: " + defense);
        pokeSpecies.setText("Species: " + species);
        pokeTypes.setText("Type: " + sb.toString());

        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "http://www.google.com/#q=" + name;
                Uri url = Uri.parse(str);
                Intent intent1 = new Intent(Intent.ACTION_VIEW, url);
                startActivity(intent1);
            }
        });

    }
}
