package com.example.mobilegroupsms.ui.groupcreate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mobilegroupsms.GroupModel;
import com.example.mobilegroupsms.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import io.grpc.Context;

public class GroupCreateFragment extends Fragment {

    EditText groupNameEditText, groupDescriptionEditText;
    ImageView groupImageImageView;
    Button groupCreateButton;
    RecyclerView groupRecyclerView;

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    FirebaseStorage mStorage;

    Uri filepath; //En son secilen resmin adresini tutan uri isimli bir degisken

    ArrayList<GroupModel> groupModelArrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_create, container, false); //on create viewde kod yazabilmek icin

        groupNameEditText=view.findViewById(R.id.groupcreate_groupNameEditText);
        groupDescriptionEditText=view.findViewById(R.id.groupcreate_groupDescriptionEditText);

        groupImageImageView=view.findViewById(R.id.groupcreate_groupImageImageView);

        groupCreateButton=view.findViewById(R.id.groupcreate_groupCreateButton);

        groupRecyclerView=view.findViewById(R.id.groupcreate_groupRecyclerView);

        groupModelArrayList=new ArrayList<>(); //Olusturdugumuz modeli dinamik olarak olusturdum.

        mAuth=FirebaseAuth.getInstance();
        mStore=FirebaseFirestore.getInstance();
        mStorage=FirebaseStorage.getInstance();

        //Eger sonuc basariliysa file pathi uri olani set et.
        ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == getActivity().RESULT_OK){
                        Intent data=result.getData();
                        filepath=data.getData();
                        groupImageImageView.setImageURI(filepath);
                    }
                }
        );
        groupImageImageView.setOnClickListener(v -> {
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(intent); //Yukaridaki set islemini cagiriyorum
        });

        groupCreateButton.setOnClickListener(v -> {
            String groupName=groupNameEditText.getText().toString();
            String groupDescription=groupDescriptionEditText.getText().toString();

            if(groupName.isEmpty()){
                groupNameEditText.setError("Grup Adı Zorunludur !");  //Grup ismini kontrol ediyor.Bossa doldurun diyor
                groupNameEditText.requestFocus();
                return;
            }
            if (groupDescription.isEmpty()){
                groupDescriptionEditText.setError("Grup Açıklaması Zorunludur !"); //Grup aciklamasini kontrol ediyor.Bossa doldurun diyor.
                groupDescriptionEditText.requestFocus();
                return;
            }
            if (filepath!=null){
                StorageReference storageReference=mStorage.getReference().child("gorsel/"+ UUID.randomUUID().toString());

                storageReference.putFile(filepath).addOnSuccessListener(taskSnapshot -> {
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl=uri.toString();

                        Toast.makeText(getContext(), "Grup Görseli Başarıyla Yüklendi !", Toast.LENGTH_SHORT).show();

                        CreateGroup(groupName,groupDescription,downloadUrl);

                    });
                });
                return;
            }else{
                CreateGroup(groupName,groupDescription,null); //Gorsel yoksa bos kalacak
            }
        });
        FetchGroup();
        return view;
    }
        //Grup olusturmak icin ortak bir private fonksiyon
        private void CreateGroup(String name, String description, String image){
        String userId=mAuth.getCurrentUser().getUid();

        mStore.collection("/userdata/"+userId+"/"+"groups").add(new HashMap<String, Object>(){{
            put("name",name);
            put("description",description);
            put("image",image);
            put("numbers",new ArrayList<String>());  //Numaralar grup olusturdugumuzda bos oldugu icin list vericem

            }}).addOnSuccessListener(documentReference -> {
            Toast.makeText(getContext(), "Mesaj Grubu Başarıyla Oluşturuldu !", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Mesaj Grubu Oluşturulamadı  !", Toast.LENGTH_SHORT).show();
        });
        }
        //Gruplari ceken fonksiyonumu olusturuyorum.
        private void FetchGroup(){
        String userId=mAuth.getCurrentUser().getUid(); //Oncelikle userid aliyor.
            mStore.collection("/userdata/"+userId+"/"+"groups").get().addOnSuccessListener(queryDocumentSnapshots -> {  //Userdata icinde useridleri her bir gruptan al.
                    groupModelArrayList.clear();    //Daha onceden olma ihtimaline karsi listeyi temizliyorum.
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                GroupModel groupModel=documentSnapshot.toObject(GroupModel.class);
                groupModelArrayList.add(groupModel);

                groupRecyclerView.setAdapter(new GroupAdapter(groupModelArrayList));
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);//Yeni olustur ve dikey olustur.
                groupRecyclerView.setLayoutManager(linearLayoutManager);
            }
            });
        }
}