package com.asimbongeni.asie.mygrmtranslator;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class ExternalSQLDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "cvDataNine.sqlite";
    private static final int DATABASE_VERSION = 2;

    public ExternalSQLDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
