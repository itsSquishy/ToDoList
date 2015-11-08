package com.sp.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Muhd on 8/7/2015.
 */
public class SMSActivity extends Activity {

    Button sendButton;
    EditText etPhone;
    EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        etPhone = (EditText) findViewById(R.id.etPhone);
        etMessage = (EditText) findViewById(R.id.etMessage);
        sendButton = (Button) findViewById(R.id.sendButton);

        Intent data = getIntent();
        String message = data.getExtras().getString("text");
        etMessage.setText("I have made a task, " + message + "!");

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendSMSMessage();
                Toast.makeText(getApplicationContext(), "SMS has been sent!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void sendSMSMessage() {
        String phoneNo = etPhone.getText().toString();
        String message = etMessage.getText().toString();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS sending failed, please try again.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
