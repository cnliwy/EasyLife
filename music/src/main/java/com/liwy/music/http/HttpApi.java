package com.liwy.music.http;


import com.liwy.music.model.OnlineMusicList;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liwy on 2017/3/14.
 */

public interface HttpApi {

//    @GET("v2/music/search")
//    Observable<MusicRoot> searchMusicByTag(@Query("tag") String tag);
//
//    @GET("v2/music/{id}")
//    Observable<Musics> getMusicDetail(@Path("id") String id);

    @GET("v1/restserver/ting")
    Observable<OnlineMusicList> searchMusicByConditions(@Query("type") String type, @Query("size") int size, @Query("offset") int offset, @Query("method") String method);


}
