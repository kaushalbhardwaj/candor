package com.indusbit.candorsdk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import androidx.annotation.Nullable;
import com.google.gson.Gson;

import java.io.File;

class CandorDBHelper extends SQLiteOpenHelper {

    private final String TAG = "CandorDBHelper";
    private final File databaseFile;
    private static ExperimentFetchedListener listener = null;

    private final String CREATE_AB_VARIANT_TABLE = "CREATE TABLE " + CandorDBContract.ABVariantTable.TABLE_NAME + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            CandorDBContract.ABVariantTable.COLUMN_EXPERIMENTS_DATA + " TEXT," +
            CandorDBContract.ABVariantTable.COLUMN_USER_ID + " TEXT)";

    private final String CREATE_TRACK_EVENT_TABLE = "CREATE TABLE " + CandorDBContract.TrackEventsTable.TABLE_NAME + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            CandorDBContract.TrackEventsTable.COLUMN_EVENT_NAME + " TEXT," +
            CandorDBContract.TrackEventsTable.COLUMN_EVENT_PROPERTIES + " TEXT)";

    private final String DELETE_AB_VARIANT_TABLE = "DROP TABLE IF EXISTS " + CandorDBContract.ABVariantTable.TABLE_NAME;
    private final String DELETE_TRACK_EVENT_TABLE = "DROP TABLE IF EXISTS " + CandorDBContract.TrackEventsTable.TABLE_NAME;

    public CandorDBHelper(Context context) {
        super(context, CandorDBContract.DATABASE_NAME, null, CandorDBContract.DATABASE_VERSION);
        databaseFile = context.getDatabasePath(CandorDBContract.DATABASE_NAME);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.v(TAG, "Creating a new db for Candor");

        db.execSQL(CREATE_AB_VARIANT_TABLE);
        db.execSQL(CREATE_TRACK_EVENT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_AB_VARIANT_TABLE);
        db.execSQL(DELETE_TRACK_EVENT_TABLE);
        onCreate(db);

    }

    public void saveExperiments(Context context, String experimentJson, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CandorDBContract.ABVariantTable.COLUMN_EXPERIMENTS_DATA, experimentJson);
        contentValues.put(CandorDBContract.ABVariantTable.COLUMN_USER_ID, userId);

        long id = db.insert(CandorDBContract.ABVariantTable.TABLE_NAME, null, contentValues);

        db.close();

        if (listener != null) {
            Log.d(TAG, "listener not null");
            listener.onExperimentFetched(new Gson().fromJson(experimentJson, Experiments.class));
        } else
            Log.d(TAG, "listener null");

    }

    public int deleteExperiments() {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(CandorDBContract.ABVariantTable.TABLE_NAME, null, null);
        Log.d(TAG, "database deleted");
        return deletedRows;

    }

    @Nullable
    public Experiments getExperiments(String userId) {
        String json = getConfigJson(userId);
        if (json == null)
            return null;

        return new Gson().fromJson(json, Experiments.class);
    }

    @Nullable
    public String getConfigJson(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = CandorDBContract.ABVariantTable.COLUMN_USER_ID + " = ?";
        String selectionArgs[] = {userId};

        Cursor cursor = db.query(CandorDBContract.ABVariantTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(CandorDBContract.ABVariantTable.COLUMN_EXPERIMENTS_DATA));
        } else {
            return null;
        }
    }

    public long saveEvent(Event event) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CandorDBContract.TrackEventsTable.COLUMN_EVENT_NAME, event.name);
        contentValues.put(CandorDBContract.TrackEventsTable.COLUMN_EVENT_PROPERTIES, event.properties.toString());

        long id = db.insert(CandorDBContract.TrackEventsTable.TABLE_NAME, null, contentValues);
        db.close();

        return id;

    }

    public int deleteEvents() {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(CandorDBContract.TrackEventsTable.TABLE_NAME, null, null);
        Log.d(TAG, "database deleted");
        return deletedRows;

    }

    public void setExperimentFetchedListener(ExperimentFetchedListener listener) {
        CandorDBHelper.listener = listener;
    }
}

