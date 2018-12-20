package com.forbitbd.constructiontm.ui.searchUser;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sohel on 02-05-18.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    private List<User> userList;
    private LayoutInflater inflater;
    private SearchUserContract.View mView;


    public UserAdapter(Activity activity) {
        this.userList = new ArrayList<>();
        this.inflater = LayoutInflater.from(activity);
        this.mView = (SearchUserContract.View) activity;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_user,parent,false);

        UserHolder holder = new UserHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(UserHolder holder, int position) {

        User user = userList.get(position);

        holder.tvName.setText(user.getName());
        holder.tvEmail.setText(user.getCompanyName());

        if(user.getPhotoUri()!=null && !user.getPhotoUri().equals("")){
            Picasso.with(holder.itemView.getContext())
                    .load(user.getPhotoUri())
                    .into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setData(List<User> users){
        this.userList = users;
        notifyDataSetChanged();
    }

    class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CircleImageView imageView;
        TextView tvName,tvEmail;

        public UserHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            tvName = itemView.findViewById(R.id.name);
            tvEmail = itemView.findViewById(R.id.email);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mView!=null){
                mView.userSelect(userList.get(getAdapterPosition()));
            }

        }
    }
}
