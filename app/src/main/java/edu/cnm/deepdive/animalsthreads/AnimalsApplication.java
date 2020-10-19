package edu.cnm.deepdive.animalsthreads;

import android.app.Application;
import com.squareup.picasso.Picasso;

public class AnimalsApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Picasso.setSingletonInstance(
        new Picasso.Builder(this)
        .loggingEnabled(true)
        .build()
    );
  }
}
