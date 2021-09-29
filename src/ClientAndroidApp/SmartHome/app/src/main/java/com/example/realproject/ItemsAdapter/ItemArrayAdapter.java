package com.example.realproject.ItemsAdapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import com.example.realproject.Login.LoginPage;
import com.example.realproject.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemArrayAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<Integer> images;
    ArrayList<String> progName, programDesc, username;
    ArrayList<Boolean> programSwitch;
    private int loadedIndex = 0;
    private Activity itemsActivity;
    private boolean started=false;
    private Runnable checkIfResponse;



    public ItemArrayAdapter(Context context, ArrayList<String> progName, ArrayList<Integer> images, ArrayList<String> programDesc, ArrayList<Boolean> isChecked, ArrayList<String> username) {
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
        ItemSingleHolder holder = null;
        if (singleItem == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            singleItem = layoutInflater.inflate(R.layout.listview_items, parent, false);
            holder = new ItemSingleHolder(singleItem);
            singleItem.setTag(holder);
        } else
            holder = (ItemSingleHolder) singleItem.getTag();


        holder.itemImage.setImageResource(images.get(position));
        holder.progTitle.setText(progName.get(position));
        holder.progDesc.setText(programDesc.get(position));
        holder.progCheck.setChecked(programSwitch.get(position));
        holder.progCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                Log.d("onCheckedChanged", "out of Synchronized");
               // synchronized (LoginPage.switchLock) {
                    Log.d("onCheckedChanged", "in Synchronized");
                    String bo = Boolean.toString(isChecked);
                    //todo add username
                    String switchChanged;
                    if (LoginPage.testing)
                        switchChanged = "{messageType:filpTheSwitchResponse,success=true,state:on}";
                    else
                        switchChanged = "{messageType:flipTheSwitch, sendToUsername:\"" + username.get(position) + "\",  msg:" + bo + "}";
                    try {
                        //Items.vItem.vibrate(100);


                        JSONObject jsonSwitchChanged = new JSONObject(switchChanged);
                        LoginPage.store = "";
                        if(LoginPage.echo)
                             LoginPage.ws.send(jsonSwitchChanged.toString());
                        else
                            LoginPage.store=jsonSwitchChanged.toString();

                        checkIfResponse = new Runnable() {
                            @Override
                            public void run() {
                                started=false;
                                    for (int i = 0; i < LoginPage.threadCycle & !started; i++) {

                                        try {
                                            Thread.sleep(LoginPage.threadSleep);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        if (!LoginPage.store.isEmpty()) {
                                            started = true;


                                            try {
                                                //maybe also send position and make sure i get it back.
                                                JSONObject jsonObject = new JSONObject(LoginPage.store);
                                                String boo = jsonObject.getString("success");
                                                if (boo.equals("true")) {


                                                } else {

                                                    programSwitch.set(position, !programSwitch.get(position));


                                                }


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                        };
                        Thread itemsActThread = new Thread(checkIfResponse);
                        itemsActThread.start();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            //}
        });
        return singleItem;
    }



}
