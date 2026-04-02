package com.example.picana_apk.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthViewModel extends ViewModel {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private MutableLiveData<FirebaseUser> userMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorMutableLiveData = new MutableLiveData<>();

    public LiveData<FirebaseUser> getUserLiveData() {
        return userMutableLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorMutableLiveData;
    }

    public void autenticarConGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userMutableLiveData.postValue(mAuth.getCurrentUser());
                    } else {
                        errorMutableLiveData.postValue("Fallo en la autenticación con Firebase");
                    }
                });
    }

    public void autenticarConFacebook(String token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userMutableLiveData.postValue(mAuth.getCurrentUser());
                    } else {
                        errorMutableLiveData.postValue("Fallo en la autenticación con Facebook");
                    }
                });
    }
}