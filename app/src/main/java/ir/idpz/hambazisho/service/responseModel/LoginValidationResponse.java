package ir.idpz.hambazisho.service.responseModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vasl.vaslapp.modules.subscriber.global.proto.holder.SubscriberV1;

public class LoginValidationResponse {
    public final Status status;

    @Nullable
    public final SubscriberV1.ValidateOperatorSubscriber data;

    @Nullable
    public final Integer errorCode;

    @Nullable
    public final String errorDescription;



    private LoginValidationResponse(Status status, @Nullable SubscriberV1.ValidateOperatorSubscriber data, @Nullable Integer errorCode, @Nullable String errorDescription) {
        this.status = status;
        this.data = data;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public static LoginValidationResponse loading() {
        return new LoginValidationResponse(Status.LOADING, null, null, null);
    }

    public static LoginValidationResponse success(@NonNull SubscriberV1.ValidateOperatorSubscriber data) {
        return new LoginValidationResponse(Status.SUCCESS, data, null, null);
    }

    public static LoginValidationResponse error(@NonNull int errorCode, @NonNull String errorDescription) {
        return new LoginValidationResponse(Status.ERROR, null, errorCode, errorDescription);
    }
}
