package com.theleafapps.pro.roomcontentprovider.data;

import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * The Room database.
 */
@Database(entities = {Menu.class}, version = 1)
public abstract class SampleDatabase extends RoomDatabase {

    /**
     * @return The DAO for the Menu table.
     */
    @SuppressWarnings("WeakerAccess")
    public abstract MenuDao menu();

    /** The only instance */
    private static SampleDatabase sInstance;

    /**
     * Gets the singleton instance of SampleDatabase.
     *
     * @param context The context.
     * @return The singleton instance of SampleDatabase.
     */
    public static synchronized SampleDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), SampleDatabase.class, "ex")
                    .build();
            sInstance.populateInitialData();
        }
        return sInstance;
    }

    /**
     * Switches the internal implementation with an empty in-memory database.
     *
     * @param context The context.
     */
    @VisibleForTesting
    public static void switchToInMemory(Context context) {
        sInstance = Room.inMemoryDatabaseBuilder(context.getApplicationContext(),
                SampleDatabase.class).build();
    }

    /**
     * Inserts the dummy data into the database if it is currently empty.
     */
    private void populateInitialData() {
        if (menu().count() == 0) {
            Menu menu = new Menu();
            beginTransaction();
            try {
                for (int i = 0; i < Menu.MENUS.length; i++) {
                    menu.name = Menu.MENUS[i];
                    menu().insert(menu);
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

}
