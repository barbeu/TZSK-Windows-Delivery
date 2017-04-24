package com.example.tzadmin.tzsk_windows;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SettingActivity extends AppCompatActivity implements OnClickListener {

    Button btn_reconnect;
    EditText tb_port, tb_httpServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        btn_reconnect = (Button)findViewById(R.id.btn_setting_reconnect);
        tb_port = (EditText)findViewById(R.id.tb_setting_port);
        tb_httpServer = (EditText)findViewById(R.id.tb_setting_server);
        btn_reconnect.setOnClickListener(this);
    }

    @Override
    public void onClick (View view) {
        switch (view.getId()) {
            case R.id.btn_setting_reconnect:
                if(!tb_httpServer.toString().equals("") && !tb_port.toString().equals("")) {
                    helper.httpServer = tb_httpServer.getText().toString();
                    helper.port = tb_port.getText().toString();
                    finish();
                }
                break;
        }
    }
}
