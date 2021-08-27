package com.example.realproject.ComboPageAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.realproject.R;

import java.util.ArrayList;

public class ComboArrayAdapter  extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> titles;
    private ArrayList<Boolean> isChecked;
    private boolean started = false;
    private Runnable checkIfResponse;

    public ComboArrayAdapter(Context context, ArrayList<String> titles, ArrayList<Boolean> isChecked) {
        super(context, R.layout.listview_combo, R.id.textView_combo_name, titles);
        this.context = context;
        this.titles = titles;
        this.isChecked = isChecked;
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        View singleItem = convertView;
        ComboSingleHolder holder = null;
        if (singleItem == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            singleItem = layoutInflater.inflate(R.layout.listview_combo, parent, false);
            holder = new ComboSingleHolder(singleItem);
            singleItem.setTag(holder);
        } else
            holder = (ComboSingleHolder) singleItem.getTag();


        holder.title.setText(titles.get(position));
        holder.progCheck.setChecked(isChecked.get(position));
        /*holder.progCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String bo = Boolean.toString(isChecked);
                //todo add username
                String switchChanged;
                if (LoginPage.testing)
                    switchChanged = "{messageType:filpTheSwitchResponse,success=true,state:on}";
                else
                    switchChanged = "{messageType:flipTheSwitch, sendToUsername:" + username.get(position) + ",  msg:" + bo + "}";
                try {
                    //Items.vItem.vibrate(100);


                    JSONObject jsonSwitchChanged = new JSONObject(switchChanged);
                    LoginPage.store = "";
                    LoginPage.ws.send(jsonSwitchChanged.toString());

                    checkIfResponse = new Runnable() {
                        @Override
                        public void run() {

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
            }
        });*/


        return singleItem;

    }
}
