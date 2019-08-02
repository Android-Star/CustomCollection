package com.example.yzs.customcollection.network.retrofit;

import com.example.yzs.customcollection.network.volley.IpModel;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {
  @GET("getIpInfo.php?ip=59.108.54.37")
  Call<IpModel> getIpMsg();

  @GET("getIpInfo.php")
  Call<IpModel> getIpMsgQuery(@Query("ip") String ip);

  @GET("getIpInfo.php")
  Call<IpModel> getIpMsgQueryMap(@QueryMap Map<String, String> options);

  /**
   * post键值对作为参数
   * @param ip
   * @return
   */
  @POST("getIpInfo.php")
  @FormUrlEncoded
  Call<IpModel> getIpMsgField(@Field("ip") String ip);

  /**
   * json串作为参数
   * @param ip
   * @return
   */
  @POST("getIpInfo.php")
  Call<IpModel> getIpMsgBody(@Body Ip ip);


}
