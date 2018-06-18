package com.example.admin.tripapplication.fragment;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.admin.tripapplication.sns.ProgressDlg;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class RecyclerViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference databaseReference2 = firebaseDatabase.getReference();
    private static final boolean GRID_LAYOUT = false;
    private static final int ITEM_COUNT = 10;
    boolean check = false;
    SnsData snsData;
    LikeData likeData;
    ArrayList<SnsData> snsDataArrayList = new ArrayList<>();
    ArrayList<LikeData> likeDataArrayList = new ArrayList<>();
    String userID;
    String proImage;
    @BindView(R.id.floatingActionMenu)
    FloatingActionMenu fabmenu;
    @BindView(R.id.fab1)
    FloatingActionButton fab1;
    @BindView(R.id.fab2)
    FloatingActionButton fab2;


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    Handler handler = new Handler();


    TestRecyclerViewAdapter adapter;
    ArrayList<SnsData> items;
    ArrayList<SnsData> temp;
    ArrayList<LikeData> templike;
    ArrayList<LikeData> LikeArray=new ArrayList<>();
    ArrayList<LikeData> likeitem=new ArrayList<>();
    ArrayList<User> userItem=new ArrayList<>();
    private AsyncTask<Integer, String, Integer> mProgressDlg;

    public static RecyclerViewFragment newInstance() {
        return new RecyclerViewFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toast.makeText(getContext(), "리사이클뷰", Toast.LENGTH_SHORT).show();
        Bundle bundle = getArguments();
        if (bundle != null) {
            Log.d("tiger", "호랑이");
            userID = bundle.getString("id");
            snsDataArrayList = (ArrayList<SnsData>) bundle.getSerializable("array");
            LikeArray=(ArrayList<LikeData>)bundle.getSerializable("like");
            userItem=(ArrayList<User>)bundle.getSerializable("users");
        }



        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        items = new ArrayList<>();
        for (int i = snsDataArrayList.size() - 1; i >= 0; i--) {
            items.add(new SnsData(snsDataArrayList.get(i).getId(), snsDataArrayList.get(i).getDate(), snsDataArrayList.get(i).getTitle(), snsDataArrayList.get(i).getText(), snsDataArrayList.get(i).getImageurl()));


        }
        for(int i=LikeArray.size()-1; i>=0; i--){
            likeitem.add(new LikeData(LikeArray.get(i).getGetID(), LikeArray.get(i).getGetTitle(), LikeArray.get(i).getGetDate(), LikeArray.get(i).getSetID(), LikeArray.get(i).getSetText()));

        }
//        Log.d("QQQ", items.get(3).getId());
        //setup materialviewpager

        if (GRID_LAYOUT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mRecyclerView.setHasFixedSize(true);

        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        adapter = new TestRecyclerViewAdapter(items);
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter.setID(userID);
        adapter.setContext(getContext());
        adapter.setLike(likeitem);
        adapter.setUsers(userItem);
        mRecyclerView.setAdapter(adapter);
//        fab1.attachToRecyclerView(mRecyclerView);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WriteActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FaceActivity.class);
                //intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

    }

    private void snsinitDatabase() {
        Log.d("QQQ", "호출");
        databaseReference.child("snsDB").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                snsData = dataSnapshot.getValue(SnsData.class);
                snsDataArrayList.add(snsData);
                Log.d("QQQ", "들어옴");
                Log.d("QQQ", snsDataArrayList.size() + "");
                temp=new ArrayList<>();
                if(snsDataArrayList.size()>0) {
                    for (int i = snsDataArrayList.size() - 1; i >= snsDataArrayList.size() - 1; i--) {
                        temp.add(new SnsData(snsDataArrayList.get(i).getId(), snsDataArrayList.get(i).getDate(), snsDataArrayList.get(i).getTitle(), snsDataArrayList.get(i).getText(), snsDataArrayList.get(i).getImageurl()));

                    }
                    for(int i=0; i<temp.size(); i++){
                        items.add(temp.get(i));
                        Log.d("QQQ", temp.get(i).getDate());
                    }
                    adapter.setDataDB(items);
                    mRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void likeinitDatabase() {
        Log.d("QE", "호출");
        databaseReference2.child("likeDB").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                likeData = dataSnapshot.getValue(LikeData.class);
                likeDataArrayList.add(likeData);
                Log.d("QE", "들어옴");
//                Log.d("QE", snsDataArrayList.size() + "");
                templike=new ArrayList<>();
                if(likeDataArrayList.size()>0) {
                    for (int i = likeDataArrayList.size() - 1; i >= likeDataArrayList.size() - 1; i--) {
                        templike.add(new LikeData(likeDataArrayList.get(i).getGetID(), likeDataArrayList.get(i).getGetID(), likeDataArrayList.get(i).getGetTitle(), likeDataArrayList.get(i).getSetID(), likeDataArrayList.get(i).getSetText()));
                    }
                    for(int i=0; i<templike.size(); i++){
                        likeitem.add(templike.get(i));

                    }
                    adapter.setDataLikeDB(likeitem);
//                    mRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onRefresh() {
        //새로고침 코드
        Log.d("QW", "새로고침");
        items.clear();
        snsDataArrayList.clear();
//        likeitem.clear();
//        likeDataArrayList.clear();
//        likeinitDatabase();
        snsinitDatabase();
//        mRecyclerView.removeAllViewsInLayout();



        Log.d("QQQ", "새로고침");
        swipeRefreshLayout.setRefreshing(false);
    }
}
