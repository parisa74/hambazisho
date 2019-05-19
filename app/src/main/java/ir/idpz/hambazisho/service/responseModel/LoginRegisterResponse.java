package ir.idpz.hambazisho.service.responseModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vasl.vaslapp.modules.subscriber.global.proto.holder.SubscriberV1;

public class LoginRegisterResponse {
    public final Status status;

    @Nullable
    public final SubscriberV1.RegisterOperatorSubscriber data;

    @Nullable
    public final Integer errorCode;

    @Nullable
    public final String errorDescription;



    private LoginRegisterResponse(Status status, @Nullable SubscriberV1.RegisterOperatorSubscriber data, @Nullable Integer errorCode, @Nullable String errorDescription) {
        this.status = status;
        this.data = data;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public static LoginRegisterResponse loading() {
        return new LoginRegisterResponse(Status.LOADING, null, null, null);
    }

    public static LoginRegisterResponse success(@NonNull SubscriberV1.RegisterOperatorSubscriber data) {
        return new LoginRegisterResponse(Status.SUCCESS, data, null, null);
    }

    public static LoginRegisterResponse error(@NonNull int errorCode, @NonNull String errorDescription) {
        return new LoginRegisterResponse(Status.ERROR, null, errorCode, errorDescription);
    }
}
