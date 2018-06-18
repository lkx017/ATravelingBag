package com.example.admin.tripapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.tripapplication.fragment.RecyclerViewFragment;
import com.example.admin.tripapplication.login.User;
import com.example.admin.tripapplication.sns.LikeData;
import com.example.admin.tripapplication.sns.SnsData;
import com.example.admin.tripapplication.sns.datWriteActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by florentchampigny on 24/04/15.
 */
public class TestRecyclerViewAdapter extends RecyclerView.Adapter<TestRecyclerViewAdapter.ItemViewHolder> {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference databaseReference2 = firebaseDatabase.getReference();
    ArrayList<SnsData> contents;
    ArrayList<SnsData> snsDataArrayList=new ArrayList<>();
    String userID;
    Handler handler = new Handler();
    LikeData likeData;
    SnsData snsData;
    ArrayList<LikeData> LikeArray=new ArrayList<>();
    ArrayList<LikeData> likeDataArrayList;
    Context context;
    String key;
    Boolean del=false;
    ArrayList<String> keyArrayList = new ArrayList<>();
    private AsyncTask<Integer, String, Integer> mProgressDlg;
    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    public TestRecyclerViewAdapter() {
    }

    public TestRecyclerViewAdapter(ArrayList<SnsData> contents) {
        this.contents = contents;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {

            default:
                return TYPE_HEADER;
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_big, parent, false);
                return new ItemViewHolder(view) {
                };
            }
            /*case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_small, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            }*/
        }
        return null;
    }

    int n;
    static int count = 0;

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(1000);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(context, "ㅇㅋ", Toast.LENGTH_SHORT).show();
//                                    holder.likeButton.setText(likeDataArrayList.size());
                                }
                            });
                            Log.d("QW", "@@@@@@");
                            Log.d("QW", likeDataArrayList.size()+"#####");
                            for (int i = 0; i < likeDataArrayList.size(); i++) {
                                if (likeDataArrayList.get(i).getGetID().equals(contents.get(position).getId()) &&
                                        likeDataArrayList.get(i).getGetTitle().equals(contents.get(position).getTitle()) &&
                                        likeDataArrayList.get(i).getGetDate().equals(contents.get(position).getDate())) {
                                    if (!likeDataArrayList.get(i).getSetID().equals(userID)) {
                                        holder.likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unlike, 0, 0, 0);
                                        Log.d("QW", "빈하트");
                                    } else {
                                        holder.likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like, 0, 0, 0);
                                        Log.d("QW", "하트");
                                    }
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                if(del==true){
                    holder.delButton.setVisibility(View.VISIBLE);
                }
                else{
                    holder.delButton.setVisibility(View.GONE);
                }
                holder.delButton.setOnClickListener(new View.OnClickListener() {
                    int itemposition = holder.getAdapterPosition();
                    @Override
                    public void onClick(View v) {
                        n = itemposition;
                        setsnsKey(n);
                        Log.d("WWW", n + "");
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    Thread.sleep(1000);
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context, "삭제", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    for (int i = 0; i < snsDataArrayList.size(); i++) {
                                        if (snsDataArrayList.get(i).getId().equals(contents.get(n).getId()) &&
                                                snsDataArrayList.get(i).getTitle().equals(contents.get(n).getTitle()) &&
                                                snsDataArrayList.get(i).getDate().equals(contents.get(n).getDate())) {
                                                Log.d("WWW", "삭제");
                                                databaseReference2.child("/snsDB/" + key).removeValue();
//                                                holder.frameLayout.setVisibility(View.GONE);
                                            notifyItemRemoved(n);
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                });
                holder.snsID.setText(contents.get(position).getId());
                holder.snsTIme.setText(contents.get(position).getDate());
                holder.snsTitle.setText(contents.get(position).getTitle());
                holder.snsText.setText(contents.get(position).getText());
                final String urlS = contents.get(position).getImageurl();
                if (!contents.get(position).getImageurl().equals("")) {
                    Picasso.get().load(urlS).into(holder.snsImage);
                    holder.snsImage.setVisibility(View.VISIBLE);
                } else {
                    holder.snsImage.setVisibility(View.GONE);
                }
                Log.d("QW", userList.size()+"호출");
                for(int i=0; i<userList.size(); i++) {
                    if (contents.get(position).getId().equals(userList.get(i).getUserID())) {
                        Log.d("QW", userList.get(i).getUserID());
                        Picasso.get().load(userList.get(i).getUserImage().toString()).into(holder.snsCircle);
                        //holder.snsImage.setVisibility(View.VISIBLE);
                    }
                    else{
//                        holder.snsImage.setVisibility(View.GONE);
                    }
                }
               // holder.likeButton.setText(likeDataArrayList.size());

                holder.likeButton.setOnClickListener(new View.OnClickListener() {
                    int itemposition = holder.getAdapterPosition();

                    @Override
                    public void onClick(View v) {
                        n = itemposition;
                        setKey(n);
                        Log.d("WWW", n + "");
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    Thread.sleep(1000);
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context, "버튼 클릭", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    for (int i = 0; i < LikeArray.size(); i++) {
                                        if (LikeArray.get(i).getGetID().equals(contents.get(n).getId()) &&
                                                LikeArray.get(i).getGetTitle().equals(contents.get(n).getTitle()) &&
                                                LikeArray.get(i).getGetDate().equals(contents.get(n).getDate())) {
                                            if (LikeArray.get(i).getSetID().equals(userID)) {//삭제
                                                count = 1;
                                                Log.d("WWW", "삭제");
                                                databaseReference.child("/likeDB/" + key).removeValue();
                                                holder.likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unlike, 0, 0, 0);
                                            }
                                        }
                                    }
                                    if (count == 0) {
                                        databaseReference.child("likeDB").push().setValue(new LikeData(contents.get(n).getId(), contents.get(n).getTitle(),
                                                contents.get(n).getDate(), userID, ""));
                                        holder.likeButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like, 0, 0, 0);
                                    }
                                    count = 0;
                                    holder.likeButton.setText(LikeArray.size());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();

                    }
                });
                holder.datButton.setOnClickListener(new View.OnClickListener() {
                    int itemposition = holder.getAdapterPosition();
                    @Override
                    public void onClick(View v) {
                        n = itemposition;
                        Intent intent= new Intent(context, datWriteActivity.class);
                        intent.putExtra("snsDB", contents.get(n));
                        intent.putExtra("userID", userID);
                        intent.putExtra("users", userList);
                        context.startActivity(intent);
                    }
                });

                break;
            /*case TYPE_CELL:
                break;*/
        }
    }

    /*public void updatekey(int i, String s) { //업데이트 소스
        Map<String, Object> likeUpdates = new HashMap<>();
//        Log.d("EEE", key);
        likeUpdates.put(i+"", LikeArray.get(i));
        databaseReference.child("likeDB").child("setID").updateChildren(likeUpdates);

    }*/
    User user;
    ArrayList<User> userList = new ArrayList<>();
    int count1=0;
    public void setsnsKey(final int position) {
        Log.d("WWW", "키 2");
        snsDataArrayList.clear();
        databaseReference2.child("snsDB").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                snsData = dataSnapshot.getValue(SnsData.class);
                snsDataArrayList.add(snsData);

//                Log.d("WWW", key);
                if (contents.get(position).getId().equals(snsData.getId()) &&
                        contents.get(position).getDate().equals(snsData.getDate()) &&
                        contents.get(position).getTitle().equals(snsData.getTitle())) {
                    key = dataSnapshot.getKey();

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
    public void setKey(final int position) {
        Log.d("WWW", "키 2");
        LikeArray.clear();
        databaseReference.child("likeDB").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                likeData = dataSnapshot.getValue(LikeData.class);
                LikeArray.add(likeData);

//                Log.d("WWW", key);
                if (contents.get(position).getId().equals(likeData.getGetID()) &&
                        contents.get(position).getDate().equals(likeData.getGetDate()) &&
                        contents.get(position).getTitle().equals(likeData.getGetTitle()) &&
                        userID.equals(likeData.getSetID())) {
                        key = dataSnapshot.getKey();

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
    public void setDel(Boolean del){
        this.del=del;
    }
    public void setUsers(ArrayList<User> userArrayList){this.userList=userArrayList;}
    public void setContext(Context context) {
        this.context = context;
    }

    public void setLike(ArrayList<LikeData> LikeArray) {
        this.likeDataArrayList = LikeArray;
    }

    public void setData(ArrayList<SnsData> contents) {
        this.contents = contents;
//        notifyDataSetChanged();
    }
    public void setDataDB(ArrayList<SnsData> contents) {
        ArrayList<SnsData> temp=new ArrayList<>();
        for(int i=contents.size()-1; i>=0; i--){
            temp.add(contents.get(i));
        }
        this.contents=temp;
//        notifyDataSetChanged();
    }
    public void setDataLikeDB(ArrayList<LikeData> contents) {
        ArrayList<LikeData> temp=new ArrayList<>();
        for(int i=contents.size()-1; i>=0; i--){
            temp.add(contents.get(i));
        }
        this.likeDataArrayList=temp;
//        notifyDataSetChanged();
    }

    public void setID(String userID) {
        this.userID = userID;
        Log.d("PPP", this.userID);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView snsID;
        private TextView snsTIme;
        private TextView snsTitle;
        private TextView snsText;
        private ImageView snsImage;
        private Button likeButton;
        private Button datButton;
        private Button delButton;
        private CircleImageView snsCircle;
        private FrameLayout frameLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            snsID = itemView.findViewById(R.id.snsID);
            snsTIme = itemView.findViewById(R.id.snsTime);
            snsTitle = itemView.findViewById(R.id.snsTitle);
            snsCircle=itemView.findViewById(R.id.proImage);
            snsText = itemView.findViewById(R.id.snsText);
            snsImage = itemView.findViewById(R.id.snsImage);
            likeButton = itemView.findViewById(R.id.likeButton);
            datButton = itemView.findViewById(R.id.datButton);
            delButton=itemView.findViewById(R.id.delBtn);
            frameLayout=itemView.findViewById(R.id.framelayout);
        }
    }


}