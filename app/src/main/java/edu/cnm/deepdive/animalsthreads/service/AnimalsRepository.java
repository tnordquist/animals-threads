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
  private String key = "";


  public AnimalsRepository(Context context,
      AnimalsService animalsService) {
    this.context = context;
    this.key = key;
    this.animalsService = animalsService;
  }

//public Completable loadKey() {
//  animalsService.getApiKey()
//      return Com
//}

  public Single loadAnimals() {

    return Completable.fromSingle(animalsService.getApiKey()
        .flatMapCompletable((key) -> (CompletableSource) animalsService.getAnimals(key.getKey()))
        .subscribeOn(Schedulers.io());
  }
}
