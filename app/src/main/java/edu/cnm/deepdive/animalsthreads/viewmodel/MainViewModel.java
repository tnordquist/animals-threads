package edu.cnm.deepdive.animalsthreads.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.animalsthreads.BuildConfig;
import edu.cnm.deepdive.animalsthreads.model.Animal;
import edu.cnm.deepdive.animalsthreads.model.ApiKey;
import edu.cnm.deepdive.animalsthreads.service.AnimalService;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.util.List;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainViewModel extends AndroidViewModel {

  private final MutableLiveData<List<Animal>> animals;
  private MutableLiveData<ApiKey> apiKey;
  private final MutableLiveData<Throwable> throwable;
  private AnimalService animalService;
  private String clientKey = "";

  public MainViewModel(@NonNull Application application) {
    super(application);
    animalService = AnimalService.getInstance();
    apiKey = new MutableLiveData<>();
    animals = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    loadAnimals();
  }

  public LiveData<List<Animal>> getAnimals() {
    return animals;
  }

  public LiveData<ApiKey> getApiKey() {
    return apiKey;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  @SuppressLint("CheckResult")
  private void loadAnimals() {

    animalService.getApiKey()
        .subscribeOn(Schedulers.io())
        .subscribe(value -> this.apiKey.postValue(value),
            this.throwable::postValue);
    apiKey.observeForever(new Observer<ApiKey>() {
      @Override
      public void onChanged(ApiKey key) {
        clientKey = key.getKey();
        apiKey.removeObserver(this::onChanged);
      }
    });
    animalService.getAnimals(clientKey)
        .subscribeOn(Schedulers.io())
        .subscribe(this.animals::postValue,
            this.throwable::postValue);

  }


}
