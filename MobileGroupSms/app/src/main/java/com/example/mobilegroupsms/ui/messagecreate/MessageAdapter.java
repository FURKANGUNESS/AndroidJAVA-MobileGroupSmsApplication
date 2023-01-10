package com.example.mobilegroupsms.ui.messagecreate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilegroupsms.GroupModel;
import com.example.mobilegroupsms.MessageModel;
import com.example.mobilegroupsms.R;
import com.example.mobilegroupsms.ui.groupcreate.GroupAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    List<MessageModel> messageModelList;   //Bu adaptere verilen mesajlari tutan
    public MessageAdapter(ArrayList<MessageModel> messageAdapterList) {
        this.messageModelList=messageModelList;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessageAdapter.MessageViewHolder messageViewHolder=new MessageAdapter.MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messagecreate_message,parent,false));
        return messageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) { //Listeden pozisyo verisini alir.
        MessageModel messageModel=messageModelList.get(position);
        holder.setData(messageModel);
    }

    @Override
    public int getItemCount() {
        return messageModelList.size(); //itemcountda size kadar olucak.
    }
        public class MessageViewHolder extends RecyclerView.ViewHolder{ //Messageviewoldera view donduruyoruz.Gerekli verileri itema gore atamamiz gerekiyor.
            TextView messageNameTextView, messageDescriptionView;
            public MessageViewHolder(View itemView){
                super(itemView);
                messageNameTextView=itemView.findViewById(R.id.messagecreate_messageNameEditText);
                messageDescriptionView=itemView.findViewById(R.id.messagecreate_messageDescriptionEditText);
            }
            public void setData(MessageModel messageModel) {  //Yukaridaki itemlari gerekli sekillerde setlemem gerekiyor.
                messageNameTextView.setText(messageModel.getName());
                messageDescriptionView.setText(messageModel.getDescription());

            }
    }
}
