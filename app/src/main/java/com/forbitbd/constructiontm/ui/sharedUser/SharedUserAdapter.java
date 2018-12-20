package com.forbitbd.constructiontm.ui.sharedUser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.database.MyDatabaseRef;
import com.forbitbd.constructiontm.model.SharedUser;
import com.forbitbd.constructiontm.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sohel on 04-05-18.
 */

public class SharedUserAdapter extends RecyclerView.Adapter<SharedUserAdapter.SharedUserHolder> {

    private Context context;
    private List<SharedUser> sharedUserList;
    private LayoutInflater inflater;

    private String projectId;

    private MyDatabaseRef myDatabaseRef;

    public SharedUserAdapter(Context context, String vehicleId) {
        this.context = context;
        this.sharedUserList = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
        this.projectId =vehicleId;
        this.myDatabaseRef = new MyDatabaseRef();
    }

    @Override
    public SharedUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_shared_user,parent,false);
        return new SharedUserHolder(view);
    }


    public void removeItem(int position) {
        sharedUserList.remove(position);
        notifyItemRemoved(position);
    }

    public SharedUser getSharedUser(int position){
        return sharedUserList.get(position);
    }

    @Override
    public void onBindViewHolder(final SharedUserHolder holder, int position) {
        SharedUser sharedUser = sharedUserList.get(position);

        if(sharedUser.getIsEnable()==1){
            holder.switchCompat.setChecked(true);
        }else{
            holder.switchCompat.setChecked(false);
        }

        myDatabaseRef.getUserRef().child(sharedUser.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    User user = dataSnapshot.getValue(User.class);

                    if( user.getPhotoUri()!=null  && !user.getPhotoUri().equals("")){
                        Picasso.with(context)
                                .load(user.getPhotoUri())
                                .into(holder.ivImage);
                    }

                    holder.tvName.setText(user.getName());
                    holder.tvCompanyName.setText(user.getCompanyName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void addSharedUser(SharedUser sharedUser){
        sharedUserList.add(sharedUser);
        int pos = sharedUserList.indexOf(sharedUser);
        notifyItemInserted(pos);
    }

    @Override
    public int getItemCount() {
        return sharedUserList.size();
    }

    class SharedUserHolder extends RecyclerView.ViewHolder{

        CircleImageView ivImage;
        Switch switchCompat;
        TextView tvName,tvCompanyName;
        public RelativeLayout viewBackground, viewForeground;

        public SharedUserHolder(View itemView) {
            super(itemView);

            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

            ivImage = itemView.findViewById(R.id.image);
            switchCompat = itemView.findViewById(R.id.user_switch);
            tvName = itemView.findViewById(R.id.name);
            tvCompanyName = itemView.findViewById(R.id.company_name);


            switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if(b){
                       myDatabaseRef.getSharedUserRef(projectId)
                               .child(sharedUserList.get(getAdapterPosition()).getId())
                               .child("isEnable").setValue(1);

                       myDatabaseRef.getSharedProjectRef(sharedUserList.get(getAdapterPosition()).getId())
                               .child(projectId).child("isEnable").setValue(1);
                    }else{
                        myDatabaseRef.getSharedUserRef(projectId)
                                .child(sharedUserList.get(getAdapterPosition()).getId())
                                .child("isEnable").setValue(0);

                        myDatabaseRef.getSharedProjectRef(sharedUserList.get(getAdapterPosition()).getId())
                                .child(projectId).child("isEnable").setValue(0);

                    }

                }
            });
        }

    }
}
