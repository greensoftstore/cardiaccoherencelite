package com.martinforget.cardiaccoherencelite;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.martinforget.ColorTable;
import com.martinforget.SessionHistoryDatabase;
import com.martinforget.SessionHistoryDatabase.SessionHistoryCursor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;


import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SessionHistoryActivity extends Activity {

    TextView startTime;
    TextView cycle;
    TextView ratio;
    TextView duration;
    TextView heartStart;
    TextView heartEnd;

    TextView textEmail;
    EditText editEmail;
    Button sendButton;
    Button cancel;
    AlertDialog.Builder builder;
    String tableHtml;
    String mPath;
    String tableSession;
    String lastSession;
    SessionHistoryDatabase db;
    private ImageView btnBack;

    ArrayList<Entry> yVals = new ArrayList<Entry>();
    final HashMap<Integer, String>numMap = new HashMap<>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy HH:mm");

    String[] mSession;

    private LineChart mChart;

    public String getSessionHistory(int id) {
        switch (id) {
            case 0: {
                return tableSession;


            }
            case 1: {
                lastSession = this.getString(R.string.table_history);
                lastSession = lastSession + "\n" + startTime.getText()
                        + "          " + cycle.getText() + "                "
                        + ratio.getText() + "           "
                        + duration.getText()+ "           "
                        + heartStart.getText()+ "           "
                        + heartEnd.getText();;

                return lastSession;
            }

        }
        return tableSession;

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_history);

//        getActionBar().setDisplayHomeAsUpEnabled(true);
 //       getActionBar().setTitle(getResources().getString(R.string.history));
        mSession = new String[]{
                getResources().getString(R.string.all_session_history),
                getResources().getString(R.string.last_session)};
        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);

        btnBack = (ImageView) findViewById(R.id.btnBack2) ;

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView menu2 = findViewById(R.id.imgMenu2);

        ImageView btnCloseLeft = findViewById(R.id.btnCloseLeft);

        LinearLayout llshareHistory = findViewById(R.id.llshareHistory);
        LinearLayout llResetParam = findViewById(R.id.llResetParam);

        llResetParam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SessionHistoryActivity.this, R.string.clearsessionhistory,
                        Toast.LENGTH_SHORT).show();
                db.deleteSession();
                recreate();
                hideRevealCyrcle2();
            }
        });

        llshareHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContextThemeWrapper ctw = new ContextThemeWrapper(SessionHistoryActivity.this, R.style.CustomDialogTheme);
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        ctw);
                //builder.setTitle("Select");
                builder.setSingleChoiceItems(mSession, 0, null);

                builder.setPositiveButton(getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                int selectedPosition = ((AlertDialog) dialog)
                                        .getListView().getCheckedItemPosition();

                                String currentHistory = getSessionHistory(selectedPosition);

                                Intent shareIntent = null;
                                shareIntent = new Intent(
                                        android.content.Intent.ACTION_SEND);

                                // set the type

                                shareIntent.setType("text/plain");

                                // add a subject
                                shareIntent
                                        .putExtra(
                                                android.content.Intent.EXTRA_SUBJECT,
                                                getResources()
                                                        .getString(
                                                                R.string.application_custom_name));

                                shareIntent
                                        .putExtra(
                                                android.content.Intent.EXTRA_TEXT,
                                                "This is Session History of your application.");

                                shareIntent.putExtra(Intent.EXTRA_TEXT,
                                        currentHistory);

                                // start the chooser for sharing
                                startActivity(Intent.createChooser(
                                        shareIntent,
                                        getResources().getString(
                                                R.string.shareusing)));

                            }
                        });

                builder.setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });

                builder.show();
                hideRevealCyrcle2();
            }
        });

        btnCloseLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRevealCyrcle2();
            }
        });
        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCircularReveal2();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(this.getResources().getColor(R.color.color1));
        }

        db = new SessionHistoryDatabase(this);
        SessionHistoryCursor cursor = db.getSessionHistoryCursor();
        tableSession = this.getString(R.string.table_history);
        for (int count = 0; count < cursor.getCount(); count++) {

            cursor.moveToPosition(count);

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View fullRow = inflater.inflate(R.layout.row_session_history, null);
            startTime = (TextView) fullRow.findViewById(R.id.startTimeSession);
            cycle = (TextView) fullRow.findViewById(R.id.cyclesSession);
            ratio = (TextView) fullRow.findViewById(R.id.ratioSession);
            duration = (TextView) fullRow.findViewById(R.id.durationSession);
            heartStart = (TextView) fullRow.findViewById(R.id.heartStartSession);
            heartEnd = (TextView) fullRow.findViewById(R.id.heartEndSession);

            fullRow.setBackgroundResource(ColorTable.color[count % 2]);
            startTime.setText(cursor.getStartTime());
            cycle.setText(String.valueOf(cursor.getCycles()) + "/min");

            ratio.setText(String.valueOf(cursor.getRatio()) + "/"
                    + String.valueOf(100 - cursor.getRatio()));
            int min = cursor.getDuration() / 60;
            int sec = cursor.getDuration() - (min * 60);
            duration.setText(String.format("%02dm %02ds", min, sec));

            tableSession = tableSession + "\n" + startTime.getText()
                    + "          " + cycle.getText() + "                "
                    + ratio.getText() + "           " + duration.getText() + "           " + heartStart.getText()+ "           " + heartEnd.getText();


            heartStart.setText(String.valueOf(cursor.getHeartBegin()));
            heartEnd.setText(String.valueOf(cursor.getHeartEnd()));

            numMap.put(count, cursor.getStartTime());

            // Generate random values
            /*Random r = new Random();

            int mini = 65;
            int max = 140;

            int i1 = r.nextInt(max - mini + 1) + mini;
            */

            // Add date for graph
            yVals.add(new Entry((float) count, (float) cursor.getHeartEnd()));

            tableLayout.addView(fullRow);

        }

        TableRow row = new TableRow(this);
        row.setBackgroundColor(Color.parseColor("#9FCDDE"));
        tableLayout.addView(row);


        //ADD Chart

        mChart = (LineChart) findViewById(R.id.chart);

        // no description text
        Description description = new Description();
        description.setText(getResources().getString(R.string.graphLabel));
        mChart.setDescription(description);
        mChart.getDescription().setEnabled(true);
        mChart.getDescription().setEnabled(true);

        // enable touch gestures

        mChart.getAxisRight().setDrawLabels(false);
        mChart.setNoDataText(getResources().getString(R.string.noData));

