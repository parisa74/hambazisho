package ir.idpz.hambazisho.view.ui.activity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.idpz.hambazisho.MainActivity;
import ir.idpz.hambazisho.R;
import ir.idpz.hambazisho.Utils.Global;
import ir.idpz.hambazisho.service.responseModel.LoginRegisterResponse;
import ir.idpz.hambazisho.service.responseModel.LoginValidationResponse;
import ir.idpz.hambazisho.service.responseModel.Status;
import ir.idpz.hambazisho.viewModel.LoginViewModel;
import swarajsaaj.smscodereader.interfaces.OTPListener;
import swarajsaaj.smscodereader.receivers.OtpReader;

public class LoginActivity extends BaseActivity implements View.OnClickListener, OTPListener {

    private static final String ACTIVITY_NAME = LoginActivity.class.getSimpleName();
    private LoginViewModel viewModel;

    private View insertMobileContainer, insertVerificationCodeContainer;
    private EditText input_mobileNumber, input_verificationCode;
    private Button btn_submitMobileNumber, btn_submitVerificationCode;
    private TextView tv_modifyMobileNumber, tv_mobileNumber, tv_repeatSend, tv_counter;
    private ProgressBar prgbr_sendVerificationCode, prgbr_checkVerificationCode;
    private String mobileNumber;
    private String verificationCode_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        /* Init viewModel */
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);


        /* Bind views */
        bindViews();


        /* Clicks handler */
        clickHandler();


        /* Get permission for read sms */
        if (!hasPermissions(this, OTP_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, OTP_PERMISSIONS, OTP_PERMISSION_REQUEST_CODE);
        }


        /* Bind OtpReader to activity for read sms */
        OtpReader.bind(this, Global.SMS_SENDER_NUMBER);


        /* Inputs add text change listener */
        input_mobileNumber.addTextChangedListener(new MyTextWatcher(input_mobileNumber));
        input_verificationCode.addTextChangedListener(new MyTextWatcher(input_verificationCode));
    }


    /* Bind views */
    private void bindViews() {
        insertMobileContainer = findViewById(R.id.insertMobileContainer);
        insertVerificationCodeContainer = findViewById(R.id.insertVerificationCodeContainer);

        input_mobileNumber = findViewById(R.id.input_mobileNumber);
        btn_submitMobileNumber = findViewById(R.id.btn_submitMobileNumber);
        prgbr_sendVerificationCode = findViewById(R.id.prgbr_sendVerificationCode);

        tv_modifyMobileNumber = findViewById(R.id.tv_modifyMobileNumber);
        tv_mobileNumber = findViewById(R.id.tv_mobileNumber);
        tv_repeatSend = findViewById(R.id.tv_repeatSend);
        input_verificationCode = findViewById(R.id.input_verificationCode);
        btn_submitVerificationCode = findViewById(R.id.btn_submitVerificationCode);
        tv_counter = findViewById(R.id.tv_counter);
        prgbr_checkVerificationCode = findViewById(R.id.prgbr_checkVerificationCode);
    }


    /* Click handler */
    private void clickHandler() {
        btn_submitMobileNumber.setOnClickListener(this);
        tv_modifyMobileNumber.setOnClickListener(this);
        tv_repeatSend.setOnClickListener(this);
        btn_submitVerificationCode.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submitMobileNumber:
                submitMobileNumber();

                break;
            case R.id.tv_modifyMobileNumber:
                // change View
                insertMobileContainer.setVisibility(View.VISIBLE);
                insertVerificationCodeContainer.setVisibility(View.GONE);

                break;
            case R.id.tv_repeatSend:
                // restart counter
                counterHandler();

                // send verification code by sms
                sendVerificationCode();

                break;
            case R.id.btn_submitVerificationCode:
                submitVerificationCode();

                break;
            default:
                break;
        }
    }


    /* submit mobile number */
    private void submitMobileNumber() {
        viewModel.register(mobileNumber).observe(this, this::consumeRegisterResponse);
    }


    /* consume register response */
    private void consumeRegisterResponse(@NonNull LoginRegisterResponse response) {
        switch (response.status) {
            case LOADING:
                setRegisterLoadingState();

                break;
            case SUCCESS:
                cancelRegisterLoadingState();

                // change View
                insertMobileContainer.setVisibility(View.GONE);
                insertVerificationCodeContainer.setVisibility(View.VISIBLE);

                // set mobile number in textView
                tv_mobileNumber.setText(mobileNumber);

                // start counter
                counterHandler();

                break;
            case ERROR:
                cancelRegisterLoadingState();

                Toast.makeText(this, "" + response.errorDescription, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


    /* set loading state for register */
    private void setRegisterLoadingState() {
        // show progressBar
        prgbr_sendVerificationCode.setVisibility(View.VISIBLE);

        // disable btn
        btn_submitMobileNumber.setEnabled(false);
    }


    /* cancel loading state for register */
    private void cancelRegisterLoadingState() {
        // invisible progressBar
        prgbr_sendVerificationCode.setVisibility(View.INVISIBLE);

        // enable btn
        btn_submitMobileNumber.setEnabled(true);
    }


    /* send verification code */
    private void sendVerificationCode() {
        viewModel.register(mobileNumber).observe(this, this::consumeSendVerificationCodeResponse);
    }


    /* consume send verification code response */
    private void consumeSendVerificationCodeResponse(@NonNull LoginRegisterResponse response) {
        if (response.status == Status.ERROR) {
            Toast.makeText(this, "" + response.errorDescription, Toast.LENGTH_SHORT).show();
        }
    }


    /* submit verification code */
    private void submitVerificationCode() {
        // get inputVerificationCode text
        verificationCode_user = input_verificationCode.getText().toString().trim();

        viewModel.validation(verificationCode_user, mobileNumber).observe(this, this::consumeValidationResponse);
    }


    /* consume validation response */
    private void consumeValidationResponse(@NonNull LoginValidationResponse response) {
        switch (response.status) {
            case LOADING:
                setVerificationLoadingState();

                break;
            case SUCCESS:
                // unBind otpReader
                OtpReader.bind(null, "");

                // go to home page
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();

                break;
            case ERROR:
                cancelVerificationLoadingState();

                Toast.makeText(this, "" + response.errorDescription, Toast.LENGTH_SHORT).show();

                break;
            default:
                break;
        }
    }


    /* set loading state for verification */
    private void setVerificationLoadingState() {
        // show progressBar
        prgbr_checkVerificationCode.setVisibility(View.VISIBLE);

        // disable btn
        btn_submitVerificationCode.setEnabled(false);
    }


    /* cancel loading state for verification */
    private void cancelVerificationLoadingState() {
        // invisible progress
        prgbr_checkVerificationCode.setVisibility(View.INVISIBLE);

        // enable btn
        btn_submitVerificationCode.setEnabled(true);
    }


    /* counter handler */
    private void counterHandler() {
        tv_counter.setVisibility(View.VISIBLE);
        tv_repeatSend.setVisibility(View.INVISIBLE);
        tv_modifyMobileNumber.setVisibility(View.INVISIBLE);

        // Setup counter
        tv_counter.setText(convertMillisToMinutes(Global.REPEAT_VERIFICATION_CODE_SEND_DISPLAY_LENGTH));

        CountDownTimer countDownTimer = new CountDownTimer(Global.REPEAT_VERIFICATION_CODE_SEND_DISPLAY_LENGTH, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_counter.setText(convertMillisToMinutes(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                tv_counter.setVisibility(View.INVISIBLE);
                tv_repeatSend.setVisibility(View.VISIBLE);
                tv_modifyMobileNumber.setVisibility(View.VISIBLE);
            }
        };

        countDownTimer.start();
    }


    /* convert millis to minutes */
    private String convertMillisToMinutes(long millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        );
    }


    /* Text watcher class */
    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_mobileNumber:
                    btn_submitMobileNumber.setEnabled(false);

                    mobileNumber = input_mobileNumber.getText().toString().trim();
                    if (mobileNumber.length() == 11) {
                        btn_submitMobileNumber.setEnabled(true);
                    }
                    break;
                case R.id.input_verificationCode:
                    btn_submitVerificationCode.setEnabled(false);

                    verificationCode_user = input_verificationCode.getText().toString().trim();
                    if (verificationCode_user.length() == 4) {
                        btn_submitVerificationCode.setEnabled(true);
                    }
                    break;
            }
        }
    }


    //**************** SMS required function *********************
    /**
     * need for Android 6 real time permissions
     */

    private static final String TAG_OTP = "OTP_SMS";
    int OTP_PERMISSION_REQUEST_CODE = 100;
    String[] OTP_PERMISSIONS = {Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS};

    public static boolean hasPermissions(@NonNull Context context, @NonNull String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG_OTP, "hasPermissions: !!!" + permission);
                return false;
            }
        }
        Log.d(TAG_OTP, "hasPermissions: has all permissions");
        return true;
    }

    @Override
    public void otpReceived(String smsText) {
        /* Do whatever you want to do with the sms text*/
        // Check permissions
        if (hasPermissions(this, OTP_PERMISSIONS)) {
            input_verificationCode.setText(parseCode(smsText)); //set code in edit text

            submitVerificationCode();
        }
    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }
}
