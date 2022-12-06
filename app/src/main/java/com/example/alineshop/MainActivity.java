package com.example.alineshop;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity { //implements UsersAdaptor.SelectedUser {
    Toolbar toolbar;
    RecyclerView recycleView;
    private Button logout, next;
    private ListView listView;
    private TextView tv;

    private GlobalVariables glblVbls;
    private TextView m_activityHeader;
    private FirebaseUser fb_user;

    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment = null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

//    List<UserModelTest> userModelList = new ArrayList<>();
//    String[] names = {"Richard", "Alice", "Hannah", "David"};
//
//    UsersAdaptor usersAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_activityHeader = findViewById(R.id.mainHeaderTV);
//        recycleView = findViewById(R.id.recyclerView);
//        toolbar = findViewById(R.id.toolbar);
//        logout = findViewById(R.id.lougoutBtn);
//        next = findViewById(R.id.nextBtn);
//        listView = findViewById(R.id.listView);
//        tv = findViewById(R.id.activityTitleTV);

        tabLayout=(TabLayout)findViewById(R.id.tabMode);
        frameLayout=(FrameLayout)findViewById(R.id.frameLayout);

        fragment = new HomeFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new HomeFragment();
                        break;
                    case 1:
                        fragment = new ProfileFragment();
                        break;
                    case 2:
                        fragment = new EditFragment();
                        break;
                }
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.frameLayout, fragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        glblVbls = (GlobalVariables)  getApplicationContext();

        fb_user = FirebaseAuth.getInstance().getCurrentUser();
        if(fb_user.getDisplayName().isEmpty())
            m_activityHeader.setText(glblVbls.getGv_welcomeGuestText());
        else
            m_activityHeader.setText("Welcome " + fb_user.getDisplayName().split(" ")[0]);


//        this.setSupportActionBar(toolbar);
//        this.getSupportActionBar().setTitle("");
//
//        recycleView.setLayoutManager(new LinearLayoutManager(this));
//        recycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//
//        for(String s: names){
//            UserModelTest userModel = new UserModelTest(s);
//
//            userModitemelList.add(userModel);
//        }
//
//        usersAdaptor = new UsersAdaptor(userModelList, this);
//        recycleView.setAdapter(usersAdaptor);

//        Intent intent = getIntent();
//
//        if(intent. getExtras() != null){
//            FirebaseAuth fb_auth = (FirebaseAuth) intent.getSerializableExtra("auth");
//            tv.setText(fb_auth.getCurrentUser().getEmail());
//        }

//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(MainActivity.this, LoginActivity.class));
//                finish(); //[(1)]
//            }
//        });

//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, NextActivity.class));
//            }
//        });
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
//                    Toast.makeText(MainActivity.this, "value added", Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(MainActivity.this, "Merge successful", Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(MainActivity.this, "New Hash Item", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        //update values of existing data
//        DocumentReference ref = FirebaseFirestore.getInstance().collection("cities").document("JSR");
//        ref.update("capital", true);
//        //continue with DocumentSnapshot (1:08:00 in video)
//
//        //filtering collection on video time 1:12:00
    }

//    @Override
//    public void selectedUser(UserModelTest userModel) {
//        startActivity(new Intent(MainActivity.this, SelectedUserActivity.class).putExtra("data", userModel));
//    }
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