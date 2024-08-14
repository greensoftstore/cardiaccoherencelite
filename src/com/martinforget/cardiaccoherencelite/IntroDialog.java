package com.martinforget.cardiaccoherencelite;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.martinforget.cardiaccoherencelite.R;

public class IntroDialog extends Dialog {

    private Button cancel;
    private Button next;
    private TextView textintro;
    private TextView textintro2;
    private TextView textintro3;
    private int page = 1;
    private boolean lastPage = false;
    private static int lastPageNumber = 3;

    public boolean continueIntro = true;
    private Context context;

    public IntroDialog(final Context context, AssetManager assetManager) {
        super(context);

        this.context = context;

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.intro);
        this.setCanceledOnTouchOutside(true);

        cancel = (Button) this.findViewById(R.id.cancelbutton);
        next = (Button) this.findViewById(R.id.nextIntro);
        textintro = (TextView) this.findViewById(R.id.textintro);
        textintro2 = (TextView) this.findViewById(R.id.textintro2);
        textintro3 = (TextView) this.findViewById(R.id.textintro3);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueIntro = false;
                dismiss();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;

                if (page == 2) {
                    textintro.setText(context.getResources().getString(R.string.intro21));
                    textintro2.setText(context.getResources().getString(R.string.intro22));
                    textintro3.setText(context.getResources().getString(R.string.intro23));
                    textintro3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.cardio, 0);

                } else if (page == 3) {
                    textintro.setText(context.getResources().getString(R.string.intro31));
                    textintro2.setText(context.getResources().getString(R.string.intro32));
                    textintro3.setText(context.getResources().getString(R.string.intro33));

                    textintro3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.heart, 0);

                    next.setVisibility(View.INVISIBLE);
                    cancel.setText(context.getResources().getString(R.string.finish));
                }
            }

        });
        this.show();
    }

}
