package edu.cnm.deepdive.animalsthreads.service;

import android.content.Context;
import edu.cnm.deepdive.animalsthreads.model.Animal;
import edu.cnm.deepdive.animalsthreads.model.ApiKey;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class AnimalsRepository {

  private final Context context;
  private final AnimalsService animalsService;

  public AnimalsRepository(Context context) {
    this.context = context;
    this.animalsService = AnimalsService.getInstance();
  }

  public Single<List<Animal>> loadAnimals() {

    return animalsService.getApiKey()
        .flatMap((key) -> animalsService.getAnimals(key.getKey()))
        .subscribeOn(Schedulers.io());
  }
}
