package com.khayah.app.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.khayah.app.BuildConfig;
import com.khayah.app.api.KhayahWebservice;
import com.khayah.app.db.KhayahDb;
import com.khayah.app.db.UserDao;
import com.khayah.app.util.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

;

@Module(includes = ViewModelModule.class)
class AppModule {
    @Singleton @Provides
    KhayahWebservice provideWebService() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        OkHttpClient okHttpClient = builder.build();

        HttpUrl base = HttpUrl.parse(BuildConfig.SERVER_BASE_ENDPOINT);
        HttpUrl url = base.resolve(BuildConfig.API_BASE_PATH);

        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(okHttpClient)
                .build()
                .create(KhayahWebservice.class);
    }

    @Singleton @Provides
    KhayahDb provideDb(Application app) {
        return Room.databaseBuilder(app, KhayahDb.class, "mvvm_template.db").build();
    }

    @Singleton @Provides
    UserDao provideUserDao(KhayahDb db) {
        return db.userDao();
    }

}
