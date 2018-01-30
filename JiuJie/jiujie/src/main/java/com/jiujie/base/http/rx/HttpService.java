package com.jiujie.base.http.rx;


import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 使用rx的HttpService示例类
 */
public interface HttpService {

    /**
     * 登录
     * Object 改成返回json的jsonBean
     */
    @POST("/api/app-login")
    Observable<Object> requestLogin(@Query("username") String username,
                                      @Query("password")String password,
                                      @Query("client")String client);//固定值：Android

}
