package com.example.waterwatch;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {
    ImageView more;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    FrameLayout frameLayout;
    Boolean isOpen=false;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);
        more=view.findViewById(R.id.more_button);
        frameLayout=view.findViewById(R.id.frame_layout);
// Initialize UI components
        drawerLayout = view.findViewById(R.id.drawer_layout);
        navigationView = view.findViewById(R.id.nav_view);
        Fragment fragment=new MapFragment();

        // Open fragment
        getChildFragmentManager()
                .beginTransaction().replace(R.id.frame_layout,fragment)
                .commit();
//        Toast.makeText(view.getContext(), "Home", Toast.LENGTH_SHORT).show();

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpen){
                    drawerLayout.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    drawerLayout.closeDrawer(navigationView);
                    isOpen=false;
                }
                else{
                    frameLayout.setVisibility(View.GONE);
                    drawerLayout.setVisibility(View.VISIBLE);
                    drawerLayout.openDrawer(navigationView);
                    isOpen=true;
                }
            }
        });
        // Handle navigation item clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            int item1=R.id.nav_item1;
            int item2=R.id.nav_item2;
            int item3=R.id.nav_item3;

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int menuitem=menuItem.getItemId();

                if(menuitem==item1){

                    Intent intent1=new Intent(view.getContext(),UserProfile.class);
                    drawerLayout.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    startActivity(intent1);

                }
                else if(menuitem==item2){
                    Intent intent2=new Intent(view.getContext(),ProblemActivity.class);
                    drawerLayout.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);
                    startActivity(intent2);

                }
                else if(menuitem==item3){

                    view=LayoutInflater.from(getContext()).inflate(R.layout.logoutdialog, null);
                    Button logout = view.findViewById(R.id.logout);
                    Button cancel=view.findViewById(R.id.no);
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setView(view);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    logout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(view.getContext(),Login.class);
                            FirebaseAuth.getInstance().signOut();
                            getActivity().finish();
                            startActivity(intent);


                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();

                        }
                    });
                }

                // Close the drawer when an item is selected
                drawerLayout.closeDrawers();
                return true;
            }
        });

        return view;
    }

}