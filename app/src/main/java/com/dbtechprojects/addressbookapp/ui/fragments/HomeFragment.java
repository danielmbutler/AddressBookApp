package com.dbtechprojects.addressbookapp.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.dbtechprojects.addressbookapp.databinding.FragmentHomeBinding;
import com.dbtechprojects.addressbookapp.models.Contact;
import com.dbtechprojects.addressbookapp.ui.dialogs.AddContactDialog;
import com.dbtechprojects.addressbookapp.ui.viewmodels.HomeViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment implements View.OnClickListener, AddContactDialog.SaveContact {

    private FragmentHomeBinding binding;
    private List<Contact> contacts;
    private HomeViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initObservers();
        binding.addContactButton.setOnClickListener(this);
    }

    private void initObservers() {
        viewModel.getAllContacts().observe(getViewLifecycleOwner(), contacts -> {
            if (contacts == null || contacts.isEmpty()){
                binding.placeholderText.setVisibility(View.VISIBLE);
            } else Log.d("contacts", contacts.toString());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        // would normally use a switch statement but we only have one option here
        if (v.getId() == binding.addContactButton.getId()) {
            // show add contact dialog
            showAddContactDialog();
        }
    }

    private void showAddContactDialog() {
        AddContactDialog dialogFragment = new AddContactDialog(this);
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        Fragment prev = getParentFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        dialogFragment.show(ft, "dialog");
    }

    @Override
    public void saveContact(Contact contact) {
        viewModel.saveContact(contact);
    }
}
