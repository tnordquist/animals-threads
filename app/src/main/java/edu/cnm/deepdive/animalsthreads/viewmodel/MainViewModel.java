package edu.cnm.deepdive.animalsthreads.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import edu.cnm.deepdive.animalsthreads.model.Animal;
import edu.cnm.deepdive.animalsthreads.service.AnimalsRepository;
import edu.cnm.deepdive.animalsthreads.service.AnimalsService;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class MainViewModel extends AndroidViewModel implements LifecycleObserver {

  private final MutableLiveData<List<Animal>> animals;
  private final MutableLiveData<Throwable> throwable;
  private final MutableLiveData<Integer> selectedItem;
  private final AnimalsRepository animalsRepository;
  private final CompositeDisposable pending;
  private final AnimalsService animalsService;

  public MainViewModel(@NonNull Application application) {
    super(application);
    animalsService = AnimalsService.getInstance();
    animals = new MutableLiveData<>();
    selectedItem = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    animalsRepository = new AnimalsRepository(application, animalsService);
    pending = new CompositeDisposable();
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
    pending.add(
        animalsRepository.loadAnimals()
            .subscribe(
                animals::postValue,
                this.throwable::postValue)
    );

  }


  @OnLifecycleEvent(Event.ON_STOP)
  private void clearPending() {
    pending.clear();
  }

}


