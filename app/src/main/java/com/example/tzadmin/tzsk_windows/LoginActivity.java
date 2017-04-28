package com.example.tzadmin.tzsk_windows;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.tzadmin.tzsk_windows.AuthModule.Auth;
import com.example.tzadmin.tzsk_windows.DatabaseModule.Database;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseHelper;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.User;
import android.view.View.OnClickListener;
import com.example.tzadmin.tzsk_windows.HttpModels.HttpResp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.tzadmin.tzsk_windows.AuthModule.Auth.setAuth;

public class LoginActivity extends AppCompatActivity implements OnClickListener {

    EditText tb_login, tb_password;
    Button btn_login;
    int settingLock = 0, id = 0;
    String _login, _password;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initData();
        Auth.resetAuth();

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Database.SetUp(dbHelper.getReadableDatabase());
        User user = Database.lastUserLogin();

        if(user != null) {
            Database.updateUser(user.id, 1);
            startMainActivity(user.id, user.login, user.password, 1);
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idOpt = item.getItemId();
        switch (idOpt) {
            case R.id.btn_setting:
                if(settingLock++ == 11) {
                    Intent intet = new Intent(this, SettingActivity.class);
                    startActivity(intet);
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick (View view) {
        if(!tb_login.getText().toString().equals("") && !tb_password.getText().toString().equals("")) {
            _login = tb_login.getText().toString(); _password = tb_password.getText().toString();
            id = Database.isUserExist(_login, _password);

            if(id != -1) {
                Database.updateUser(id, 1);
                startMainActivity(id, _login, _password, 1);
                return;
            }
            else {
                if (!helper.InetHasConnection(this)) {
                    helper.message(this, helper.MSG.INTERNET_NOT_CONNECTING, Toast.LENGTH_SHORT);
                    return;
                }
                new getAuth().execute(helper.HTTP_QUERY_AUTH, _login, _password);
            }
        }
        else {
            helper.message(this, helper.MSG.EMPTY_AUTH_DATA, Toast.LENGTH_SHORT);
        }
    }

    private void setHttpResp (HttpResp resp) {
        switch (resp.Code) {
            case helper.CODE_RESP_SERVER_OK:
                id = Database.insertUser(_login, _password);
                startMainActivity(id, _login, _password, 1);
                break;
            case helper.CODE_RESP_SERVER_AUTH_ERROR:
                helper.message(this, helper.MSG.INCORRECT_AUTH_DATA, Toast.LENGTH_SHORT);
                break;
            default:
                Toast toast = Toast.makeText(
                        this,
                        "Обратитесь к администратору код ошибки - " + resp.Code,
                        Toast.LENGTH_SHORT);
                toast.show();
                break;
        }
    }

    private void startMainActivity (int id, String login, String password, Integer dateLogin) {
        setAuth(id, login, password, dateLogin);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    class getAuth extends AsyncTask<String, Void, HttpResp> {
        HttpResp resp;

        @Override
        protected void onPreExecute () {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected HttpResp doInBackground(String... params) {
            try {
                URL url = new URL(helper.httpServer + params[helper.HTTP_PARAM_QUERY]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString((params[helper.HTTP_PARAM_LOGIN] + ":" + params[helper.HTTP_PARAM_PASSWORD]).getBytes(), Base64.NO_WRAP ));
                connection.connect();
                resp = new HttpResp();
                resp.Code = connection.getResponseCode();
                resp.Message = connection.getResponseMessage();
                if(resp.Code == helper.CODE_RESP_SERVER_OK)
                    resp.body = helper.streamToString(connection.getInputStream());
                connection.disconnect();
                return resp;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        protected void onPostExecute (HttpResp result) {
            setHttpResp(result);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void initData () {
        tb_login = (EditText)findViewById(R.id.tb_login);
        tb_password = (EditText)findViewById(R.id.tb_password);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.pb_login_activity);
        progressBar.setVisibility(View.INVISIBLE);
    }
}