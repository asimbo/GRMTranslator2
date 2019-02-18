package com.asimbongeni.asie.mygrmtranslator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.asimbongeni.asie.mygrmtranslator.Model.DatabaseAttributes;

import java.util.List;

public class ViewTranslatedActivity extends AppCompatActivity {
    RecyclerView mRecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_translated);

        mRecycleView = (RecyclerView) findViewById(R.id.translatedRecyclerView);

        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getApplicationContext());
        final List<DatabaseAttributes> dbAttributes = databaseAccess.getEnglishTranslatedSentenceAll();
        Log.d("TAG", "EngNumber: " + databaseAccess.getEnglishTranslatedSentenceCount());
        Log.d("TAG", "translated retrieve");
        RecycleViewAdapterTranslated adapter = new RecycleViewAdapterTranslated(this, dbAttributes);
        mRecycleView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleView.setHasFixedSize(true);

    }
}
