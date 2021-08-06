package com.dbtechprojects.addressbookapp.ui.adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.dbtechprojects.addressbookapp.databinding.PhoneNumberItemBinding;
import com.dbtechprojects.addressbookapp.models.Contact;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.RVViewHolder> {
    private List<Contact> dataSet = new ArrayList<>();
    private final onDeleteListener deleteListener;
    private final onCallListener callListener;
    private final onEditListener editListener;

    public ContactsAdapter(
            onDeleteListener deleteListener,
            onCallListener callListener,
            onEditListener editListener
    ) {
        this.deleteListener = deleteListener;
        this.callListener = callListener;
        this.editListener =editListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setDataSet(List<Contact> value) {
        dataSet = value;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RVViewHolder.getViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RVViewHolder holder, int position) {
        holder.bind(dataSet.get(position), deleteListener, callListener, editListener);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class RVViewHolder extends RecyclerView.ViewHolder {
        public PhoneNumberItemBinding binding;

        RVViewHolder(@NonNull PhoneNumberItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public static RVViewHolder getViewHolder(ViewGroup parent) {
            PhoneNumberItemBinding binding = PhoneNumberItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new RVViewHolder(binding);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Contact currentItem, onDeleteListener deleteListener, onCallListener onCallListener, onEditListener onEditListener) {
            binding.rvItemTvName.setText(currentItem.firstName + " " + currentItem.lastName);
            binding.rvItemDelete.setOnClickListener(v -> deleteListener.onDelete(currentItem));
            binding.rvItemIvphone.setOnClickListener(v -> onCallListener.onCall(currentItem));
            binding.rvItemEdit.setOnClickListener(v -> onEditListener.onEdit(currentItem));
        }

    }

    public interface onDeleteListener{
        void onDelete(Contact contact);
    }

    public interface onCallListener{
        void onCall(Contact contact);
    }

    public interface onEditListener{
        void onEdit(Contact contact);
    }
}
