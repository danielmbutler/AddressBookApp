package com.dbtechprojects.addressbookapp.utils;


import android.content.Context;

import androidx.room.Room;

import com.dbtechprojects.addressbookapp.data.ContactsDatabase;
import com.dbtechprojects.addressbookapp.models.ContactsDatabaseDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class AppModule {

    @Singleton
    @Provides
    public static ContactsDatabaseDao provideDb(ContactsDatabase contactsDatabase) {
        return contactsDatabase.getDao();
    }

    @Provides
    @Singleton
    public static ContactsDatabase provideContactsDb(@ApplicationContext Context context){
        return Room.databaseBuilder(context, ContactsDatabase.class, "contacts_database").build();
    }

}