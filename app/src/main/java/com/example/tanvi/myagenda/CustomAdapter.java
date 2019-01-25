package com.example.tanvi.myagenda;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tanvi on 4/30/2017.
 */

public class CustomAdapter extends BaseExpandableListAdapter{
    Context context;
    //ArrayList<String> parentList;
    ArrayList<JSONObject> childList;
    ArrayList<JSONObject> days;
    String today;


    public CustomAdapter(Context context, ArrayList<JSONObject> days) {
        this.context = context;
        this.days = days;
        //this.today = today;
    }

    @Override
    public int getGroupCount() {

        return days.size();
    }
    public ArrayList<JSONObject> getArrayList() {

        return days;
    }

    public ArrayList<JSONObject> getChangedArrayList(){
        return days;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try{
            ArrayList<JSONObject> reminders = new ArrayList<>();
            JSONArray j = days.get(groupPosition).getJSONArray("reminders");
            for (int i=0; i<j.length(); i++) {
                reminders.add(j.getJSONObject(i));
            }
            return reminders.size();
        } catch (JSONException e){e.printStackTrace();}
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        try{
            String s =  days.get(groupPosition).getString("name");
            return s;
        } catch (JSONException e){e.printStackTrace();}
        return new Object();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        /*try{
            ArrayList<JSONObject> reminders = new ArrayList<>();
            JSONArray j = days.get(groupPosition).getJSONArray("reminders");
            for (int i=0; i<j.length(); i++) {
                reminders.add(j.getJSONObject(i));
            }
            return reminders.get(childPosition);
        } catch (JSONException e){e.printStackTrace();}*/
        return new Object();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        LayoutInflater i = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = i.inflate(R.layout.layout_parent,null);
        Button add = (Button) layout.findViewById(R.id.button_add);
        ImageView imageView = (ImageView) layout.findViewById(R.id.imageView3);
        imageView.setImageResource(R.drawable.tras);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                days.remove(groupPosition);
                notifyDataSetChanged();
                //Log.d("hey","remove+ "+days.toString());
            }
        });
        //imageView.setImageResource();
        layout.setBackgroundColor(Color.rgb(229,170,250));
        final EditText parentName = (EditText) layout.findViewById(R.id.et_parent);
        parentName.setFocusable(false);
        parentName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    days.get(groupPosition).put("name",s+"");
                    Log.d("cool",days+"");
                } catch (JSONException e){e.printStackTrace();}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        parentName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                parentName.setFocusableInTouchMode(true);
                return false;
            }
        });
        parentName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    parentName.setFocusable(false);
                }
                return false;
            }
        });
        String s = getGroup(groupPosition)+"";
        parentName.setText(s);
        parentName.setTextColor(Color.BLACK);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject j = new JSONObject();
                    j.put("work", "");
                    j.put("due", "");
                    days.get(groupPosition).getJSONArray("reminders").put(j);
                    notifyDataSetChanged();
                }catch (JSONException e){e.printStackTrace();}
            }
        });
        return layout;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        LayoutInflater i = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View layout = i.inflate(R.layout.layout_child,null);
        //layout.setBackgroundColor(Color.rgb(196,239,252));
        layout.setBackgroundColor(Color.rgb(236,203,247));
        EditText w = (EditText) layout.findViewById(R.id.et_work);
        EditText d = (EditText) layout.findViewById(R.id.et_due);
        w.setTextColor(Color.BLACK);
        ImageView imageView = (ImageView) layout.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.tras);
        imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        days.get(groupPosition).getJSONArray("reminders").remove(childPosition);
                        Log.d("hey","remove2 "+days.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    notifyDataSetChanged();
                }
            });
        w.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    days.get(groupPosition).getJSONArray("reminders").getJSONObject(childPosition).put("work",s+"");
                    Log.d("cool",days+"");
                } catch (JSONException e){e.printStackTrace();}
                //notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Log.d("cool",childPosition+"");
            }
        });
        d.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    days.get(groupPosition).getJSONArray("reminders").getJSONObject(childPosition).put("due",s+"");

                } catch (JSONException e){e.printStackTrace();}
                //notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Log.d("cool",childPosition+"");
            }
        });
        try{
            String work = days.get(groupPosition).getJSONArray("reminders").getJSONObject(childPosition).getString("work");
            String due = days.get(groupPosition).getJSONArray("reminders").getJSONObject(childPosition).getString("due");
            w.setText(work);
            d.setText(due);
            Log.d("cool",work+due);
        } catch (JSONException e){e.printStackTrace();}
        w.setTextColor(Color.BLACK);
        d.setTextColor(Color.BLACK);
        return layout;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
