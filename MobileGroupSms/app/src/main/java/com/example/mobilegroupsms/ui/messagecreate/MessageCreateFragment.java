package com.example.mobilegroupsms.ui.messagecreate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobilegroupsms.GroupModel;
import com.example.mobilegroupsms.MessageModel;
import com.example.mobilegroupsms.R;
import com.example.mobilegroupsms.ui.groupcreate.GroupAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageCreateFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;

    EditText messageNameEditText, messageDescriptionEditText;
    RecyclerView messageRecyclerView;
    Button messageCreateButton;

    ArrayList<MessageModel> messageModelArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_message, container, false);

        //viewleri idlere atiyorum.
        messageNameEditText = view.findViewById(R.id.messagecreate_messageNameEditText);
        messageDescriptionEditText = view.findViewById(R.id.messagecreate_messageDescriptionEditText);
        messageRecyclerView = view.findViewById(R.id.messagecreate_messageRecyclerView);
        messageCreateButton = view.findViewById(R.id.messagecreate_messageCreateButton);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        messageModelArrayList = new ArrayList<>(); //Olusturdugumuz modeli dinamik olarak olusturdum.

        messageCreateButton.setOnClickListener(v -> {
            String messageName = messageNameEditText.getText().toString();
            String messageDescription = messageDescriptionEditText.getText().toString();

            if (messageName.isEmpty() || messageDescription.isEmpty()) {  //eger mesaj adi veya icerigi bossa kontrolunu yapiyorum.
                Toast.makeText(getContext(), "Lütfen Mesaj Adı ve Mesaj İçeriğini Doldurun !", Toast.LENGTH_SHORT).show();
                return;
            }
            CreateMessage(messageName, messageDescription);
        });
        FetchMessage();
        return view;
    }

    private void CreateMessage(String messageName, String messageDescription) {
        String userId = mAuth.getCurrentUser().getUid();

        mStore.collection("/userdata/" + userId + "/" + "groups").add(new HashMap<String, String>() {
                    { //String disinda herhangi birsey kaydetmedigimiz icin tamamini string yapiyorum.
                        put("name", messageName);
                        put("description", messageDescription);
                    }
                })
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Mesaj Başarıyla Oluşturuldu !", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Mesaj Oluşturulamadı !", Toast.LENGTH_SHORT).show();
                });
    }

    //Uygulamamiz acildiginda mesajlari cekicek fonksiyondur.
    private void FetchMessage() {
        String userId = mAuth.getCurrentUser().getUid(); //Oncelikle userid aliyor.
        mStore.collection("/userdata/" + userId + "/" + "messages").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {  //Userdata icinde useridleri her bir mesaji al.
                    messageModelArrayList.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        MessageModel messageModel = new MessageModel(documentSnapshot.getString("name"), documentSnapshot.getString("description"), documentSnapshot.getId());
                        messageModelArrayList.add(messageModel);
                    }
                    messageRecyclerView.setAdapter(new MessageAdapter(messageModelArrayList));
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);//Yeni olustur ve dikey olustur.
                    messageRecyclerView.setLayoutManager(linearLayoutManager);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Mesaj Alınamadı !", Toast.LENGTH_SHORT).show();
                });
    }
}