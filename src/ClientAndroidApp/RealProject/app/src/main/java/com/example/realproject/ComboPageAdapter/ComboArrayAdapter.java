package com.example.realproject.ComboPageAdapter;

import android.content.Context;
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
        ComboSingleHolder finalHolder = holder;
        holder.progCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                String bo = Boolean.toString(isChecked);
                //todo add username
                String switchChanged;
                JSONObject jsonSend = new JSONObject();
                    if (LoginPage.testing){
                        jsonSend.put("messageType","group");
                        jsonSend.put("groupName", finalHolder.title.getText());
                        if(isChecked)
                            jsonSend.put("action","groupScenarioOn");
                        else
                            jsonSend.put("action","groupScenarioOff");
                        jsonSend.put("messageType","group");

                    }
                    else {
                        jsonSend.put("messageType", "groupResponse");
                        jsonSend.put("success","true");
                    }

                    //Items.vItem.vibrate(100);


                    LoginPage.store = "";
                    if(LoginPage.echo)
                        LoginPage.ws.send(jsonSend.toString());
                    else
                        LoginPage.store = jsonSend.toString();

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
        });
        return singleItem;

    }
}
