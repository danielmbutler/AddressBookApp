package com.dbtechprojects.addressbookapp.models;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
import io.reactivex.rxjava3.core.Completable;

@Dao
public interface ContactsDatabaseDao {
    @Query("SELECT * FROM contacts ORDER BY firstName ASC")
    LiveData<List<Contact>> getAllContacts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContact(Contact contact);

    @Delete
    Completable deleteContact(Contact contact);

}

