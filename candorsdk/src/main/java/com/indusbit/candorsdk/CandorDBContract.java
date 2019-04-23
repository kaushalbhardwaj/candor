package com.indusbit.candorsdk;

public interface CandorDBContract {
    final String DATABASE_NAME = "candor_db";
    final int DATABASE_VERSION = 1;

    public interface ABVariantTable {
        final String TABLE_NAME = "abvariant";
        final String COLUMN_EXPERIMENTS_DATA = "experiments_data";
        final String COLUMN_USER_ID = "user_id";
    }

    public interface TrackEventsTable {
        final String TABLE_NAME = "track_event_table";
        final String COLUMN_EVENT_NAME = "event_name";
        final String COLUMN_EVENT_PROPERTIES = "event_properties";
    }

}
