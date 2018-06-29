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

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by florentchampigny on 24/04/15.
 */
public class FirstRecyclerViewAdapter extends RecyclerView.Adapter<FirstRecyclerViewAdapter.ItemViewHolder> {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference databaseReference2 = firebaseDatabase.getReference();
    ArrayList<SnsData> contents;
    ArrayList<SnsData> snsDataArrayList = new ArrayList<>();
    String userID;
    Handler handler = new Handler();
    LikeData likeData;
    SnsData snsData;
    ArrayList<LikeData> LikeArray = new ArrayList<>();
    ArrayList<LikeData> likeDataArrayList;
    Context context;
    String key;
    Boolean del = false;
    ArrayList<String> keyArrayList = new ArrayList<>();
    private AsyncTask<Integer, String, Integer> mProgressDlg;
    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    public FirstRecyclerViewAdapter() {
    }

    public FirstRecyclerViewAdapter(ArrayList<SnsData> contents) {
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
    int likecount = 0;

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:




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
            snsCircle = itemView.findViewById(R.id.proImage);
            snsText = itemView.findViewById(R.id.snsText);
            snsImage = itemView.findViewById(R.id.snsImage);
            likeButton = itemView.findViewById(R.id.likeButton);
            datButton = itemView.findViewById(R.id.datButton);
            delButton = itemView.findViewById(R.id.delBtn);
            frameLayout = itemView.findViewById(R.id.framelayout);
        }
    }


}