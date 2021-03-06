package com.liwy.happy.http;



import com.liwy.happy.entity.Joke;
import com.liwy.happy.entity.RootResult;
import com.liwy.happy.entity.WeiboResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liwy on 2017/3/14.
 */

public interface HttpApi {

//    @GET("top250")
//    Observable<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);
//
//    @GET("v2/music/search")
//    Observable<MusicRoot> searchMusicByTag(@Query("tag") String tag);
//
//    @GET("v2/music/{id}")
//    Observable<Musics> getMusicDetail(@Path("id") String id);
//
//    @GET("v1/restserver/ting")
//    Observable<OnlineMusicList> searchMusicByConditions(@Query("type") String type, @Query("size") int size, @Query("offset") int offset, @Query("method") String method);

    @GET("341-1/")
    Observable<RootResult<Joke>> getJokes(@Query("time") String time, @Query("page") String page,
                                          @Query("maxResult") String maxResult,
                                          @Query("showapi_appid") String showapi_appid,
                                          @Query("showapi_sign") String showapi_sign);

    @GET("341-2/")
    Observable<RootResult<Joke>> getImgJokes(@Query("page") String page,
                                             @Query("maxResult") String maxResult,
                                             @Query("showapi_appid") String showapi_appid,
                                             @Query("showapi_sign") String showapi_sign);

    @GET("341-3/")
    Observable<RootResult<Joke>> getGifJokes(@Query("page") String page,
                                             @Query("maxResult") String maxResult,
                                             @Query("showapi_appid") String showapi_appid,
                                             @Query("showapi_sign") String showapi_sign);

    @GET("254-1/")
    Observable<WeiboResult> getWeibos(@Query("typeId") String typeId,
                                      @Query("page") String page,
                                      @Query("space") String space,
                                      @Query("showapi_appid") String showapi_appid,
                                      @Query("showapi_sign") String showapi_sign);


}
