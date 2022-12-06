package com.example.alineshop;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ItemListActivity extends AppCompatActivity implements ItemsAdaptor.SelectedItem {
    private GlobalVariables glblVbls;

    private TextView m_activityHeader;
    private Toolbar m_toolbar;
    private RecyclerView m_recycleView;
    private ImageView m_moreMenu;

    private List<ItemModel> itemModelList;
    private ItemsAdaptor m_ItemsAdaptor;
    private Date currTime;

    private FirebaseUser fb_user;
    private FirebaseFirestore fb_FSDatabase;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        initialSetup();

        setHeader();

        m_moreMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ItemListActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ItemListActivity.this, LoginActivity.class));
                finish(); //[(1)]
            }
        });
//
//        //creates a branch and under that another branch which value is pink
//        FirebaseDatabase.getInstance().getReference().child("Test").child("Bachelorette Dress 1").setValue("pink");
//
//        //create more than 1 branch at a time
//        HashMap<String,Object> map = new HashMap<>();
//        map.put("color", "pink");
//        map.put("size", "small");
//        FirebaseDatabase.getInstance().getReference().child("Test").child("Bachelorette Dress 2").updateChildren(map);
//
//        //Firebase grabbing and showing data
//        final ArrayList<String> list2 = new ArrayList<>();
//        final ArrayAdapter adapter2 = new ArrayAdapter<String>(this, R.layout.list_item, list2);
//        listView.setAdapter(adapter2);
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Dresses");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                list2.clear();
//                for(DataSnapshot snapshot1 : snapshot.getChildren()){
//                    ItemModel item = snapshot1.getValue(ItemModel.class);
//                    String txt = item.getName() + " : " + item.getColor() + " : " + item.getSize();
//                    list2.add(txt);
//                }
//                adapter2.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        //Cloud FireBase - to add a new item
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Map<String, Object> city = new HashMap<>();
//        city.put("name", "skokie");
//        city.put("state", "illinois");
//        city.put("country", "USA");
//
//        db.collection("cities").document("JSR").set(city).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful())
//                    Toast.makeText(ItemListActivity.this, "value added", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //to merge new value to existing item
//        Map<String,Object> data = new HashMap<>();
//        data.put("capital", false);
//
//        db.collection("cities").document("JSR").set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful())
//                    Toast.makeText(ItemListActivity.this, "Merge successful", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //creates new iten that has unique Id
//        city.put("name", "Denver");
//        city.put("state", "Colorodo");
//        city.put("country", "USA");
//        city.put("capital", true);
//
//        db.collection("cities").add(city).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentReference> task) {
//                if(task.isSuccessful())
//                    Toast.makeText(ItemListActivity.this, "New Hash Item", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //update values of existing data
//        DocumentReference ref = FirebaseFirestore.getInstance().collection("cities").document("JSR");
//        ref.update("capital", true);
//        //continue with DocumentSnapshot (1:08:00 in video)
//
//        //filtering collection on video time 1:12:00
//
    }

    private void initialSetup() {
        m_activityHeader = findViewById(R.id.mainHeaderTV);
        m_moreMenu = findViewById(R.id.moreMenuIV);
        m_recycleView = findViewById(R.id.recyclerView);
        m_toolbar = findViewById(R.id.toolbar);

        glblVbls = (GlobalVariables)  getApplicationContext();
        itemModelList = new ArrayList<>();

        fb_user = FirebaseAuth.getInstance().getCurrentUser();
        fb_FSDatabase = FirebaseFirestore.getInstance();

        this.setSupportActionBar(m_toolbar);
        this.getSupportActionBar().setTitle("");

        recyclerViewSetup();
    }

    private void recyclerViewSetup() {
        m_ItemsAdaptor = new ItemsAdaptor(itemModelList);

        m_recycleView.setLayoutManager(new LinearLayoutManager(this));
        m_recycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        m_recycleView.setAdapter(m_ItemsAdaptor);

        fb_FSDatabase.collection("Dresses").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null)
                    Toast.makeText(ItemListActivity.this, "Error: "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                for (DocumentChange doc: value.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        String currname = doc.getDocument().getString("m_name");
                        String currcolor = doc.getDocument().getString("m_color");
                        String currsize = doc.getDocument().getString("m_size");
                        String currowner = doc.getDocument().getString("m_owner");
                        ItemModel currItem = new ItemModel(currcolor, currname,currowner, currsize);
                        ItemModel currItem2 = (ItemModel) doc.getDocument().toObject(ItemModel.class);
                        itemModelList.add(currItem);
                        itemModelList.add(currItem2);
                        m_ItemsAdaptor.notifyDataSetChanged();
                    }

                }

            }
        });


    }

    private void setHeader() {
        if (fb_user != null) {
            if (fb_user.getDisplayName().isEmpty())
                m_activityHeader.setText(glblVbls.getGv_welcomeGuestText());
            else
                m_activityHeader.setText("Welcome " + fb_user.getDisplayName().split(" ")[0]);
        }
        else
            Toast.makeText(this, "User error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void selectedItem(ItemModel itemModel) {
        startActivity(new Intent(ItemListActivity.this, SelectedUserActivity.class).putExtra("data", itemModel));
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu, menu);
//
//        MenuItem menuItem = menu.findItem(R.id.search_view);
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                //usersAdaptor.getFilter().filter(newText);
//                return false;
//            }
//        });
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//
//        if(id == R.id.search_view)
//            return true;
//
//        return super.onOptionsItemSelected(item);
//    }
}