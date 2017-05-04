package com.example.tzadmin.tzsk_windows;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tzadmin.tzsk_windows.AuthModule.Auth;
import com.example.tzadmin.tzsk_windows.DatabaseModule.Database;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseHelper;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.ChangedData;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Delivery;
import com.example.tzadmin.tzsk_windows.Location.MyLocation;

public class DeliveriesActivity extends AppCompatActivity implements OnItemSelectedListener {

    Delivery delivery;
    TextView tvData;
    Spinner spinStatus;
    String[] status = {"Новая", "В работе", "Исполнена", "Отмена", "Ожидание"};
    int first_status;
    AlertDialog.Builder alertd;
    Context context;
    EditText tb_summ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliveries);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Database.SetUp(dbHelper.getReadableDatabase());

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);

        delivery = Database.selectDelivery(id, Auth.id);
        initData();
        first_status = delivery.Status;
        initAlertDialog();
    }

    @Override
    public void onBackPressed() {
        if(first_status != delivery.Status ||
                Integer.parseInt(tb_summ.getText().toString()) != delivery.Summ) {
            alertd.show();
        } else {
            finish();
        }
    }

    private void initData () {
        tb_summ = (EditText)findViewById(R.id.tv_delivery_summ);
        tvData = (TextView)findViewById(R.id.tvDeliveryData);
        spinStatus = (Spinner)findViewById(R.id.spinDeliveryStatus);
        spinStatus.setOnItemSelectedListener(this);
        String date = delivery.DeliveryDate;
        String info = "Доставка на " + date.substring(0,10) + "\n\n" +
                delivery.Client + "\n\n" + delivery.Address + "\n\n" +
                delivery.ContactDetails + "\n\n" + "Задание: " + delivery.Task +
                "\n\n";
        tb_summ.setText(String.valueOf(delivery.Summ));
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
        int idDelivery = delivery.id;
        switch (id) {
            case R.id.btn_deliv_close:
                finish();
                break;
            case R.id.btn_deliv_call:
                Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" +delivery.ContactDetails));
                startActivity(intentCall);
                break;
            case R.id.btn_deliv_camera:
                Intent intentCamera = new Intent(this, CameraActivity.class);
                intentCamera.putExtra("id", idDelivery);
                startActivity(intentCamera);
                break;
            case R.id.btn_deliv_geo:
                if(MyLocation.Longitude != null && MyLocation.Latitude != null) {
                    if(helper.InetHasConnection(this)) {
                        Intent intentGeo = new Intent(this, MapsActivity.class);
                        intentGeo.putExtra("id", idDelivery);
                        startActivity(intentGeo);
                    } else
                        helper.message(this, helper.MSG.INTERNET_NOT_CONNECTING, Toast.LENGTH_SHORT);
                } else
                    helper.message(this, helper.MSG.POWER_SEND_GEODATA, Toast.LENGTH_SHORT);
                break;
            case R.id.btn_deliv_ok:
                sendDataAndFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0 && delivery.Status != 0) {
            helper.message(this, helper.MSG.INCORRECT_SPINNER_ITEM, Toast.LENGTH_SHORT);
            spinStatus.setSelection(delivery.Status);
            return;
        }
        delivery.Status = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void commit () {
        if(first_status != delivery.Status ||
                Integer.parseInt(tb_summ.getText().toString()) != delivery.Summ) {

            first_status = delivery.Status;
            delivery.Summ = Integer.parseInt(tb_summ.getText().toString());

            ChangedData data = new ChangedData();
            data.idUser = Auth.id;
            data.SerialNumber = delivery.SerialNumber;
            data.DocID = delivery.DocID;
            data.Status = delivery.Status;
            data.summ = delivery.Summ;
            data.Date = helper.Date();
            Database.insertDataChanged(data);
            Database.updateDelivery(delivery);
        }
    }

    private void sendDataAndFinish() {
        commit();
        finish();
    }

    private void initAlertDialog () {
        context = DeliveriesActivity.this;
        String title = "Были внесены изменения";
        String message = "Отправить данные на сервер?";
        String button1String = "Да";
        String button2String = "Нет";

        alertd = new AlertDialog.Builder(context);
        alertd.setTitle(title);
        alertd.setMessage(message);
        alertd.setPositiveButton(button1String, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int arg1) {
                sendDataAndFinish();
            }
        });
        alertd.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                finish();
            }
        });
        alertd.setCancelable(false);
    }
}
