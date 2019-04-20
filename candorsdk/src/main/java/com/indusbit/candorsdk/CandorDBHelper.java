package com.indusbit.candorsdk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class CandorDBHelper extends SQLiteOpenHelper {

    private final String TAG = "CandroDBHelper";
    private final File databaseFile;

    private final String CREATE_AB_VARIANT_TABLE = "CREATE TABLE " + CandorDBContract.ABVariantTable.TABLE_NAME + " (" +
            BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            CandorDBContract.ABVariantTable.COLUMN_EXPERIMENT_KEY + " TEXT," +
            CandorDBContract.ABVariantTable.COLUMN_VARIANT_KEY + " TEXT," +
            CandorDBContract.ABVariantTable.COLUMN_VALUE_KEY + " TEXT," +
            CandorDBContract.ABVariantTable.COLUMN_VALUE_TYPE + " TEXT," +
            CandorDBContract.ABVariantTable.COLUMN_VALUE_VALUE + " TEXT)";

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

    public void saveExperiments(List<Experiment> experiments) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (Experiment experiment : experiments) {

            ContentValues contentValues = new ContentValues();

            contentValues.put(CandorDBContract.ABVariantTable.COLUMN_EXPERIMENT_KEY, experiment.key);
            contentValues.put(CandorDBContract.ABVariantTable.COLUMN_VARIANT_KEY, experiment.variant.key);
            contentValues.put(CandorDBContract.ABVariantTable.COLUMN_VALUE_KEY, "");
            contentValues.put(CandorDBContract.ABVariantTable.COLUMN_VALUE_TYPE, "");
            contentValues.put(CandorDBContract.ABVariantTable.COLUMN_VALUE_VALUE, "");

            long id = db.insert(CandorDBContract.ABVariantTable.TABLE_NAME, null, contentValues);

            /*if(experiment.variant.values != null) {

                for (Value value : experiment.variant.values) {
                    ContentValues contentValues = new ContentValues();

                    contentValues.put(CandorDBContract.ABVariantTable.COLUMN_EXPERIMENT_KEY, experiment.key);
                    contentValues.put(CandorDBContract.ABVariantTable.COLUMN_VARIANT_KEY, experiment.variant.key);
                    contentValues.put(CandorDBContract.ABVariantTable.COLUMN_VALUE_KEY, value.key);
                    contentValues.put(CandorDBContract.ABVariantTable.COLUMN_VALUE_TYPE, value.type);
                    contentValues.put(CandorDBContract.ABVariantTable.COLUMN_VALUE_VALUE, value.value);

                    long id = db.insert(CandorDBContract.ABVariantTable.TABLE_NAME, null, contentValues);

                }
            }*/
        }

        db.close();

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

    public Variant getVariant(String experimentKey) {

        SQLiteDatabase db = this.getReadableDatabase();
        List<Value> values = new ArrayList<Value>();

        String selection = CandorDBContract.ABVariantTable.COLUMN_EXPERIMENT_KEY + " = ?";
        String selectionArgs[] = {experimentKey};

        Cursor cursor = db.query(CandorDBContract.ABVariantTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);

        Variant variant = new Variant();

        while (cursor.moveToNext()) {
            Value value = new Value();
            value.key = cursor.getString(cursor.getColumnIndex(CandorDBContract.ABVariantTable.COLUMN_VALUE_KEY));
            value.type = cursor.getString(cursor.getColumnIndex(CandorDBContract.ABVariantTable.COLUMN_VALUE_TYPE));
            value.value = cursor.getString(cursor.getColumnIndex(CandorDBContract.ABVariantTable.COLUMN_VALUE_VALUE));
            values.add(value);

            variant.key = cursor.getString(cursor.getColumnIndex(CandorDBContract.ABVariantTable.COLUMN_VARIANT_KEY));

        }

        if (values.size() == 0)
            return null;

        variant.values = values;
        return variant;

    }

    List<Event> getEvents() throws JSONException {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(CandorDBContract.TrackEventsTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        List<Event> events = new ArrayList<Event>();

        while (cursor.moveToNext()) {
//            Event event = new Event();
//            event.name = cursor.getString(cursor.getColumnIndex(CandorDBContract.TrackEventsTable.COLUMN_EVENT_NAME));
//            event.properties = new JSONObject(cursor.getString(cursor.getColumnIndex(CandorDBContract.TrackEventsTable.COLUMN_EVENT_PROPERTIES)));
//            events.add(event);
        }

        cursor.close();
        return events;

    }

    public File getDatabaseFile() {
        return databaseFile;
    }
}

