package edu.cnm.deepdive.animalsthreads.controller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cnm.deepdive.animalsthreads.BuildConfig;
import edu.cnm.deepdive.animalsthreads.R;
import edu.cnm.deepdive.animalsthreads.model.Animal;
import edu.cnm.deepdive.animalsthreads.model.ApiKey;
import edu.cnm.deepdive.animalsthreads.service.AnimalService;
import java.io.IOException;
import java.util.List;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageFragment extends Fragment {

  private WebView contentView;

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_image, container, false);
    setupWebView(root);
    return root;
  }

  private void setupWebView(View root) {
    contentView = root.findViewById(R.id.content_view);
    contentView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return false;
      }
    });
    WebSettings settings = contentView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setSupportZoom(true);
    settings.setBuiltInZoomControls(true);
    settings.setDisplayZoomControls(false);
    settings.setUseWideViewPort(true);
    settings.setLoadWithOverviewMode(true);
    new RetrieveImageTask().execute();
  }

  private class RetrieveImageTask extends AsyncTask<Void, Void, List<Animal>> {

    AnimalService animalService;

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      Gson gson = new GsonBuilder()
          .excludeFieldsWithoutExposeAnnotation()
          .create();
      Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(BuildConfig.BASE_URL)
          .addConverterFactory(GsonConverterFactory.create(gson))
          .build();

      animalService = retrofit.create(AnimalService.class);
    }

    protected List<Animal> doInBackground(Void... voids) {
      try {
        Response<ApiKey> response1 = animalService.getApiKey().execute();
        ApiKey key = response1.body();
        assert key != null;
        final String clientKey = key.getKey();
        Response<List<Animal>> response = animalService
            .getAnimals(clientKey)
            .execute();
        if (response.isSuccessful()) {
          List<Animal> animals = response.body();
          return animals;

        } else {
          Log.e("AnimalService", response.message());
          cancel(true);
        }

      } catch (IOException e) {
        Log.e("AnimalService", e.getMessage(), e);
        cancel(true);
      }
      return null;
    }

    @Override
    protected void onPostExecute(List<Animal> animals) {
      final String url = animals.get(37).getImageUrl();
      getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
          contentView.loadUrl(url);
        }
      });
    }
  }
}