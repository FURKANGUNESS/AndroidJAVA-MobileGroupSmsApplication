package com.example.mobilegroupsms.ui.sendmessage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilegroupsms.MessageModel;
import com.example.mobilegroupsms.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    List<MessageModel>messageModelList; //Bir dizi mesaji alacagi icin list kullaniyorum.
    public MessageAdapter(List<MessageModel> messageModelList) {
        this.messageModelList = messageModelList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sendmessage_message,parent,false));
        //yeni bir tane messageview olusturmamiz gerekiyordu.
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageModel messageModel=messageModelList.get(position);
        holder.setData(messageModel);  //Bunun bdatasini cagiriyorum.
    }

    @Override
    public int getItemCount() {
        return messageModelList.size(); //Yukaridaki listeye gore item count olacak o yuzden size
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView, descriptionTextView;
        public MessageViewHolder(View itemview){
            super(itemview);

            nameTextView=itemview.findViewById(R.id.item_sendmessage_name_nameTextView);
            descriptionTextView=itemview.findViewById(R.id.item_sendmessage_message_descriptionTextView);
        }
        public void setData(MessageModel messageModel){
            nameTextView.setText(messageModel.getName());
            descriptionTextView.setText(messageModel.getDescription());
        }
    }
}
