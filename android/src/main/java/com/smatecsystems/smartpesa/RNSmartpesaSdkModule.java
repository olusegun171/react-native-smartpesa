
package com.smatecsystems.smartpesa;


import android.support.annotation.Nullable;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import okhttp3.HttpUrl;
import smartpesa.sdk.ServiceManager;
import smartpesa.sdk.ServiceManagerConfig;
import smartpesa.sdk.SmartPesa;
import smartpesa.sdk.error.SpException;
import smartpesa.sdk.models.version.GetVersionCallback;
import smartpesa.sdk.models.version.SpVersionException;
import smartpesa.sdk.models.version.Version;
import smartpesa.sdk.network.NetworkModule;
import smartpesa.sdk.error.SpSessionException;
import smartpesa.sdk.models.merchant.VerifiedMerchantInfo;
import smartpesa.sdk.models.merchant.VerifyMerchantCallback;
import smartpesa.sdk.devices.SpTerminal;
import smartpesa.sdk.error.SpTransactionException;
import smartpesa.sdk.interfaces.TerminalScanningCallback;
import smartpesa.sdk.interfaces.TransactionCallback;
import smartpesa.sdk.models.currency.Currency;
import smartpesa.sdk.models.loyalty.Loyalty;
import smartpesa.sdk.models.loyalty.LoyaltyTransaction;
import smartpesa.sdk.models.transaction.Balance;
import smartpesa.sdk.models.transaction.Card;
import smartpesa.sdk.models.transaction.Transaction;
public class RNSmartpesaSdkModule extends ReactContextBaseJavaModule {

    ServiceManager mServiceManager;
    private static final int REQUEST_PERMISSION = 232;

  private final ReactApplicationContext reactContext;

  public RNSmartpesaSdkModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;

      //      *** initialise ServiceManager...


      //use this to get serviceManager instance anywhere
      //mServiceManager = ServiceManager.get(SplashActivity.this);
      //perform getVersion from init()
  }

    @ReactMethod
    public void init(ReadableMap  params) {

        String merchantCode = params.getString("merchantCode");
        String operatorCode = params.getString("operatorCOde");
        String operatorPin = params.getString("operatorPin");

        ServiceManagerConfig config = ServiceManagerConfig.newBuilder(reactContext)
                .endPoint("netplus.prod.smartpesa.com")
                .withoutSsl()
                .build();
        ServiceManager.init(config);

        //if the merchant is null, take him to login screen
        if(mServiceManager.getCachedMerchant() == null){
            getVersion();
            performVerifyMerchant(merchantCode, operatorCode, operatorPin);

        } else{
            //take the user into the Main Activity
            //dialogBox("Merchant is already logged in, proceed to MainActivity...");
           // return; true;
        }

    }

    @ReactMethod
    private void performVerifyMerchant(String merchantCode, String operatorCode, String operatorPin) {

//      *** initiate the login using SDK
        mServiceManager.verifyMerchant(merchantCode, operatorCode, operatorPin, new VerifyMerchantCallback() {
            @Override
            public void onSuccess(VerifiedMerchantInfo verifiedMerchantInfo) {
                //progressDialog.dismiss();
                //showToast("Login Success");
                //showMainActivity();
                //return true;
            }

            @Override
            public void onError(SpException exception) {
                if(exception instanceof SpSessionException) {
                  //  progressDialog.dismiss();
                    //showToast("Session Expired, proceeding to getVersion i.e. SplashActivity");
                    //showSplashActivity();
                } else {
                   // progressDialog.dismiss();
                    //showToast(exception.getMessage());
                }
            }
        });
    }

