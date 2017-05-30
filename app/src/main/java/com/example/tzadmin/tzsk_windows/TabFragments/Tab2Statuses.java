package com.example.tzadmin.tzsk_windows.TabFragments;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tzadmin.tzsk_windows.AuthModule.Auth;
import com.example.tzadmin.tzsk_windows.DatabaseModule.Database;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.ChangedData;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Delivery;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.StatusParam;
import com.example.tzadmin.tzsk_windows.DatabaseModule.DatabaseModels.Switches;
import com.example.tzadmin.tzsk_windows.JsonModule.JSON;
import com.example.tzadmin.tzsk_windows.R;
import com.example.tzadmin.tzsk_windows.helper;
import com.github.kevinsawicki.http.HttpRequest;
import java.util.Date;

/**
 * Created by tzadmin on 06.05.17.
 */

public class Tab2Statuses extends Fragment {

    View rootView;
    TextView tb_odmtr, tb_mileage, tb_mileOdmtr;
    EditText et_valueOdmtr;
    Switch getStartedSwitch, finishUnloadingSwitch, finishWorkSwitch;
    String DocID = null;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab2statuses, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb_tab2status);
        progressBar.setVisibility(View.INVISIBLE);
        initializeComponents(rootView);
        return rootView;
    }

    public void saveStateSwitches() {
        if(DocID == null)
            return;
        Database.deleteSwitches(Auth.id, DocID);
        Switches switches = new Switches();
        switches.idUser = Auth.id;
        switches.DocID = DocID;
        switches.getStarted = getStartedSwitch.isChecked() == true ? 1 : 0;
        switches.finishUnloading = finishUnloadingSwitch.isChecked() == true ? 1 : 0;
        switches.finishWork = finishWorkSwitch.isChecked() == true ? 1 : 0;
        switches.valueOdmtr = et_valueOdmtr.getText().toString().equals("") ? "" : et_valueOdmtr.getText().toString();
        Database.insertSwitches(switches);
    }

    private void initializeComponents(View root) {
        tb_odmtr = (TextView) root.findViewById(R.id.tb_status_odmtr);
        tb_mileage = (TextView) root.findViewById(R.id.tb_status_mileage);
        tb_mileOdmtr = (TextView) root.findViewById(R.id.tb_status_mile_odmtr);
        et_valueOdmtr = (EditText) root.findViewById(R.id.et_status_valueOdmtr);
        helper.setFilterEditBox(et_valueOdmtr, 9);
        getStartedSwitch = (Switch) root.findViewById(R.id.switch_getStarted);
        finishUnloadingSwitch = (Switch) root.findViewById(R.id.switch_finishUnloading);
        finishWorkSwitch = (Switch) root.findViewById(R.id.switch_finishWork);
    }

    private ChangedData generateChengedData (int status, int summ) {
        ChangedData data = new ChangedData();
        data.isGlobal = 1;
        data.DocID = DocID;
        data.idUser = Auth.id;
        data.Date = helper.Date(new Date());
        data.summ = summ;
        data.Status = status;
        data.SerialNumber = "";
        return data;
    }

    private void eventUnSubscriptionChecked () {
        getStartedSwitch.setOnCheckedChangeListener(null);
        finishUnloadingSwitch.setOnCheckedChangeListener(null);
        finishWorkSwitch.setOnCheckedChangeListener(null);
    }

    private void eventSubscriptionChecked() {
        getStartedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked) {
                   Database.insertDataChanged(
                           generateChengedData(helper.INDEX_STATUS_GET_STARTED, -1));
                   saveStateSwitches();
               } else
                   getStartedSwitch.setChecked(true);
            }
        });

        finishUnloadingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if(getStartedSwitch.isChecked()) {
                        Database.insertDataChanged(
                                generateChengedData(helper.INDEX_STATUS_FINISH_UNLOADING, -1));
                        saveStateSwitches();
                        Database.updateStatusDelivery(DocID, 1);
                        Delivery delivery = Database.selectDelivery(Auth.id, DocID, 1);
                        ChangedData data = new ChangedData();
                        data.idUser = Auth.id;
                        data.isGlobal = 0;
                        data.SerialNumber = delivery.SerialNumber;
                        data.DocID = delivery.DocID;
                        data.Status = delivery.Status;
                        data.summ = delivery.Summ;
                        data.Date = delivery.DeliveryDate;
                        Database.insertDataChanged(data);
                    } else
                        finishUnloadingSwitch.setChecked(false);
                } else
                    finishUnloadingSwitch.setChecked(true);
            }
        });

        finishWorkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if(finishUnloadingSwitch.isChecked()) {
                        if (!et_valueOdmtr.getText().toString().equals("")) {
                            Database.insertDataChanged(
                                    generateChengedData(
                                            helper.INDEX_STATUS_FINISH_WORK,
                                            Integer.parseInt(et_valueOdmtr.getText().toString())
                                    ));
                            saveStateSwitches();
                        } else {
                            helper.message(getActivity(), helper.MSG.ERROR_ODMTR_VALUE_NULLABLE, Toast.LENGTH_SHORT);
                            finishWorkSwitch.setChecked(false);
                        }
                    } else
                        finishWorkSwitch.setChecked(false);
                } else
                    finishWorkSwitch.setChecked(true);
            }
        });
    }

    private void refreshStatuses (String jsonStatuses) {
        StatusParam statusParam = JSON.parseStatusParam(jsonStatuses);

        if(statusParam != null) {
            Database.insertStatusParam(statusParam);
        }

        statusParam = Database.selectStatusParam(Auth.id, DocID);
        if (statusParam == null) {
            return;
        }

        tb_mileage.setText("Одометр на начало дня - " + statusParam.AllMileage);
        tb_odmtr.setText("Километраж - " + statusParam.AllOdmtr);
        tb_mileOdmtr.setText("Расчетное значение на конец дня - " + (statusParam.AllMileage + statusParam.AllOdmtr));
    }

    private void defaultValues() {
        getStartedSwitch.setChecked(false);
        finishUnloadingSwitch.setChecked(false);
        finishWorkSwitch.setChecked(false);
        et_valueOdmtr.setText("");
        tb_odmtr.setText("");
        tb_mileage.setText("");
        tb_mileOdmtr.setText("");
    }

    public void reloadStatuses (String DocID) {
        if(DocID == null || DocID.toString().equals("")) {
            eventUnSubscriptionChecked();
            defaultValues();
            return;
        }

        if(!helper.InetHasConnection(getActivity()))
            helper.message(getActivity(), helper.MSG.INTERNET_NOT_CONNECTING, Toast.LENGTH_LONG);

        this.DocID = DocID;

        eventUnSubscriptionChecked();

        Switches switchesState = Database.selectSwitches(Auth.id, DocID);
        if(switchesState != null) {
            getStartedSwitch.setChecked(switchesState.getStarted == 1 ? true : false);
            finishUnloadingSwitch.setChecked(switchesState.finishUnloading == 1 ? true : false);
            finishWorkSwitch.setChecked(switchesState.finishWork == 1 ? true : false);
            et_valueOdmtr.setText(switchesState.valueOdmtr);
        } else
            defaultValues();

        eventSubscriptionChecked();

        new downloadStatuses().execute();
    }

    class downloadStatuses extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {

            String JsonStringRequest =
                    JSON.generateStatusParam(DocID);

            String result = null;
            try {
                result = helper.streamToString(
                        HttpRequest.post(helper.httpServer + helper.HTTP_QUERY_GET_STATUS_PARAM)
                                .basic(Auth.login, Auth.passwd)
                                .send(JsonStringRequest)
                                .stream()
                );
            } catch (Exception e) {
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            refreshStatuses(result);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
