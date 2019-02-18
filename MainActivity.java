package com.asimbongeni.asie.mygrmtranslator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView unsentTranslations, translatedWords, translatedSentences;
    private ImageView translate, viewStatistics, viewWork, customize;
    private int sumWordCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        unsentTranslations = (TextView) findViewById(R.id.textViewUnsent);
        translatedWords = (TextView) findViewById(R.id.textViewWordCount);
        translatedSentences = (TextView)findViewById(R.id.textViewSentenceCount);




        final DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        final List<Integer> wordCountList = databaseAccess.getAllTranslatedWordsCount();
        databaseAccess.close();
        sumWordCount = 0;
        for (int i = 0;i < wordCountList.size();i++ ){
            sumWordCount = wordCountList.get(i) + sumWordCount;}
        translatedWords.setText(String.format(getString(R.string.words_translated), sumWordCount));

        final int unsyncCounted = databaseAccess.getUnsyncCount();
        databaseAccess.close();
        unsentTranslations.setText(String.format(getString(R.string.sync_to_server), unsyncCounted));
        if (unsyncCounted > 8){
            unsentTranslations.setBackgroundColor(Color.parseColor("#FFFF5252"));
        }
        final int englishSentCount = databaseAccess.getEnglishTranslatedSentenceCount();
        databaseAccess.close();
        translatedSentences.setText(String.format(getString(R.string.sentence_translated), englishSentCount));

        translate = (ImageView) findViewById(R.id.imageViewTranslate);
        viewStatistics = (ImageView) findViewById(R.id.imageViewStatistics);
        viewWork =(ImageView) findViewById(R.id.imageViewTranslated);
        customize = (ImageView) findViewById(R.id.imageViewManage);

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                //LayoutInflater inflater = getLayoutInflater();
                builder.setTitle("Chapter to translate");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder.setIcon(getDrawable(R.drawable.logo_translate));
                }
                final EditText editText = new EditText(MainActivity.this);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(editText);


                //builder.setView(inflater.inflate(R.layout.dialog_chapterselect, null));

                builder.setPositiveButton("  Select  ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //mListener.onDialogPositiveClick(SelectSearchParameterFragment.this);
                        String chapter = editText.getText().toString();
                        SharedPreferences sharedPreferences = getSharedPreferences("TranslationOptions", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("chapterToTranslate", Integer.parseInt(chapter));
                        editor.apply();
                        Toast.makeText(MainActivity.this, "Chapter Selected: " + chapter, Toast.LENGTH_LONG).show();
                        Log.d("TAG","select chosen");
                        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.this);
                        try {
                            if (databaseAccess.getNdebeleSentenceCountByIsihloko(Integer.parseInt(chapter)) > 0
                                    &&  databaseAccess.getNdebeleSentenceCountByIsihloko(Integer.parseInt(chapter)) <= 669){
                                Intent intent = new Intent(MainActivity.this, DatabaseListActivity.class);
                                startActivity(intent);}
                            else {
                                Toast.makeText(MainActivity.this, "You have translated all of chapter " + chapter + ". Or its greater than 669" , Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception e){
                            Log.d("MAIN ACT",e.getMessage());
                            Toast.makeText(MainActivity.this, "Chapter should be a number", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //mListener.onDialogNegativeClick(SelectSearchParameterFragment.this);
                                // User cancelled the dialog
                                Toast.makeText(MainActivity.this, "Translation chapter from settings", Toast.LENGTH_LONG).show();
                            }
                        });
                // Create the AlertDialog object and show
                builder.create().show();
            }
        });

        customize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile_intent = new Intent(MainActivity.this, CustomizationActivity.class);
                startActivity(profile_intent);
            }
        });

        viewStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GraphViewActivity.class);
                startActivity(intent);
            }
        });

        viewWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewTranslatedActivity.class);
                startActivity(intent);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
