package com.example.realproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProgramAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<Integer> images;
    ArrayList<String> progName, programDesc, username;
    ArrayList<Boolean> programSwitch;


    public ProgramAdapter(Context context, ArrayList<String> progName, ArrayList<Integer> images, ArrayList<String> programDesc, ArrayList<Boolean> isChecked, ArrayList<String> username) {
        super(context, R.layout.listview_items, R.id.textView1, progName);
        this.context = context;
        this.images = images;
        this.progName = progName;
        this.programDesc = programDesc;
        this.programSwitch = isChecked;
        this.username = username;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View singleItem = convertView;
        ProgramViewHolder holder = null;
        if (singleItem == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            singleItem = layoutInflater.inflate(R.layout.listview_items, parent, false);
            holder = new ProgramViewHolder(singleItem);
            singleItem.setTag(holder);
        } else
            holder = (ProgramViewHolder) singleItem.getTag();


        holder.itemImage.setImageResource(images.get(position));
        holder.progTitle.setText(progName.get(position));
        holder.progDesc.setText(programDesc.get(position));
        holder.progCheck.setChecked(programSwitch.get(position));
        holder.progCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                //todo add username
                String switchChanged = "{messageType:flipTheSwitch, sendToUsername:" + username.get(position) + "  msg:" + isChecked + "}";
                try {
                    JSONObject jsonSwitchChanged = new JSONObject(switchChanged);

                    LoginPage.ws.send(jsonSwitchChanged.toString());
                    System.out.println(LoginPage.store);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return singleItem;
    }
}
