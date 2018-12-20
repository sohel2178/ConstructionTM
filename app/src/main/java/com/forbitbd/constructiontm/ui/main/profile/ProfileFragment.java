package com.forbitbd.constructiontm.ui.main.profile;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.forbitbd.constructiontm.R;
import com.forbitbd.constructiontm.model.User;
import com.forbitbd.constructiontm.ui.main.MainActivity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener ,ProfileContract.View{


    private EditText etCompanyName,etCompanyDesc;
    private TextInputLayout tiCompanyName,tiCompanyDesc;
    private CircleImageView ivLogo,ivProfile;
    private TextView tvName,tvEmail;
    private Button btnSelect,btnUpload;

    private ProfilePresenter mPresenter;

    private boolean isUpdate;



    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ProfilePresenter(this);
    }

    @Override
    public void onResume() {
        if(getActivity() instanceof MainActivity){
            MainActivity ma = (MainActivity) getActivity();
            ma.setTitle(getString(R.string.profile));

        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvName = view.findViewById(R.id.name);
        tvEmail = view.findViewById(R.id.email);
        ivProfile = view.findViewById(R.id.iv_profile);

        etCompanyName = view.findViewById(R.id.et_company_name);
        etCompanyDesc = view.findViewById(R.id.et_company_desc);
        tiCompanyName = view.findViewById(R.id.one);
        tiCompanyDesc = view.findViewById(R.id.two);

        ivLogo = view.findViewById(R.id.company_logo);
        btnSelect = view.findViewById(R.id.btn_select);
        btnUpload = view.findViewById(R.id.btn_upload);

        btnSelect.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

        mPresenter.requestForUser();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_select:
                mPresenter.browseClick();
                //getCameraPermission();
                break;

            case R.id.btn_upload:
                final String companyName = etCompanyName.getText().toString();
                final String companyDesc = etCompanyDesc.getText().toString();

                Bitmap bitmap;

                byte[] byteArray=null;

                try {
                    bitmap = ((BitmapDrawable)ivLogo.getDrawable()).getBitmap();
                }catch (Exception e){
                   bitmap=null;
                }


                if(bitmap!=null){
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byteArray = baos.toByteArray();
                }





                boolean valid =mPresenter.validate(companyName,companyDesc,byteArray);

                if(!valid){
                    return;
                }
                mPresenter.updateInfo(companyName,companyDesc,byteArray,isUpdate);

                break;
        }

    }

    @Override
    public void clearPreErrors() {
        tiCompanyName.setErrorEnabled(false);
        tiCompanyDesc.setErrorEnabled(false);
    }

    @Override
    public void showDialog() {
        if(getActivity() instanceof MainActivity){
            MainActivity ma = (MainActivity) getActivity();
            ma.showProgressDialog();
        }

    }

    @Override
    public void hideDialog() {
        if(getActivity() instanceof MainActivity){
            MainActivity ma = (MainActivity) getActivity();
            ma.hideProgressDialog();
        }
    }

    @Override
    public void showErrorMessage(int fieldId, String message) {
        switch (fieldId){
            case FieldConstant.COMPANY_NAME:
                tiCompanyName.setError(message);
                break;

            case FieldConstant.COMPANY_DESC:
                tiCompanyDesc.setError(message);
                break;

            case FieldConstant.COMPANY_LOGO:
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void complete() {
        if(getActivity() instanceof MainActivity){
            MainActivity ma = (MainActivity) getActivity();
            ma.hideProgressDialog();
            ma.loadHomeFragment();
        }

    }

    @Override
    public void openCropImageActivity() {
        if(getActivity() instanceof MainActivity){
            MainActivity ma = (MainActivity) getActivity();
            ma.openCropImageActivity(true);
        }
    }

    @Override
    public void updateInfo(User user) {
        tvName.setText(user.getName());
        tvEmail.setText(user.getEmail());
        etCompanyName.setText(user.getCompanyName());
        etCompanyDesc.setText(user.getCompanyDesc());

        btnUpload.setText(getResources().getString(R.string.update));

        if(!user.getCompany_logo().equals("")){
            Picasso.with(getActivity())
                    .load(user.getCompany_logo())
                    .into(ivLogo);
        }

        if(!user.getPhotoUri().equals("")){
            Picasso.with(getContext())
                    .load(user.getPhotoUri())
                    .into(ivProfile);
        }
    }

    public void setBitmap(Bitmap bitmap){
        isUpdate=true;
        ivLogo.setImageBitmap(bitmap);
    }



}
