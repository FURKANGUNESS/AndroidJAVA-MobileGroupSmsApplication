package com.example.mobilegroupsms.ui.addtogroup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobilegroupsms.GroupModel;
import com.example.mobilegroupsms.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder>{

    List<GroupModel> groupModelList;
    public GroupAdapter(List<GroupModel> groupModelList) {
        this.groupModelList=groupModelList;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //Yeni bir viewholder olusturmami istiyor.
       GroupViewHolder groupViewHolder=new GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_addtogroup_group,parent,false));
        return groupViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) { //Listeden grup modeli cektim.
        GroupModel groupModel=groupModelList.get(position);
        holder.setData(groupModel);
    }

    @Override
    public int getItemCount() {
        return groupModelList.size();
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder{ //Groupviewoldera view donduruyoruz.Gerekli verileri itema gore atamamiz gerekiyor.
        ImageView groupImageView;
        TextView groupNameTextView, groupDescriptionView;
        public GroupViewHolder(View itemView){
            super(itemView);
            groupImageView=itemView.findViewById(R.id.item_addtogroup_group_imageImageView);
            groupNameTextView=itemView.findViewById(R.id.item_addtogroup_group_nameTextView);
            groupDescriptionView=itemView.findViewById(R.id.item_addtogroup_group_descriptionTextView);
        }
        public void setData(GroupModel groupModel){  //Yukaridaki itemlari gerekli sekillerde setlemem gerekiyor.
            groupNameTextView.setText(groupModel.getName());
            groupDescriptionView.setText(groupModel.getDescription());

            if(groupModel.getImage() !=null){
                Picasso.get().load(groupModel.getImage()).into(groupImageView); //Bu uri deki image al ve groupimageviewa ata.
            }
        }
    }
}
