package com.example.realproject.Items;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realproject.Login.LoginPage;
import com.example.realproject.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemInfo extends AppCompatActivity {

    private TextView name, notes;
    private ImageView image;
    private int imageInt;
    private Button back, edit;
    private String usernameAppliance,nameString,descString;
    private boolean changed = false;
    Runnable checkIfResponse;
    boolean started =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_info);
        back = findViewById(R.id.button_items_back);
        edit = findViewById(R.id.button_edit_item);
        name = findViewById(R.id.textView_appliance_name);
        image = findViewById(R.id.imageView_edititem);
        notes = findViewById(R.id.textView_notes);
        Intent intent = getIntent();
        nameString = intent.getStringExtra("areaInput");
        usernameAppliance = intent.getStringExtra("username");
        descString = intent.getStringExtra("desc");
        imageInt = intent.getIntExtra("picture", -1);


        name.setText(nameString);
        image.setImageResource(imageInt);

       notes.setText("   Notes \n   -Username: "+usernameAppliance);



    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void OnClickInfo(View view) {
        if (view.getId() == back.getId()) {
            //add info
            Intent resultIntent = new Intent();
            //resultIntent.putExtra("type", typeString);
            resultIntent.putExtra("area", name.getText().toString());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
        if (view.getId() == edit.getId()) {
            Intent editItemsIntent = new Intent(this, EditItem.class);
            editItemsIntent.putExtra("area", name.getText().toString());
            editItemsIntent.putExtra("picture",imageInt);
            editItemsIntent.putExtra("username",usernameAppliance);
            editItemsIntent.putExtra("desc",descString);

            startActivityForResult(editItemsIntent, Items.EditItem);
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Handler handler = new Handler();

        if (requestCode == Items.EditItem) {
            if (resultCode == RESULT_OK) {


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
                                    JSONObject jsonObject = new JSONObject(LoginPage.store);
                                    String boo = jsonObject.getString("success");
                                    if (boo.equals("true")) {
                                        String areaString = data.getStringExtra("area");
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                name.setText(areaString);

                                            }
                                        });

                                    } else
                                        Toast.makeText(getApplicationContext(),"Could not edit, please check internet connection",Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (!started)
                            Toast.makeText(getApplicationContext(),"Could not edit, please check internet connection",Toast.LENGTH_SHORT).show();
                    }
                };
                Thread itemsActThread = new Thread(checkIfResponse);
                itemsActThread.start();
            }
            else if(resultCode==LoginPage.ResultRemoved){
                setResult(LoginPage.ResultRemoved);
                finish();
            }
        }
    }

}