// add data
        setData();


    }

    /*public boolean onOptionsItemSelected(MenuItem item) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;



            case R.id.ClearHistory:

                Toast.makeText(SessionHistoryActivity.this, R.string.clearsessionhistory,
                        Toast.LENGTH_SHORT).show();
                db.deleteSession();
                recreate();
                return true;

            case R.id.ShareHistory:
                ContextThemeWrapper ctw = new ContextThemeWrapper(SessionHistoryActivity.this, R.style.CustomDialogTheme);
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        ctw);
                //builder.setTitle("Select");
                builder.setSingleChoiceItems(mSession, 0, null);

                builder.setPositiveButton(getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                int selectedPosition = ((AlertDialog) dialog)
                                        .getListView().getCheckedItemPosition();

                                String currentHistory = getSessionHistory(selectedPosition);

                                Intent shareIntent = null;
                                shareIntent = new Intent(
                                        android.content.Intent.ACTION_SEND);

                                // set the type

                                shareIntent.setType("text/plain");

                                // add a subject
                                shareIntent
                                        .putExtra(
                                                android.content.Intent.EXTRA_SUBJECT,
                                                getResources()
                                                        .getString(
                                                                R.string.application_custom_name));

                                shareIntent
                                        .putExtra(
                                                android.content.Intent.EXTRA_TEXT,
                                                "This is Session History of your application.");

                                shareIntent.putExtra(Intent.EXTRA_TEXT,
                                        currentHistory);

                                // start the chooser for sharing
                                startActivity(Intent.createChooser(
                                        shareIntent,
                                        getResources().getString(
                                                R.string.shareusing)));

                            }
                        });

                builder.setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });

                builder.show();
                return true;
        }

        return false;
}
*/
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.histomenu, menu);


        return true;
    }

    private void setData() {

        LineDataSet set1;
if  (!yVals.isEmpty())
{
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0 ) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals, "toto");

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);
            set1.setDrawCircles(false);
            set1.setLineWidth(1.8f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(R.color.color0);
            set1.setHighLightColor(R.color.color1);
            set1.setColor(R.color.color0);
            set1.setFillColor(R.color.color1);
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return -10;
                }
            });

            // create a data object with the datasets
            LineData data = new LineData(set1);
            data.setValueTextSize(9f);
            data.setDrawValues(true);

            // set data
            mChart.setData(data);

            Legend l = mChart.getLegend();
            l.setForm(Legend.LegendForm.LINE);
            l.setEnabled(false);

            XAxis xAxis = mChart.getXAxis();

            xAxis.setValueFormatter(new IAxisValueFormatter() {

                @Override
                public String getFormattedValue(float value, AxisBase axis) {

                    return numMap.get((int)value);
                }

            });


            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTypeface(Typeface.SANS_SERIF);
            xAxis.setDrawGridLines(true);
            xAxis.setLabelRotationAngle(45);
            xAxis.setGranularity(1f); // one hour

            // Despite everything done, do not display the X values
            xAxis.setDrawLabels(false);
            xAxis.setYOffset(10);
            mChart.setExtraLeftOffset(5);
            mChart.setExtraRightOffset(5);
        }
    }
}
    public void startCircularReveal2() {
        final View view = findViewById(R.id.view2);
        final View startView = findViewById(R.id.mainContent);
        final ConstraintLayout background = findViewById(R.id.background2);
        final int cx = (int) ((startView.getRight() ) );
        final int cy = (int) ((startView.getTop()  ) );
        //  view.setBackgroundColor(Color.parseColor("#6FA6FF"));
        int finalRadius = Math.max(cy , view.getHeight() - cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, (float) (view.getMeasuredWidth()*1.3) );

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration(500);
        anim.start();
        background.setVisibility(View.VISIBLE);

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRevealCyrcle();
            }
        });
    }

    private void hideRevealCyrcle(){
        final View view = findViewById(R.id.view);
        final View startView = findViewById(R.id.mainContent);
        final ConstraintLayout background = findViewById(R.id.background);
        int dimx =0;
        int dimy =0;

        if (view != null) {
            try {
                dimx = (int) ((startView.getLeft() - view.getMeasuredWidth() * 0.1) / 2);
            } catch (Exception e) {
            }
            try {
                dimy = (int) ((startView.getTop() - view.getMeasuredHeight() * 0.5) / 2);

            } catch (Exception e) {
            }

            final int cx = dimx;
            final int cy = dimy;
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, (float) (view.getMeasuredWidth() * 1.3), startView.getLeft() / 25);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    view.setVisibility(View.INVISIBLE);
                }
            });

            anim.setDuration(500);
            anim.start();
            background.setVisibility(View.INVISIBLE);
        }
    }

    private void hideRevealCyrcle2(){
        final View view = findViewById(R.id.view2);
        final View startView = findViewById(R.id.mainContent);
        final ConstraintLayout background = findViewById(R.id.background2);
        final int cx = (int) ((startView.getRight()  ) );
        final int cy = (int) ((startView.getTop()  ) );
        if (view != null) {
            int finalRadius = Math.max(cy, view.getHeight() - cy);
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, cx, cy, (float) (view.getMeasuredWidth() * 1.3), startView.getLeft() / 25);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    view.setVisibility(View.INVISIBLE);
                }
            });
            anim.setDuration(500);
            anim.start();
            background.setVisibility(View.INVISIBLE);
        }
    }
}
