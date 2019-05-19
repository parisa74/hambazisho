package ir.idpz.hambazisho.service.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vasl.vaslapp.modules.subscriber.global.proto.holder.SubscriberV1;
import com.vaslSdk.utils.ResponseProtoHandler;

import ir.idpz.hambazisho.MyApplication;
import ir.idpz.hambazisho.service.responseModel.LoginRegisterResponse;
import ir.idpz.hambazisho.service.responseModel.LoginValidationResponse;

public class ProjectRepository {

    private static class SingletonHelper {
        private static final ProjectRepository INSTANCE = new ProjectRepository();
    }

    public static ProjectRepository getInstance() {
        return SingletonHelper.INSTANCE;
    }



    /* Login register */
    public LiveData<LoginRegisterResponse> register(String mobileNumber) {
        final MutableLiveData<LoginRegisterResponse> response = new MutableLiveData<>();

        response.setValue(LoginRegisterResponse.loading());

        MyApplication.getVaslAppClient().subscriberServiceV1().registerOperatorSubscriber(mobileNumber, new ResponseProtoHandler<SubscriberV1.RegisterOperatorSubscriber>() {
            @Override
            public void onSuccess(SubscriberV1.RegisterOperatorSubscriber registerOperatorSubscriber) {
                response.setValue(LoginRegisterResponse.success(registerOperatorSubscriber));
            }

            @Override
            public void onFailure(int i, String s) {
                response.setValue(LoginRegisterResponse.error(i, s));
            }
        });

        return response;
    }


    /* Login validation */
    public LiveData<LoginValidationResponse> validation(String activationKey, String mobileNumber) {
        final MutableLiveData<LoginValidationResponse> response = new MutableLiveData<>();

        response.setValue(LoginValidationResponse.loading());


        MyApplication.getVaslAppClient().subscriberServiceV1().validateOperatorSubscriber(activationKey, mobileNumber, new ResponseProtoHandler<SubscriberV1.ValidateOperatorSubscriber>() {
            @Override
            public void onSuccess(SubscriberV1.ValidateOperatorSubscriber validateOperatorSubscriber) {
                response.setValue(LoginValidationResponse.success(validateOperatorSubscriber));
            }

            @Override
            public void onFailure(int i, String s) {
                response.setValue(LoginValidationResponse.error(i, s));
            }
        });

        return response;
    }
}
