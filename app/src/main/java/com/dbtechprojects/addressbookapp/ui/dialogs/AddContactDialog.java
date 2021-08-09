package com.dbtechprojects.addressbookapp.ui.dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.dbtechprojects.addressbookapp.R;
import com.dbtechprojects.addressbookapp.databinding.DialogAddContactBinding;
import com.dbtechprojects.addressbookapp.models.Contact;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class AddContactDialog extends DialogFragment {

    private DialogAddContactBinding binding;
    private Contact editContact;
    List<TextInputLayout> errorTextFields;
    SaveContact saveListener;

    public AddContactDialog(SaveContact saveListener){
        this.saveListener = saveListener;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogAddContactBinding.inflate(inflater);
        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.Custom_Dialog);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupClickListeners();
        errorTextFields = new ArrayList<>();
        if (getArguments() != null && getArguments().getSerializable("edit") != null){
            // user is in edit mode
            setupEditMode();
        }

    }

    private void setupEditMode() {
        binding.SaveButton.setText(R.string.update);
        binding.AddContactDialogTitle.setText(R.string.update_contact);
        Contact contact = (Contact) getArguments().getSerializable("edit");
        editContact = contact;
        if (contact.firstName != null) binding.textFieldFirstName.getEditText().setText(contact.firstName);
        if (contact.lastName != null) binding.textFieldLastName.getEditText().setText(contact.lastName);
        if (contact.email != null) binding.textFieldEmail.getEditText().setText(contact.email);
        if (contact.phone != null) binding.editextPhonenumber.getEditText().setText(contact.phone);
        if (contact.postcode != null) binding.textFieldPostCode.getEditText().setText(contact.postcode);
        if (contact.city != null) binding.textfieldCity.getEditText().setText(contact.city);
        if (contact.address != null) binding.textFieldAddress.getEditText().setText(contact.address);
        if (contact.dob != null) binding.textFieldDatePickerLayout.getEditText().setText(contact.dob);
    }

    private void setupClickListeners() {
        binding.cancelButton.setOnClickListener(v -> this.dismiss());
        binding.SaveButton.setOnClickListener(v -> validateFields());
        binding.datePickerText.setOnClickListener(v -> openDatePicker());
    }

    private void validateFields() {
        // email, first name, last name and phone number email are required fields
        checkForAndRemoveErrors();
        if (Objects.requireNonNull(Objects.requireNonNull(binding.textFieldFirstName.getEditText()).getText().toString()).length() < 2) {
            binding.textFieldFirstName.setError(getString(R.string.validate_firstName_Error));
            errorTextFields.add(binding.textFieldFirstName);
            return;
        }
        if (Objects.requireNonNull(Objects.requireNonNull(binding.textFieldLastName.getEditText()).getText().toString()).length() < 2) {
            binding.textFieldLastName.setError(getString(R.string.validate_lastname_Error));
            errorTextFields.add(binding.textFieldLastName);
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Objects.requireNonNull(binding.textFieldEmail.getEditText()).getText()).matches()) {
            binding.textFieldEmail.setError(getString(R.string.validate_email_Error));
            errorTextFields.add(binding.textFieldEmail);
            return;
        }
        if (!Patterns.PHONE.matcher(Objects.requireNonNull(binding.editextPhonenumber.getEditText()).getText()).matches()) {
            binding.editextPhonenumber.setError(getString(R.string.validate_phone_Error));
            errorTextFields.add(binding.editextPhonenumber);
            return;
        }

        String uid;
        if (editContact != null){
            uid = editContact.id;
        } else uid = UUID.randomUUID().toString();
        Contact contact = new Contact(
                uid,
                binding.textFieldFirstName.getEditText().getText().toString(),
                binding.textFieldLastName.getEditText().getText().toString(),
                binding.textFieldEmail.getEditText().getText().toString(),
                binding.editextPhonenumber.getEditText().getText().toString(),
                Objects.requireNonNull(binding.textFieldAddress.getEditText()).getText().toString(),
                Objects.requireNonNull(binding.textfieldCity.getEditText()).getText().toString(),
                Objects.requireNonNull(binding.textFieldPostCode.getEditText()).getText().toString(),
                Objects.requireNonNull(binding.textFieldDatePickerLayout.getEditText()).getText().toString()
        );

        saveListener.saveContact(contact);
        this.dismiss();

    }

    private void openDatePicker() {
        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        materialDateBuilder.setTitleText("SELECT A DOB");

        final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();
        materialDatePicker.show(getChildFragmentManager(), "picker");

        materialDatePicker.addOnPositiveButtonClickListener(selection ->
                Objects.requireNonNull(binding.textFieldDatePickerLayout.getEditText()).setText(
                        materialDatePicker.getHeaderText())
        );

    }

    /*
     loop through any text fields which have an error and then remove the error and
     text field from list.
     */
    private void checkForAndRemoveErrors() {
        if (!errorTextFields.isEmpty()) {
            for (TextInputLayout field : errorTextFields) {
                field.setError(null);
                errorTextFields.remove(field);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public interface SaveContact {
        void saveContact(Contact contact);
    }

}
