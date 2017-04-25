package com.example.tzadmin.tzsk_windows;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.tzadmin.tzsk_windows.AuthModule.Auth;
import com.example.tzadmin.tzsk_windows.DatabaseModule.Database;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseHelper;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Delivery;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.User;

public class DeliveriesActivity extends AppCompatActivity implements OnItemSelectedListener {

    Delivery delivery;
    TextView tvData;
    Spinner spinStatus;
    String[] status = {"Новая", "В работе", "Исполнена", "Отмена", "Ожидание"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliveries);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Database.SetUp(dbHelper.getReadableDatabase());
        User user = Database.lastUserLogin();

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);

        delivery = Database.selectDelivery(id, Auth.id);
        initData();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initData () {
        tvData = (TextView)findViewById(R.id.tvDeliveryData);
        spinStatus = (Spinner)findViewById(R.id.spinDeliveryStatus);
        spinStatus.setOnItemSelectedListener(this);
        String info = "Доставка на " + delivery.DeliveryDate + "\n\n" +
                delivery.Client + "\n\n" + delivery.Address + "\n\n" +
                delivery.ContactDetails + "\n\n" + "Задание: " + delivery.Task +
                "\n\n" + "Статус";

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, status);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinStatus.setAdapter(adapter);
        spinStatus.setSelection(delivery.Status);
        tvData.setText(info);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delivery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.btn_deliv_close:
                finish();
                break;
            case R.id.btn_deliv_call:
                break;
            case R.id.btn_deliv_camera:
                break;
            case R.id.btn_deliv_geo:
                break;
            case R.id.btn_deliv_ok:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        delivery.Status = position;
        Database.updateDelivery(delivery);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
