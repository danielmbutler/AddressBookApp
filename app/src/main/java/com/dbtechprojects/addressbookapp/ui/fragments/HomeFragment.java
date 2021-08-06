package com.dbtechprojects.addressbookapp.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.dbtechprojects.addressbookapp.R;
import com.dbtechprojects.addressbookapp.databinding.FragmentHomeBinding;
import com.dbtechprojects.addressbookapp.models.Contact;
import com.dbtechprojects.addressbookapp.ui.adapters.ContactsAdapter;
import com.dbtechprojects.addressbookapp.ui.dialogs.AddContactDialog;
import com.dbtechprojects.addressbookapp.ui.viewmodels.HomeViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment
        implements
        View.OnClickListener,
        AddContactDialog.SaveContact,
        ContactsAdapter.onCallListener,
        ContactsAdapter.onDeleteListener
{

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private ContactsAdapter adapter;

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

        setupRecyclerView();
        initObservers();
        binding.addContactButton.setOnClickListener(this);
    }

    private void initObservers() {
        viewModel.getAllContacts().observe(getViewLifecycleOwner(), contacts -> {
            if (contacts == null || contacts.isEmpty()){
                binding.placeholderText.setVisibility(View.VISIBLE);
            } else{
                if (adapter != null){
                    adapter.setDataSet(contacts);
                    binding.placeholderText.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new ContactsAdapter(this, this);
        binding.contactsRecyclerView.setAdapter(adapter);

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

    @Override
    public void onDelete(Contact contact) {
        // show warning dialog
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
        alertDialog.setTitle("Delete Contact");
        alertDialog.setMessage(getString(R.string.contact_Delete_warning));


        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                viewModel.deleteContact(contact);
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onCall(Contact contact) {

    }
}
