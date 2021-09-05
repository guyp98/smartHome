package com.example.realproject.ComboAddAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.realproject.Combos.ComboAddPage;
import com.example.realproject.Combos.ComboItem;
import com.example.realproject.Combos.ComboPage;
import com.example.realproject.Items.Items;
import com.example.realproject.ItemsAdapter.ItemSingleHolder;
import com.example.realproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ComboAddAdapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<Integer> images;
    ArrayList<String> progName, programDesc;
    ArrayList<Boolean> programSwitch;
    private int check = 0;
    private Activity itemsActivity;
    private boolean started = false;
    private Runnable checkIfResponse;
    private ArrayAdapter options;
    private String groupName;


    public ComboAddAdapter(Context context, ArrayList<String> progName, ArrayList<Integer> images, ArrayList<Boolean> isChecked, ArrayAdapter options,String groupName) {
        super(context, R.layout.listview_single_item_group, R.id.textView_title_group, progName);
        this.context = context;
        this.images = images;
        this.progName = progName;
        this.programSwitch = isChecked;
        this.options=options;
        this.groupName = groupName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View singleItem = convertView;
        ComboAddHolder holder = null;
        if (singleItem == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            singleItem = layoutInflater.inflate(R.layout.listview_single_item_group, parent, false);
            holder = new ComboAddHolder(singleItem);
            singleItem.setTag(holder);
        } else
            holder = (ComboAddHolder) singleItem.getTag();


        options.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ComboAddHolder finalHolder = holder;

        holder.dropDownScenarioOn.setAdapter(options);
        ComboAddHolder finalHolder2 = holder;
        holder.dropDownScenarioOn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
                check++;
                if (check > progName.size()) {
                    ((TextView) parent.getChildAt(0)).setTextSize(18);
                    String jsonMessage = "", jsonMessage2 = "";
                    //we want to replace the appliance, so if it exist we will delete the former and add a new one
                    if (ComboPage.checkedApplianceInt.contains(position)) {
                        for (ComboItem appliance : ComboPage.checkedAppliances) {
                            String a = appliance.getUsername();
                            String b = Items.username.get(position);
                            if (a.equals(b)) {
                                ComboPage.checkedAppliances.remove(appliance);
                                break;
                            }

                        }


                        if (finalHolder2.dropDownScenarioOn.getSelectedItem().toString().equals("On")) {
                            jsonMessage = "{messageType:flipTheSwitch, sendToUsername:\"" + Items.username.get(position) + "\",  msg:true}";
                            jsonMessage2 = "{messageType:flipTheSwitch, sendToUsername:\"" + Items.username.get(position) + "\",  msg:false}";

                        } else {
                            jsonMessage = "{messageType:flipTheSwitch, sendToUsername:\"" + Items.username.get(position) + "\",  msg:false}";
                            jsonMessage2 = "{messageType:flipTheSwitch, sendToUsername:\"" + Items.username.get(position) + "\",  msg:true}";

                        }
                        try {
                            JSONObject jsonFlip = new JSONObject(jsonMessage);
                            JSONObject jsonFlip2 = new JSONObject(jsonMessage2);

                            ComboPage.checkedAppliances.add(new ComboItem(Items.username.get(position), jsonFlip.toString(), jsonFlip2.toString()));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
        holder.itemImage.setImageResource(images.get(position));
        holder.title.setText(progName.get(position));
        try {
        if(groupName!=null) {
            for(ComboItem cm : Items.groups.get(groupName)) {

                String d = holder.title.getText().toString();
                String g = Items.getAreaFromUsername(cm.getUsername());
                if (Items.getAreaFromUsername(cm.getUsername()).equals(holder.title.getText().toString()))
                {
                    holder.progCheck.setChecked(true);
                    JSONObject scenario = new JSONObject(cm.getScenarioOn());
                    if ("true".equals(scenario.getString("msg"))){
                        holder.dropDownScenarioOn.setSelection(0);
                    }
                    else
                        holder.dropDownScenarioOn.setSelection(1);
                }




            }
        }else{

            holder.progCheck.setChecked(programSwitch.get(position));




        }
        } catch (JSONException e) {
            e.printStackTrace();
        }







        ComboAddHolder finalHolder1 = holder;
        holder.progCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String jsonMessage="",jsonMessage2="";

                if(isChecked) {
                    ComboPage.checkedApplianceInt.add(position);
                    if( finalHolder1.dropDownScenarioOn.getSelectedItem().toString().equals("On")) {
                        jsonMessage = "{messageType:flipTheSwitch, sendToUsername:\"" + Items.username.get(position) + "\",  msg:true}";
                        jsonMessage2 = "{messageType:flipTheSwitch, sendToUsername:\"" + Items.username.get(position) + "\",  msg:false}";

                    }

                    else {
                        jsonMessage = "{messageType:flipTheSwitch, sendToUsername:\"" + Items.username.get(position) + "\",  msg:false}";
                        jsonMessage2 = "{messageType:flipTheSwitch, sendToUsername:\"" + Items.username.get(position) + "\",  msg:true}";

                    }
                    try {
                        JSONObject jsonFlip = new JSONObject(jsonMessage);
                        JSONObject jsonFlip2 = new JSONObject(jsonMessage2);


                        ComboPage.checkedAppliances.add(new ComboItem(Items.username.get(position),jsonFlip.toString(),jsonFlip2.toString()));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    for (ComboItem appliance: ComboPage.checkedAppliances){

                        if(appliance.getUsername() == Items.username.get(position))
                        {
                            ComboPage.checkedAppliances.remove(appliance);
                        }
                        break;
                    }

                    ComboPage.checkedApplianceInt.remove(position);

                }
            }

        });



        return singleItem;
    }

}
