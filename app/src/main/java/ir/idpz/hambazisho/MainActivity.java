package ir.idpz.hambazisho;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.vasl.vaslapp.modules.game.global.proto.holder.Game;
import com.vaslSdk.utils.ResponseProtoHandler;

import java.util.List;

import ir.idpz.hambazisho.service.responseModel.Action;
import ir.idpz.hambazisho.view.ui.activity.BaseActivity;

public class MainActivity extends BaseActivity {

    boolean isLiked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView img_like = findViewById(R.id.img_like);
        TextView tv_likeCounter = findViewById(R.id.tv_likeCounter);
        TextView tv_rate = findViewById(R.id.tv_rate);


        img_like.setOnClickListener(view -> {
            if (!isLiked) {
                MyApplication.getVaslAppClient().gameActionServiceV2().dynamicRaiseAction("1", Action.LIKE, null, new ResponseProtoHandler<Game.RaiseAction>() {
                    @Override
                    public void onSuccess(Game.RaiseAction raiseAction) {
                        List<Game.VariableValue> dataList = raiseAction.getDataList();
                        for (Game.VariableValue variableValue : dataList){

                            if (variableValue.getTitle().equalsIgnoreCase("like"))
                                tv_likeCounter.setText(String.valueOf(variableValue.getValue()));

                        }
                        img_like.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.videoLiked), PorterDuff.Mode.SRC_IN);
                        isLiked = true;
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(MainActivity.this, ""+s, Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                MyApplication.getVaslAppClient().gameActionServiceV2().dynamicRaiseAction("2", Action.LIKE, null, new ResponseProtoHandler<Game.RaiseAction>() {
                    @Override
                    public void onSuccess(Game.RaiseAction raiseAction) {
                        List<Game.VariableValue> dataList = raiseAction.getDataList();
                        for (Game.VariableValue variableValue : dataList){

                            if (variableValue.getTitle().equalsIgnoreCase("like"))
                                tv_likeCounter.setText(String.valueOf(variableValue.getValue()));

                        }
                        img_like.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.videoDisliked), PorterDuff.Mode.SRC_IN);
                        isLiked = true;
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(MainActivity.this, ""+s, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
