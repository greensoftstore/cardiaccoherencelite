package com.martinforget.cardiaccoherencelite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;


public class ProfileSelectionDialog extends Dialog {


    public int ProfileId = 0;
    public boolean isCancelled = false;

    private ProfileManager profileManager;

    private Context context;

    public ProfileSelectionDialog(final Context context, int ProfileId) {
        super(context);
        this.ProfileId = ProfileId;
        this.context = context;

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.profile_selection_menu);
        this.setCanceledOnTouchOutside(false);

        profileManager = new ProfileManager(context);

        setRadioButton(ProfileId);

        Button cancelButton;
        cancelButton = (Button) findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isCancelled = true;
                dismiss();

            }

        });

        // Setprofile name
        for (int i=1; i<ProfileManager.maxNumberProfiles+1; i++)
        {
            String tempString = "profiletxt" + i;
            Resources res = context.getResources();
            int profileId = res.getIdentifier(tempString, "id", getContext().getPackageName());
            TextView textview = (TextView) findViewById(profileId);

            textview.setText(profileManager.getProfileName(i));
        }


        RadioButton radioButton = (RadioButton) findViewById(R.id.radioButton1);

        radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isCancelled = false;
                setSelecteProfileId(1);
            }
        });

        radioButton = (RadioButton) findViewById(R.id.radioButton2);

        radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isCancelled = false;
                setSelecteProfileId(2);
            }
        });

        radioButton = (RadioButton) findViewById(R.id.radioButton3);

        radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isCancelled = false;
                setSelecteProfileId(3);
            }
        });

        radioButton = (RadioButton) findViewById(R.id.radioButton4);

        radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isCancelled = false;
                setSelecteProfileId(4);
            }
        });

        radioButton = (RadioButton) findViewById(R.id.radioButton5);

        radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                isCancelled = false;
                setSelecteProfileId(5);
            }
        });


        // Set listener to play sound on click
        final TextView textView1 = (TextView) findViewById(R.id.profiletxt1);
        textView1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle(context.getResources().getString(R.string.profilename));

                // Set an EditText view to get user input
                final EditText input = new EditText(context);
                alert.setView(input);

                alert.setPositiveButton(context.getResources().getString(R.string.ok), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        textView1.setText(String.valueOf(input.getText()));
                        profileManager.setProfileName(1,String.valueOf(input.getText()));

                    }
                });

                alert.setNegativeButton(context.getResources().getString(R.string.cancel), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });

        final TextView textView2 = (TextView) findViewById(R.id.profiletxt2);
        textView2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle(context.getResources().getString(R.string.profilename));

                // Set an EditText view to get user input
                final EditText input = new EditText(context);
                alert.setView(input);

                alert.setPositiveButton(context.getResources().getString(R.string.ok), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        textView2.setText(String.valueOf(input.getText()));
                        profileManager.setProfileName(2,String.valueOf(input.getText()));
                    }
                });

                alert.setNegativeButton(context.getResources().getString(R.string.cancel), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });

        final TextView textView3 = (TextView) findViewById(R.id.profiletxt3);
        textView3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle(context.getResources().getString(R.string.profilename));

                // Set an EditText view to get user input
                final EditText input = new EditText(context);
                alert.setView(input);

                alert.setPositiveButton(context.getResources().getString(R.string.ok), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        textView3.setText(String.valueOf(input.getText()));
                        profileManager.setProfileName(3,String.valueOf(input.getText()));
                    }
                });

                alert.setNegativeButton(context.getResources().getString(R.string.cancel), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });

        final TextView textView4 = (TextView) findViewById(R.id.profiletxt4);
        textView4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle(context.getResources().getString(R.string.profilename));

                // Set an EditText view to get user input
                final EditText input = new EditText(context);
                alert.setView(input);

                alert.setPositiveButton(context.getResources().getString(R.string.ok), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        textView4.setText(String.valueOf(input.getText()));
                        profileManager.setProfileName(4,String.valueOf(input.getText()));
                    }
                });

                alert.setNegativeButton(context.getResources().getString(R.string.cancel), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });

        final TextView textView5 = (TextView) findViewById(R.id.profiletxt5);
        textView5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);

                alert.setTitle(context.getResources().getString(R.string.profilename));

                // Set an EditText view to get user input
                final EditText input = new EditText(context);
                alert.setView(input);

                alert.setPositiveButton(context.getResources().getString(R.string.ok), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        textView5.setText(String.valueOf(input.getText()));
                        profileManager.setProfileName(5,String.valueOf(input.getText()));
                    }
                });

                alert.setNegativeButton(context.getResources().getString(R.string.cancel), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });

    }

    private int getResourceId(String pVariableName, String pResourcename, String pPackageName) {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void setSelecteProfileId(int id) {
        this.ProfileId = id;
        selectRadioButton(id);
        profileManager.setCurrentProfileId(id);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                dismiss();
            }
        }, 500);

    }

    private void selectRadioButton (int id)
    {
        for (int i=1; i<ProfileManager.maxNumberProfiles+1; i++)
        {
            String tempString = "radioButton" + i;
            Resources res = context.getResources();
            int radioId = res.getIdentifier(tempString, "id", getContext().getPackageName());

            if (i != id)
            {
                RadioButton radioButton = (RadioButton) findViewById(radioId);
                radioButton.setChecked(false);
            }
            else
            {
                RadioButton radioButton = (RadioButton) findViewById(radioId);
                radioButton.setChecked(true);
            }
        }
    }

    private void setRadioButton (int id)
    {
        // Set selected radio button on
        String tempString = "radioButton" + id;
        Log.d("Relaxation", "String :" + tempString);
        Resources res = context.getResources();
        id = res.getIdentifier(tempString, "id", getContext().getPackageName());
        RadioButton radioButton = (RadioButton) findViewById(id);

        radioButton.setChecked(true);

    }

}