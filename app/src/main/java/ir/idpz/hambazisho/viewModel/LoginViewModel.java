package ir.idpz.hambazisho.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ir.idpz.hambazisho.service.repository.ProjectRepository;
import ir.idpz.hambazisho.service.responseModel.LoginRegisterResponse;
import ir.idpz.hambazisho.service.responseModel.LoginValidationResponse;

public class LoginViewModel extends AndroidViewModel {

    private final ProjectRepository repository;

    public LoginViewModel(@NonNull Application application) {
        super(application);

        repository = ProjectRepository.getInstance();
    }

    public LiveData<LoginRegisterResponse> register(String mobileNumber) {
        return repository.register(mobileNumber);
    }

    public LiveData<LoginValidationResponse> validation(String activationKey, String mobileNumber) {
        return repository.validation(activationKey, mobileNumber);
    }
}
