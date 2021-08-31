package com.dbtechprojects.addressbookapp.ui.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.dbtechprojects.addressbookapp.R;
import com.dbtechprojects.addressbookapp.databinding.FragmentHomeBinding;
import com.dbtechprojects.addressbookapp.models.Contact;
import com.dbtechprojects.addressbookapp.ui.adapters.ContactsAdapter;
import com.dbtechprojects.addressbookapp.ui.dialogs.AddContactDialog;
import com.dbtechprojects.addressbookapp.ui.viewmodels.HomeViewModel;
import com.dbtechprojects.addressbookapp.utils.Constants;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment
        implements
        View.OnClickListener,
        AddContactDialog.SaveContact,
        ContactsAdapter.onCallListener,
        ContactsAdapter.onDeleteListener,
        ContactsAdapter.onEditListener,
        ContactsAdapter.onVideoCallListener {

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        initObservers();
        binding.addContactButton.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initObservers() {
        viewModel.getAllContacts().observe(getViewLifecycleOwner(), contacts -> {
            if (contacts == null || contacts.isEmpty()) {
                binding.placeholderText.setVisibility(View.VISIBLE);
                adapter.setDataSet(new ArrayList<>());
            } else {
                if (adapter != null) {
                    adapter.setDataSet(contacts);
                    binding.placeholderText.setVisibility(View.GONE);
                }
            }
        });

        // observe messages from db events
        viewModel.dbMessages.observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Constants.showToast(message, requireContext());
            }
        });
    }

    private void setupRecyclerView() {
        adapter = new ContactsAdapter(this, this, this, this);
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
        dialogFragment.show(addContactDialogTransaction(), "dialog");
    }

    private FragmentTransaction addContactDialogTransaction() {

        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        Fragment prev = getParentFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        return ft;
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


        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> viewModel.deleteContact(contact));

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> alertDialog.dismiss());

        alertDialog.show();
    }

    @Override
    public void onCall(Contact contact) {
        //execute phone call
        executePhoneCall(contact);

    }

    private void executePhoneCall(Contact contact) {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CALL_PHONE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    // call number
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + contact.phone));
                    startActivity(callIntent);
                } else if (report.isAnyPermissionPermanentlyDenied()) {
                    // show permission denied message
                    Constants.showSettingsSnackBar(binding.getRoot(), requireActivity());
                }

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }


    @Override
    public void onEdit(Contact contact) {
        // pass contact to dialog
        Bundle bundle = new Bundle();
        bundle.putSerializable("edit", contact);
        AddContactDialog dialogFragment = new AddContactDialog(this);
        dialogFragment.setArguments(bundle);

        dialogFragment.show(addContactDialogTransaction(), "dialog");

    }

    @Override
    public void onVideoCall(Contact contact) {
        try {
            Intent videoCall = new Intent("com.android.phone.videocall");
            videoCall.putExtra("videocall", true);
            videoCall.setData(Uri.parse("tel:" + contact.phone));
            startActivity(videoCall);
        } catch (ActivityNotFoundException e) {
            Constants.showToast("no default video recorded found", requireActivity());
        }

    }
}
