package com.example.mobilegroupsms.ui.addtogroup;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilegroupsms.ContactListModel;
import com.example.mobilegroupsms.R;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactListViewHolder> {
    List<ContactListModel> contactListModelList;

    public ContactListAdapter(List<ContactListModel> contactListModelList) {
        this.contactListModelList = contactListModelList;

    }

    public ContactListAdapter(ArrayList<ContactListModel> contactListModelList, Object o) {
    }

    @NonNull
    @Override
    public ContactListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addtogroup_contactlist, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListViewHolder holder, int position) {
        ContactListModel contactListModel = contactListModelList.get(position);
        holder.setData(contactListModel);
    }

    @Override
    public int getItemCount() {
        return contactListModelList.size();
    }

    public class ContactListViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView, phoneTextView;

        public ContactListViewHolder(View view) {
            super(view);

            imageView = view.findViewById(R.id.item_addtogroup_contacList_imageImageView);
            nameTextView = view.findViewById(R.id.item_addtogroup_contacList_nameTextView);
            phoneTextView = view.findViewById(R.id.item_addtogroup_contacList_phoneTextView);

        }

        public void setData(ContactListModel contactListModel) {
            nameTextView.setText(contactListModel.getName());
            phoneTextView.setText(contactListModel.getNumber());

            if (contactListModel.getImage() != null) {
                imageView.setImageURI(Uri.parse(contactListModel.getImage())); //Donusturmemiz gerekiyor.
            }
        }

    }
}

