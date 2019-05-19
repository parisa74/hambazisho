package ir.idpz.hambazisho.view.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ir.idpz.hambazisho.Utils.MySharedPreferences;

public class BaseActivity extends AppCompatActivity {

    public MySharedPreferences mySharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SharedPreferences
        mySharedPreferences = MySharedPreferences.getInstance(getApplicationContext());
    }
}
