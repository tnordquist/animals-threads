package edu.cnm.deepdive.animalsthreads.service;

import edu.cnm.deepdive.animalsthreads.model.Animal;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AnimalService {

  @FormUrlEncoded
  @POST("getAnimals")
  Call<List<Animal>> getAnimals(@Field("key") String key);

}
