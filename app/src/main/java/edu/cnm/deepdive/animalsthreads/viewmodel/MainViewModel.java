package edu.cnm.deepdive.animalsthreads.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import edu.cnm.deepdive.animalsthreads.model.Animal;
import edu.cnm.deepdive.animalsthreads.service.AnimalService;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

  private final MutableLiveData<List<Animal>> animals;
  private final MutableLiveData<Throwable> throwable;
  private final MutableLiveData<Integer> selectedItem;
  private final AnimalService animalService;

  public MainViewModel(@NonNull Application application) {
    super(application);
    animalService = AnimalService.getInstance();
    animals = new MutableLiveData<>();
    selectedItem = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    loadAnimals();
  }

  public LiveData<List<Animal>> getAnimals() {
    return animals;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public LiveData<Integer> getSelectedItem() {
    return selectedItem;
  }

  public void select(int index) {
    selectedItem.setValue(index);
  }

  @SuppressLint("CheckResult")
  private void loadAnimals() {
    animalService.getApiKey()
        .subscribeOn(Schedulers.io())
        .flatMap((key) -> animalService.getAnimals(key.getKey()))
        .subscribe(
            (value) -> {
              this.animals.postValue(value);
              selectedItem.postValue(0);
            },
            throwable::postValue
        );
  }

}

