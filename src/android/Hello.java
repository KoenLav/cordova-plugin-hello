package com.mrwinston.mypos;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.UUID;

import com.mrwinston.icardpossdk.BluetoothDevicesDialog;
import com.mrwinston.icardpossdk.ConnectionListener;
import com.mrwinston.icardpossdk.Currency;
import com.mrwinston.icardpossdk.POSHandler;
import com.mrwinston.icardpossdk.TransactionData;

public class Hello extends CordovaPlugin {
    private POSHandler mPOSHandler;

    public static void main (String[] args) {

    }

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        callbackContext.success(action);

        return true;

        if (action.equals("greet")) {
            POSHandler.setCurrency(Currency.EUR);
            mPOSHandler = POSHandler.getInstance();

            BluetoothDevicesDialog dialog = new BluetoothDevicesDialog(this);
            dialog.show();

            mPOSHandler.setConnectionListener(new ConnectionListener() {
                @Override
                public void onConnected(final BluetoothDevice device) {
                    if( POSHandler.getInstance().isConnected()){
                        mPOSHandler.purchase(
                            "12.12",
                            UUID.randomUUID().toString() /*transaction reference*/,
                            POSHandler.RECEIPT_PRINT_AUTOMATICALLY /*receipt configuration*/
                        );
                    }
                }
            });

            String name = data.getString(0);
            String message = "Hello, " + name;
            callbackContext.success(message);

            return true;
        } else {
            return false;
        }
    }
}
