package com.example.admin.tripapplication.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.admin.tripapplication.R;
import com.example.admin.tripapplication.TestRecyclerViewAdapter;
import com.example.admin.tripapplication.cameraAction.FaceActivity;
import com.example.admin.tripapplication.login.User;
import com.example.admin.tripapplication.sns.LikeData;
import com.example.admin.tripapplication.sns.SnsData;
import com.example.admin.tripapplication.sns.WriteActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class FirstRecyclerViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference databaseReference2 = firebaseDatabase.getReference();
    private static final boolean GRID_LAYOUT = false;




    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    Handler handler = new Handler();


    public static FirstRecyclerViewFragment newInstance() {
        return new FirstRecyclerViewFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toast.makeText(getContext(), "리사이클뷰", Toast.LENGTH_SHORT).show();
        Bundle bundle = getArguments();




        return inflater.inflate(R.layout.fragment_firtst_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


        if (GRID_LAYOUT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mRecyclerView.setHasFixedSize(true);

        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());

        swipeRefreshLayout.setOnRefreshListener(this);


    }


    @Override
    public void onRefresh() {
        //새로고침 코드
        Log.d("QW", "새로고침");

//        mRecyclerView.removeAllViewsInLayout();



        Log.d("QQQ", "새로고침");
        swipeRefreshLayout.setRefreshing(false);
    }
}
