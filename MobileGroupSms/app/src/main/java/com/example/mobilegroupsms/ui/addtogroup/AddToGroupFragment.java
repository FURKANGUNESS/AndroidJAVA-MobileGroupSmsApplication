package com.example.mobilegroupsms.ui.addtogroup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilegroupsms.ContactListModel;
import com.example.mobilegroupsms.GroupModel;
import com.example.mobilegroupsms.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class AddToGroupFragment extends Fragment {
    TextView selectedGroupsTextView;
    RecyclerView groupsRecyclerView, contactRecyclerView;

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    FirebaseStorage mStorage;

    GroupModel selectedGroup; //Secili gruplari tutmasi icin bir model.
    ArrayList<GroupModel> groupModelList;
    ArrayList<ContactListModel> contactListModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_to_group, container, false);

        selectedGroupsTextView = view.findViewById(R.id.addtogroup_selectedGroupsTextView);

        groupsRecyclerView = view.findViewById(R.id.addtogroup_groupsRecyclerView);
        contactRecyclerView = view.findViewById(R.id.addtogroup_groupsRecyclerView);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();

        //olusturdugumuz model listleri baslatiyorum.
        groupModelList = new ArrayList<>();
        contactListModelList = new ArrayList<>();

        //rehber izinlerini dinamik olarak aliyoruz.
        ActivityResultLauncher launcher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGrant -> {
                    //izinin verilip verilmedigini kontrol ediyorum
                    if (isGrant) {
                        FetchContacts();
                    } else {
                        Toast.makeText(getContext(), "Rehber'e Erişim İzini Verilmeli !", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            launcher.launch(Manifest.permission.READ_CONTACTS); //eger okuma iznimiz varsa launcherla baslaticaz.
        } else {
            FetchContacts();
        }
        FetchGroup();
        return view;
    }

    //Gruplari cekmek icin
    private void FetchGroup() {
        String uid = mAuth.getCurrentUser().getUid();

        mStore.collection("/userdata/" + uid + "/" + "groups").get().addOnSuccessListener(queryDocumentSnapshots -> {
            groupModelList.clear();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                GroupModel groupModel = documentSnapshot.toObject(GroupModel.class);
                groupModelList.add(groupModel);
            }
            groupsRecyclerView.setAdapter(new GroupAdapter(groupModelList));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            groupsRecyclerView.setLayoutManager(linearLayoutManager);
        });
    }

    private void FetchContacts() {
        //Rehberle bir cursor baslat dedim.
        Cursor cursor = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        //Cursorda item oldugu surece numara isim ve imagleri bu sekilde okuyacagim.
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            @SuppressLint("Range") String image = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            ContactListModel contactListModel = new ContactListModel(name, number, image);
            contactListModelList.add(contactListModel);
        }
        contactRecyclerView.setAdapter(new ContactListAdapter(contactListModelList));

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    }
}