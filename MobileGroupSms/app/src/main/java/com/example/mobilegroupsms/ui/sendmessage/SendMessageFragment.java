package com.example.mobilegroupsms.ui.sendmessage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilegroupsms.GroupModel;
import com.example.mobilegroupsms.MessageModel;
import com.example.mobilegroupsms.R;
import com.example.mobilegroupsms.ui.addtogroup.GroupAdapter;
import com.example.mobilegroupsms.ui.messagecreate.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SendMessageFragment extends Fragment {

    //Gerekli viewlerimizi tanimliyorum.
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;

    RecyclerView groupsRecyclerView, messagesRecyclerView;
    TextView selectedGroupTextView, selectedMessageTextView;
    Button sendButton;

    //Mesaj olustur ve gruba olustur kisimlarinda kullandigimiz liste yapilarina ihtiyac duyuyorum.

    ArrayList<GroupModel> groupModelList;
    ArrayList<MessageModel> messageModelList; //Mesajlari tutmamiz gerekiyor.

    GroupModel selectedGroupModel;
    MessageModel selectedMessageModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_message, container, false);

        //viewleri idlere atiyorum.
        selectedGroupTextView = view.findViewById(R.id.sendmessage_selectedGroupsTextView);
        groupsRecyclerView = view.findViewById(R.id.sendmessage_groupsRecyclerView);
        messagesRecyclerView = view.findViewById(R.id.sendmessage_messagesRecyclerView);
        sendButton = view.findViewById(R.id.sendmessage_sendButton);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        groupModelList = new ArrayList<>();
        messageModelList = new ArrayList<>();

            sendButton.setOnClickListener(v -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS},100); //eger okuma iznimiz varsa launcherla baslaticaz.
                } else {
                    BulkMessage();
                }
            });
        FetchGroup();
        FetchMessage();
        return view;
    }
    private void FetchGroup(){
        String uid=mAuth.getCurrentUser().getUid();
        mStore.collection("/userdata/" + uid + "/" + "groups").get().addOnCompleteListener(queryDocumentSnapshot -> {
            if(queryDocumentSnapshot.isSuccessful()){
                groupModelList.clear(); //Listeyi siliyoruz.
                for(DocumentSnapshot documentSnapshot : queryDocumentSnapshot.getResult()){ //Listeye ekliyorum.
                    GroupModel groupModel=documentSnapshot.toObject(GroupModel.class);
                    groupModelList.add(groupModel);
                }
                groupsRecyclerView.setAdapter(new GroupAdapter(groupModelList));
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    }
    private void FetchMessage(){
        String userId = mAuth.getCurrentUser().getUid(); //Oncelikle userid aliyor.
        mStore.collection("/userdata/" + userId + "/" + "messages").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                messageModelList.clear(); //Listeyi siliyoruz.
                for(DocumentSnapshot documentSnapshot : task.getResult()){ //Listeye ekliyorum.
                    MessageModel messageModel=documentSnapshot.toObject(MessageModel.class); //icindeki her bir dokuman icin
                    messageModelList.add(messageModel);
                }
                messagesRecyclerView.setAdapter(new MessageAdapter(messageModelList));
                selectedMessageTextView.setText("Seçili Mesaj "+selectedMessageModel.getName());
            }
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);//Yeni olustur ve dikey olustur.
            messagesRecyclerView.setLayoutManager(linearLayoutManager);
        });
    }
    private void BulkMessage(){
        if(selectedGroupModel == null || selectedMessageModel == null){  //Grup ve mesaj kontrolu yapiyorum.
            Toast.makeText(getContext(), "Toplu Mesaj İçin Bir Grup Seçin Ve Mesaj Oluşturun !", Toast.LENGTH_SHORT).show();
            return;
        }
        if(selectedGroupModel.getNumbers() != null && selectedGroupModel.getNumbers().size() > 0){ //Grup sayisinin toplu sms gonderimine uygun olup olmama kontrolunu yapiyorum.
            SmsManager smsManager = SmsManager.getDefault();
            for (String number : selectedGroupModel.getNumbers()) {
                smsManager.sendTextMessage(number, null, selectedMessageModel.getDescription(), null, null);//sms olarak secilen mesajin icerigini verdim.
            }

            Toast.makeText(getContext(), "Toplu Mesaj Gönderildi !", Toast.LENGTH_SHORT).show();
        }
    }
}