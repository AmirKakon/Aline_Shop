package com.example.alineshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private Button m_editBtn;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_profle, container, false);

        m_editBtn = (Button) myView.findViewById(R.id.editbutton);
        m_editBtn.setOnClickListener((View.OnClickListener) this);
        return myView;
    }

    @Override
    public void onClick(View v) {
//        Fragment fragment = new EditProfileFragment();
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.frameLayout, fragment)
//                .addToBackStack(null)
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                .commit();
    }
}
