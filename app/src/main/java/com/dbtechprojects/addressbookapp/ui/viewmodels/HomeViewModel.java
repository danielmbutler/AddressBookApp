package com.dbtechprojects.addressbookapp.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dbtechprojects.addressbookapp.models.Contact;
import com.dbtechprojects.addressbookapp.models.ContactsDatabaseDao;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class HomeViewModel extends ViewModel {

    private final ContactsDatabaseDao contactsDatabaseDao;


    public LiveData<List<Contact>> getAllContacts() {
        return contactsDatabaseDao.getAllContacts();
    }

    @Inject
    public HomeViewModel(ContactsDatabaseDao dao) {
        this.contactsDatabaseDao = dao;
    }


    public void saveContact(Contact contact) {
        Completable.fromAction(() -> contactsDatabaseDao
                .insertContact(contact))
                .subscribeOn(Schedulers.io())
                .onErrorComplete()
                .subscribe();

    }
}
