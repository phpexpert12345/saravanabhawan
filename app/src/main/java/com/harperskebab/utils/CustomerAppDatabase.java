package com.harperskebab.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.harperskebab.model.CartDao;
import com.harperskebab.model.CartItem;
import com.harperskebab.model.ToppingDao;
import com.harperskebab.model.ToppingItems;

@Database(entities ={CartItem.class, ToppingItems.class}, version = 1)
public abstract class CustomerAppDatabase extends RoomDatabase {
    private static volatile CustomerAppDatabase INSTANCE;
    public abstract CartDao cartDao();
    public abstract ToppingDao toppingDao();
    public  static CustomerAppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CustomerAppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CustomerAppDatabase.class, "customer_database")
                            .addCallback(sRoomDatabaseCallback)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.

        }
    };

}
