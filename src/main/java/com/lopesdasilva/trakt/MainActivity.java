package com.lopesdasilva.trakt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import com.jakewharton.trakt.ServiceManager;
import com.lopesdasilva.trakt.activities.EpisodeActivity;
import com.lopesdasilva.trakt.activities.MovieActivity;
import com.lopesdasilva.trakt.activities.SeasonsActivity;
import com.lopesdasilva.trakt.activities.ShowActivity;
import com.lopesdasilva.trakt.extras.UserChecker;

public class MainActivity extends FragmentActivity {

    String mUsername = "";
    String mPassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserChecker.saveUserAndPassword(this, mUsername, mPassword);


        findViewById(R.id.buttonEpisodeActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Launching Episode Activity", Toast.LENGTH_SHORT).show();
                Log.d("Trakt", "Launching Episode Activity");


                Bundle arguments = new Bundle();
                arguments.putString("show_imdb", "tt0944947");
                arguments.putInt("show_season", 3);
                arguments.putInt("show_episode", 7);
                Intent i = new Intent(getBaseContext(), EpisodeActivity.class);
                i.putExtras(arguments);

                startActivity(i);
                overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
            }
        });

        findViewById(R.id.buttonShowActivity).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Bundle arguments = new Bundle();
                arguments.putString("show_imdb", "tt0944947");

                Intent i = new Intent(getBaseContext(), ShowActivity.class);
                i.putExtras(arguments);

                startActivity(i);
                overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
            }
        });

        findViewById(R.id.buttonMovieActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Launching Movie Activity", Toast.LENGTH_SHORT).show();
                Log.d("Trakt Fragments", "Launching Movie Activity");


                Bundle arguments = new Bundle();
                arguments.putString("movie_imdb", "tt1228705");
                Intent i = new Intent(getBaseContext(), MovieActivity.class);
                i.putExtras(arguments);

                startActivity(i);
                overridePendingTransition(R.anim.push_left_out, R.anim.push_left_in);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
