package com.indusbit.candorsdk;

public interface CandorDBContract {
    final String DATABASE_NAME = "candor_db";
    final int DATABASE_VERSION = 1;

    public interface ABVariantTable {
        final String TABLE_NAME = "abvariant";
        final String COLUMN_EXPERIMENT_KEY = "experiment_key";
        final String COLUMN_VARIANT_KEY = "variant_key";
        final String COLUMN_VALUE_KEY = "value_key";
        final String COLUMN_VALUE_TYPE = "value_type";
        final String COLUMN_VALUE_VALUE = "value_value";
    }

    public interface TrackEventsTable {
        final String TABLE_NAME = "track_event_table";
        final String COLUMN_EVENT_NAME = "event_name";
        final String COLUMN_EVENT_PROPERTIES = "event_properties";
    }

}
