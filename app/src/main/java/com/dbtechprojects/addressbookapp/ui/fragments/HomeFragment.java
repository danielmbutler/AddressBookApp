package com.dbtechprojects.addressbookapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.dbtechprojects.addressbookapp.databinding.FragmentHomeBinding;
import com.dbtechprojects.addressbookapp.models.Contact;
import com.dbtechprojects.addressbookapp.ui.dialogs.AddContactDialog;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private List<Contact> contacts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (contacts == null){
            binding.placeholderText.setVisibility(View.VISIBLE);
        }
        binding.addContactButton.setOnClickListener(this);
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
        AddContactDialog dialogFragment = new AddContactDialog();
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        Fragment prev = getParentFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        dialogFragment.show(ft, "dialog");
    }
}
