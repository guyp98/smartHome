package com.example.realproject.Items;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.realproject.PopUp.DeleteContactDialog;
import com.example.realproject.Login.LoginPage;
import com.example.realproject.R;

import org.json.JSONException;
import org.json.JSONObject;

public class EditItem extends AppCompatActivity implements DeleteContactDialog.DeleteDialogListener {
    private EditText nameEdit;
    private ImageView image;
    private String name,desc,usernameAppliance;

    private int imageInt;
    private Button back2Info, deleteContact;
    private boolean changed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        nameEdit = findViewById(R.id.edittext_edit_item_name);
        image = findViewById(R.id.imageView_edititem);
        deleteContact = findViewById(R.id.button_delete_contact);
        back2Info = findViewById(R.id.button_items_back2);

        Intent intent = getIntent();
        name = intent.getStringExtra("area");
        imageInt = intent.getIntExtra("picture", -1);
        usernameAppliance = intent.getStringExtra("username");
        desc = intent.getStringExtra("desc");

        nameEdit.setText(name);
        image.setImageResource(imageInt);

    }


    public void onClickEdit(View view) {
        if (view.getId() == back2Info.getId()) {
            Intent resultIntent = new Intent();
            //resultIntent.putExtra("type", typeString);

            try {

                LoginPage.store = "";

                String jsonLoginStr = "{area:" + name + ", desc: " + desc + "}";
                JSONObject jsonLogin = new JSONObject(jsonLoginStr);

                String jsonLoginStr2;
                if (LoginPage.testing)
                    jsonLoginStr2 = "{messageType:editApplianceResponse ,success:true}";
                else
                    jsonLoginStr2 = "{messageType:editAppliance,username:"+usernameAppliance+", details:" + jsonLogin.toString() + "}";

                JSONObject jsonLogin2 = new JSONObject(jsonLoginStr2);
                if(LoginPage.echo)
                    LoginPage.ws.send(jsonLogin2.toString());
                else
                    LoginPage.store=jsonLogin2.toString();


            } catch (JSONException e) {
                e.getStackTrace();
            }


            resultIntent.putExtra("area", nameEdit.getText().toString());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
        if (view.getId() == deleteContact.getId()) {
            openDeleteDialog();

        }


    }

    public void openDeleteDialog() {
        DeleteContactDialog dialog = new DeleteContactDialog();
        dialog.show(getSupportFragmentManager(), "dans dialog");


    }

    @Override
    public void onYesClicked() {

        try {
            String jsonLoginStr;
            if (LoginPage.testing)
                jsonLoginStr = "{messageType:removeApplianceResponse,removed:true}";
            else
                jsonLoginStr = "{messageType:removeAppliance,username:" + usernameAppliance+"}";
            JSONObject jsonLogin = new JSONObject(jsonLoginStr);
            LoginPage.store = "";

            if(LoginPage.echo)
                LoginPage.ws.send(jsonLogin.toString());
            else
                LoginPage.store=jsonLogin.toString();

        } catch (JSONException e) {
            e.getStackTrace();
        }
        setResult(LoginPage.ResultRemoved);
        finish();
    }
}