@ReactMethod
        private void scanTerminal() {

                mServiceManager.scanTerminal(new TerminalScanningCallback() {
                    @Override
                    public void onDeviceListRefresh(Collection<SpTerminal> collection) {
                        // Here, you can invoke a dialog for the user to select which terminal are they going to use.
                        //displayBluetoothDevice(collection);
                        //return list
                        //performPayment(device);
                    }

                    @Override
                    public void onScanStopped() {
                        // Bluetooth scanning has stopped.
                        // This is the result of calling serviceManager.stopScan();
                    }

                    @Override
                    public void onScanTimeout() {
                        // Bluetooth scanning has timed out.
                        // This is not an exception. You can still start a transaction even though the scanning has timed out.
                    }

                    @Override
                    public void onEnablingBluetooth(String message) {
                        // SDK is turning on the Bluetooth
                    }

                    @Override
                    public void onBluetoothPermissionDenied(String[] permissions) {
                        // On Marshmallow device, this callback indicates that the user has not authorised your app to access Bluetooth capability.
                        // When this method is called. You need to explicitly ask the user to grant the required permissions.
                    }
                });
}
    @Override
    public String getName() {
        return "RNSmartpesaSdk";
    }

    @ReactMethod
    private void performPayment(SpTerminal spTerminal, Double amount) {

//      *** initialise transaction builder
//      After the device is selected, you need to setup the Transaction parameters.
//      This class provides a logical way to provide the parameters to your transaction and ensures that all the required parameters are set and validated.

        SmartPesa.TransactionParam param = SmartPesa.TransactionParam.newBuilder()
                .transactionType(SmartPesa.TransactionType.GOODS_AND_SERVICES)
                .terminal(spTerminal) //terminal selected by user scanTerminal
                .amount(new BigDecimal(Double.valueOf(amount)))
                .build();

//      *** Perform Transaction
//      Please handle UI for other callbacks as needed
        mServiceManager.performTransaction(param, new TransactionCallback() {

            @Override
            public void onProgressTextUpdate(String s) {
               // progressTv.setText(s);
            }


            @Override
            public void onTransactionFinished(SmartPesa.TransactionType transactionType, boolean isSuccess, @Nullable Transaction transaction, @Nullable SmartPesa.Verification verification, @Nullable SpTransactionException exception){

                if(isSuccess){
                   // progressTv.setText("Transaction approved");

                } else{
                   // progressTv.setText(exception.getMessage());
                }
            }

            @Override
            public void onError(SpException exception) {
               // progressTv.setText(exception.getMessage());
            }

            @Override
            public void onDeviceConnected(SpTerminal spTerminal) {

            }

            @Override
            public void onDeviceDisconnected(SpTerminal spTerminal) {

            }

            @Override
            public void onBatteryStatus(SmartPesa.BatteryStatus batteryStatus) {

            }

            @Override
            public void onShowSelectApplicationPrompt(List<String> list) {

            }

            @Override
            public void onWaitingForCard(String s, SmartPesa.CardMode cardMode) {
               // progressTv.setText("Insert/swipe card");
            }

            @Override
            public void onShowInsertChipAlertPrompt() {
               // progressTv.setText("Insert chip card");
            }

            @Override
            public void onReadCard(Card card) {

            }

            @Override
            public void onShowPinAlertPrompt() {

            }

            @Override
            public void onPinEntered() {

            }

            @Override
            public void onShowInputPrompt() {

            }

            @Override
            public void onReturnInputStatus(SmartPesa.InputStatus inputStatus, String s) {

            }

            @Override
            public void onShowConfirmAmountPrompt() {
                //progressTv.setText("Confirm amount on pesaPOD");
            }

            @Override
            public void onAmountConfirmed(boolean b) {

            }


            @Override
            public void onStartPostProcessing(String providerName, Transaction transaction) {

            }

            @Override
            public void onReturnLoyaltyBalance(Loyalty loyalty) {

            }

            @Override
            public void onShowLoyaltyRedeemablePrompt(LoyaltyTransaction loyaltyTransaction) {

            }

            @Override
            public void onLoyaltyCancelled() {

            }

            @Override
            public void onLoyaltyApplied(LoyaltyTransaction loyaltyTransaction) {

            }

        });
    }

    private void getVersion() {
        //mProgressBar.setVisibility(View.VISIBLE);
        //progressTv.setText("Perform getVersion");

//      *** initiate the session
        mServiceManager.getVersion(new GetVersionCallback() {

            public void onSuccess(Version version) {
                //mProgressBar.setVisibility(View.GONE);
                String major = String.valueOf(version.getMajor());
                String minor = String.valueOf(version.getMinor());
                String build = String.valueOf(version.getBuild());
                String a = "GetVersion success! Version: " + major + "." + minor + "." + build;
               // progressTv.setText(a);
                //dialogBox("Merchant not logged in, proceed to LoginActivity...");
            }

            @Override
            public void onError(SpException exception) {
                //mProgressBar.setVisibility(View.GONE);
               // progressTv.setText(exception.getMessage());
            }
        });
    }

}