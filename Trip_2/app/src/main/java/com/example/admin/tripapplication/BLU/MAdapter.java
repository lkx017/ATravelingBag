package com.example.admin.tripapplication.BLU;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.tripapplication.R;
import com.example.admin.tripapplication.fragment.BleFragment;

import java.util.ArrayList;

public class MAdapter extends RecyclerView.Adapter<MAdapter.ViewHolder> {

    private ArrayList<MyList> mDataset;
    public static Context mContext;
    public static BleFragment bleFragment;

    Handler handler=null;




    // Provide a suitable constructor (depends on the kind of dataset)
    //생성자
    public MAdapter(ArrayList<MyList> myDataset, Context mContext, BleFragment bleFragment ) {
        mDataset = myDataset;
        this.mContext = mContext;
        this.bleFragment = bleFragment;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_list, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
/*        private TextView MAC = null;
        private Button connect = null;*/
        private TextView name = null;
        private ImageView image = null;
        private ImageView siran = null;
        private ImageView serch = null;
        private ImageView set =  null;
        private ImageView con = null;
        private ImageView signal = null;
        private TextView con_sate = null;

        public ViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
           /* MAC = (TextView) view.findViewById(R.id.mac);
            connect =(Button)view.findViewById(R.id.connect);*/
            image=(ImageView)view.findViewById(R.id.image);
            siran=(ImageView)view.findViewById(R.id.siran);
            serch=(ImageView)view.findViewById(R.id.serch);
            set=(ImageView)view.findViewById(R.id.set);
            con=(ImageView)view.findViewById(R.id.con);
            signal=(ImageView)view.findViewById(R.id.signal_state);
            con_sate=(TextView)view.findViewById(R.id.con_state);


        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.name.setText(mDataset.get(position).getName()+" (" + mDataset.get(position).getItem() + ") ");
        holder.con_sate.setText(mDataset.get(position).getCon_state());
        holder.signal.setImageResource(mDataset.get(position).getSignal_state());
        holder.siran.setImageResource(mDataset.get(position).getSiran());
        holder.serch.setImageResource(mDataset.get(position).getSerch());
        holder.set.setImageResource(mDataset.get(position).getSet());
        holder.con.setImageResource(mDataset.get(position).getCon());
        holder.image.setImageResource(mDataset.get(position).getIcon());

        //holder.MAC.setText(mDataset.get(position).getMAC());
       /* holder.connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Tiger", mDataset.get(position).getMAC()+"");
                //Toast.makeText(mContext,mDataset.get(position).getMAC()+"",Toast.LENGTH_SHORT).show();
                bleFragment.deviceconnect(position);
            }
        });*/
       holder.con.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(mDataset.get(position).getCon() == R.drawable.con_1){
                   Log.d("Tiger", "onClick: "+position);
                   AlertDialog.Builder alert_confirm = new AlertDialog.Builder(mContext);
                   alert_confirm.setTitle("connect").setMessage("연결하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                           new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   bleFragment.deviceconnect(position);
                                    bleFragment.progressdialog();
                               }
                           }).setNegativeButton("취소",
                           new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   // 'No'
                                   return;
                               }
                           });
                   AlertDialog alert = alert_confirm.create();
                   alert.show();

               }
               else {
                   bleFragment.stopprevent(position);
                   bleFragment.devicedisconnect(position);
                   bleFragment.signal_stop(position);
                  // bleFragment.prevent_flag(0);
                   Log.d("Tiger", "onClick:1 ");
                  // mDataset.get(position).setCon(R.drawable.con_1);
                 //  holder.con.setImageResource(R.drawable.con_1);
                   //mDataset.get(position).setSiran(R.drawable.siran_1);
                  // holder.siran.setImageResource(R.drawable.siran_1);
                   Log.d("Tiger", "onClick:2 ");
                  // holder.con_sate.setText("연결 끊김");
                  // holder.signal.setImageResource(R.drawable.signal_0);

               }
           }
       });

       holder.siran.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(mDataset.get(position).getSiran()== R.drawable.siran_1&& mDataset.get(position).getCon() == R.drawable.con_2 ) {
                   Log.d("Tiger", "onClick: ");
                   bleFragment.startprevent(position);
                   bleFragment.prevent_flag(position,1);
                   holder.siran.setImageResource(R.drawable.siran_2);
                   mDataset.get(position).setSiran(R.drawable.siran_2);
               }
               else if(mDataset.get(position).getSiran()== R.drawable.siran_2 && mDataset.get(position).getCon() == R.drawable.con_2 ){
                   Log.d("Tiger", "onClick: ");
                   bleFragment.stopprevent(position);
                   bleFragment.prevent_flag(position,0);
                   holder.siran.setImageResource(R.drawable.siran_1);
                   mDataset.get(position).setSiran(R.drawable.siran_1);

               }
           }
       });

       holder.serch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(mDataset.get(position).getSerch()== R.drawable.serch_1&& mDataset.get(position).getCon() == R.drawable.con_2 ) {
                   AlertDialog.Builder alert_confirm = new AlertDialog.Builder(mContext);
                   alert_confirm.setTitle("search").setMessage("센서에서 소리가 울립니다.").setCancelable(false).setNegativeButton("취소",
                           new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {

                                   if(handler!=null) {
                                       handler.removeMessages(0);
                                        handler = null;
                                   }
                                   holder.serch.setImageResource(R.drawable.serch_1);
                                   mDataset.get(position).setSerch(R.drawable.serch_1);
                                   return;
                               }
                           });
                   AlertDialog alert = alert_confirm.create();
                   alert.show();

                   if (handler == null) {
                       handler = new Handler() {
                           @Override
                           public void handleMessage(Message msg) {

                               super.handleMessage(msg);
                               handler.sendEmptyMessageDelayed(0, 500);
                               bleFragment.writeCharacteristic("2",position);
                           }
                       };
                       handler.sendEmptyMessage(0);
                   }

                   holder.serch.setImageResource(R.drawable.serch_2);
                   mDataset.get(position).setSerch(R.drawable.serch_2);

               }

           }
       });

       holder.set.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(mDataset.get(position).getSet()== R.drawable.set_1){
                   ArrayList<String> setmenu = new ArrayList<String>();

                       setmenu.add("세부 사항");
                       setmenu.add("삭제");


                   //Create sequence of items
                   final CharSequence[] menu = setmenu.toArray(new String[setmenu.size()]);
                   AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
                   dialogBuilder.setTitle("set");
                   dialogBuilder.setItems(menu, new DialogInterface.OnClickListener() {
                       public int bleflag=0;

                       public void onClick(DialogInterface dialog, int item) {
                           String selectedText = menu[item].toString();  //Selected item in listview
                           Log.d("Tiger", "onClick: "+selectedText);
                           switch (selectedText){
                               case "세부 사항":
                                   bleFragment.details(position);
                                   break;
                               case "삭제":
                                   dialog.dismiss();
                                   AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                                   ab.setTitle("삭제");
                                   this.bleflag =bleFragment.bleflag();
                                   if(bleflag==1) {
                                       ab.setMessage("삭제하기 위해선 블루투스의 연결을 모두 해제해야 합니다.");
                                       ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {
                                               bleFragment.disconnectAll();
                                               bleFragment.deleteitem(position);
                                           }
                                       });
                                       ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which) {
                                               dialog.dismiss();
                                           }
                                       });
                                       ab.show();
                                       //bleFragment.deleteitem(position);
                                       bleflag =0;
                                       break;
                                   }else{
                                       bleFragment.deleteitem(position);
                                   }
                           }
                       }
                   });
                   //Create alert dialog object via builder
                   AlertDialog alertDialogObject = dialogBuilder.create();
                   //Show the dialog
                   alertDialogObject.show();
               }

           }
       });


    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

   /* public void startprevent(){
        if (handler == null) {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {

                    super.handleMessage(msg);
                    handler.sendEmptyMessageDelayed(10, 500);
                    bleFragment.startprevent();
                }
            };
            handler.sendEmptyMessage(10);
        }
    }*/
/*

    public void stopprevent(){
        if(handler!=null){
            handler.removeMessages(10);
            handler=null;
        }
    }
*/

}


