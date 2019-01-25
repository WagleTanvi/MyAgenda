// Tanvi Wagle Agenda App
// Keeps track of all your homework for multiple days in a expandable listview
// A subject can be added for each day (parent view) and the homework for the subject and its due date (child view)
// You can change the date using datepicker which is run when you click the change date button
//You can delete/add both subjects and reminders
// Press enter after editing the subjects in order to be able to expand it



package com.example.tanvi.myagenda;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyAgenda extends AppCompatActivity {
    Button dateButton;
    DatePickerDialog datePickerDialog;
    TextView date;
    ExpandableListView expandableListView;
    ArrayList<JSONObject> days = new ArrayList<JSONObject>();
    //List<List<JSONObject>> current = new ArrayList<>();
    ArrayList<JSONObject> current = new ArrayList<>();
    String file = "datfile.json";
    //String f = "data.json";
    Button addMain;
    RelativeLayout r;
    CustomAdapter adapter;
    String today;
    int position;
    TextView de;
    TextView m;
    TextView y;
    TextView da;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_agenda);
        dateButton = (Button) findViewById(R.id.datebutton_id);
        addMain =  (Button) findViewById(R.id.buttonMain_add);
        //date = (TextView) findViewById(R.id.tv_dat);
        de = (TextView) findViewById(R.id.tv_dat);
        r = (RelativeLayout) findViewById(R.id.rl);
        r.setBackgroundColor(Color.rgb(154,212,255));
        m = (TextView) findViewById(R.id.tv_month);
        y = (TextView) findViewById(R.id.tv_year);
        da = (TextView) findViewById(R.id.tv_day);
        JSONObject d = new JSONObject();
        JSONObject subject = new JSONObject();
        JSONArray reminders = new JSONArray();
        JSONObject first = new JSONObject();
        JSONObject second = new JSONObject();
        JSONArray a = new JSONArray();
        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat s = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
        today = s.format(c.getTime());
        s = new SimpleDateFormat("yyyy", Locale.US);
        y.setText(s.format(c.getTime()));
        s = new SimpleDateFormat("EEEE", Locale.US);
        da.setText(s.format(c.getTime()));
        s = new SimpleDateFormat("MMM", Locale.US);
        m.setText(s.format(c.getTime()));
        s = new SimpleDateFormat("d", Locale.US);
        de.setText(s.format(c.getTime()));

        //date.setText(today);
        /*Log.d("great",today);
        try{
            first.put("work","Worksheets");
            first.put("due","5/4/16");
            second.put("work","projects");
            second.put("due","6/4/16");
            reminders.put(first);
            reminders.put(second);
            //days.add("Chemistry",subject)
            subject.put("name","Math");
            subject.put("reminders",reminders);
            a.put(subject);
            d.put("info",a);
            d.put("date",today);
            days.add(d);
        }
        catch (JSONException e){ e.printStackTrace();}
        Log.d("hello","first "+days.toString());*/
        if (days.size() == 0){
            JSONObject j = new JSONObject();
            JSONArray ja = new JSONArray();
            try {
                j.put("date",today);
                j.put("info",ja);
            } catch (JSONException e) {e.printStackTrace();}
            days.add(j);
            current.clear();
        }
        try{
            boolean st = true;
            int x=0;
            while (st == true){
                if (x< days.size()  && today.equals(days.get(x).getString("date"))){
                    st = false;
                    position = x;
                    JSONArray jsonArray = days.get(x).getJSONArray("info");
                    for (int e=0; e<jsonArray.length();e++){
                        current.add(jsonArray.getJSONObject(e));
                    }
                }
                if (x == days.size()){
                    //Log.d("hey","cool");
                    st = false;
                    current = new ArrayList<>();
                }
                x++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        expandableListView = (ExpandableListView) findViewById(R.id.expand_id);
        adapter= new CustomAdapter(getApplicationContext(),current);
        expandableListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        expandableListView.setAdapter(adapter);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean st = true;
                    int x=0;
                    int pos = 0;
                    while (st == true){
                        if (x< days.size()  && today.equals(days.get(x).getString("date"))){
                            st = false;
                            pos = x;
                        }
                        /*if (x == days.size()){
                            //Log.d("hey","cool");
                            st = false;
                            current = new ArrayList<>();
                        }*/
                        x++;
                    }
                    Log.d("hey","before "+current.toString());
                    JSONArray l = new JSONArray();
                    for (int y = 0; y < current.size(); y++) {
                        l.put(current.get(y));
                    }
                    days.get(pos).put("info",l);
                    Log.d("hey","after "+days.toString());
                } catch (JSONException e) {e.printStackTrace();}
                datePickerDialog = new DatePickerDialog(MyAgenda.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar c = Calendar.getInstance();
                        c.set(year,month,dayOfMonth);//Toast.makeText(getApplicationContext(), dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();
                        SimpleDateFormat s = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
                        today = s.format(c.getTime());
                        s = new SimpleDateFormat("yyyy", Locale.US);
                        y.setText(s.format(c.getTime()));
                        s = new SimpleDateFormat("EEEE", Locale.US);
                        da.setText(s.format(c.getTime()));
                        s = new SimpleDateFormat("MMM", Locale.US);
                        m.setText(s.format(c.getTime()));
                        s = new SimpleDateFormat("d", Locale.US);
                        de.setText(s.format(c.getTime()));;
                        try{
                            boolean st = true;
                            int x=0;
                            while (st == true){
                                if (x< days.size()  && today.equals(days.get(x).getString("date"))){
                                    st = false;
                                    JSONArray jsonArray = days.get(x).getJSONArray("info");
                                    current.clear();
                                    for (int e=0; e<jsonArray.length();e++){
                                        current.add(jsonArray.getJSONObject(e));
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                if (x == days.size()){
                                    st = false;
                                    JSONObject j = new JSONObject();
                                    JSONArray ja = new JSONArray();
                                    j.put("date",today);
                                    j.put("info",ja);
                                    days.add(j);
                                    current.clear();
                                    adapter.notifyDataSetChanged();
                                }
                                x++;
                            }
                        } catch (JSONException e) {e.printStackTrace();}
                        //date.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });
        addMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    JSONObject j = new JSONObject();
                    JSONArray a = new JSONArray();
                    JSONObject ja = new JSONObject();
                    ja.put("work","");
                    ja.put("due","");
                    a.put(ja);
                    j.put("name","TBD");
                    j.put("reminders",a);
                    int x= 0;
                    boolean st = true;
                    while (st == true) {
                        if (x < days.size() && today.equals(days.get(x).getString("date"))) {
                            st = false;
                            JSONArray jsonArray = days.get(x).getJSONArray("info");
                            jsonArray.put(j);
                            current.add(j);
                            adapter.notifyDataSetChanged();
                        }
                        x++;
                    }
                } catch (JSONException e) {e.printStackTrace();}


            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        /*try {
            JSONArray l = new JSONArray();
            for (int x = 0; x < current.size(); x++) {
                l.put(current.get(x));
            }
            days.get(position).put("info",l);
        } catch (JSONException e) {e.printStackTrace();}*/
        try {
            boolean st = true;
            int x=0;
            int pos = 0;
            while (st == true){
                if (x< days.size()  && today.equals(days.get(x).getString("date"))){
                    st = false;
                    pos = x;
                }
                x++;
            }
            //Log.d("hey","before "+current.toString());
            JSONArray l = new JSONArray();
            for (int y = 0; y < current.size(); y++) {
                l.put(current.get(y));
            }
            days.get(pos).put("info",l);
            //Log.d("hey","after "+days.toString());
        } catch (JSONException e) {e.printStackTrace();}
        try {
            OutputStreamWriter writer = new OutputStreamWriter(openFileOutput(file, Context.MODE_PRIVATE)); // mode private (you dont want to alter it)
            JSONArray j = new JSONArray();
            //days.get(today).getJSONArray("info") = adapter.getArrayList();
            for (int x = 0; x < days.size(); x++) {
                j.put(days.get(x));
            }
            //Log.d("hey","second "+days.toString());
            //Log.d("hey","third "+current.toString());
            writer.write(j.toString());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput(file)));
            String s = reader.readLine();
            if (s != null) {
                JSONArray k = new JSONArray(s);
                days.clear();
                for (int x = 0; x < k.length(); x++) {
                    days.add(k.getJSONObject(x));
                }
                //Log.d("hello", "fourth " + days.toString());
                boolean st = true;
                int d = 0;
                while (st == true && days.size() > 0) {
                    //Log.d("great", today + " " + days.get(d).getString("date"));
                    if (d < days.size() && today.equals(days.get(d).getString("date"))) {
                        st = false;
                        JSONArray jsonArray = days.get(d).getJSONArray("info");
                        current.clear();
                        for (int e = 0; e < jsonArray.length(); e++) {
                            current.add(jsonArray.getJSONObject(e));
                        }
                        adapter.notifyDataSetChanged();
                    }
                    if (d == days.size()) {
                        st = false;
                        JSONObject j = new JSONObject();
                        JSONArray ja = new JSONArray();
                        j.put("date", today);
                        j.put("info", ja);
                        days.add(j);
                        current.clear();
                        adapter.notifyDataSetChanged();
                    }
                    d++;
                }
                adapter.notifyDataSetChanged();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}

