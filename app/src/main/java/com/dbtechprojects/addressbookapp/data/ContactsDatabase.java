package com.dbtechprojects.addressbookapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dbtechprojects.addressbookapp.models.Contact;
import com.dbtechprojects.addressbookapp.models.ContactsDatabaseDao;

@Database(entities = Contact.class, version = 1, exportSchema = false)

public abstract class ContactsDatabase extends RoomDatabase {
    private static final String DB_NAME = "contacts_database";
    private static ContactsDatabase countriesDatabase;

    public static synchronized ContactsDatabase getContactsDatabase(Context context) {
        if (countriesDatabase == null) {
            countriesDatabase = Room.databaseBuilder(context.getApplicationContext(), ContactsDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return countriesDatabase;
    }

    public abstract ContactsDatabaseDao getDao();
}
