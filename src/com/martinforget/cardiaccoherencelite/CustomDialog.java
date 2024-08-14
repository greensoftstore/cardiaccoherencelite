package com.martinforget.cardiaccoherencelite;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.martinforget.Data;

public class CustomDialog {
    String title = "";
    String msj = "";
    TextView mTitle1;
    TextView mMsj1;
    TextView close;
    ConstraintLayout mConstrain;
    Context mContext;
    CheckBox checkBox;
    boolean debug;
    private Data data ;

    public void customDialog(Activity activity, final Context context){

        data = Data.getInstance(context);
        final View mView = activity.getLayoutInflater().inflate(R.layout.custom_dialog,null);
        final ViewGroup mViewGroup =activity.getWindow().getDecorView().findViewById(android.R.id.content);
        mConstrain = mView.findViewById(R.id.custom1);
        mTitle1 = mView.findViewById(R.id.txt_tittle);
        mMsj1 = mView.findViewById(R.id.txt_msj);
        ConstraintLayout close = mView.findViewById(R.id.close_dialog);
        checkBox = mView.findViewById(R.id.debug);
        mTitle1.setText(this.title);
        mMsj1.setText(this.msj);
        mConstrain.setVisibility(View.VISIBLE);
        mViewGroup.addView(mView);
        checkBox.setChecked(debug);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                debug = checkBox.isChecked();
                data.setDebugMode(debug);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewGroup.removeView(mView);
            }
        });
    }

    public void customAlert(Activity activity, Context context){
        final View mView = activity.getLayoutInflater().inflate(R.layout.custom_dialog,null);
        final ViewGroup mViewGroup =activity.getWindow().getDecorView().findViewById(android.R.id.content);
        mConstrain = mView.findViewById(R.id.custom2);
        mTitle1 = mView.findViewById(R.id.tittle_txt);
        mMsj1 = mView.findViewById(R.id.msj_txt);
        mConstrain.setVisibility(View.VISIBLE);
        close = mView.findViewById(R.id.close_2);
        mTitle1.setText(this.title);
        mMsj1.setText(this.msj);
        mViewGroup.addView(mView);

        close.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     mConstrain.setVisibility(View.GONE);
                     mViewGroup.removeView(mView);
                 }
             }

        );
    }

    public void setDebug(boolean status) {this.debug = status;}
    public void setTitle(String title){
        this.title = title;
    }

    public void setMsj(String msj){
        this.msj = msj;
    }
    public  void setContext(Context context){
        this.mContext = context;
    }
    
}
