package com.example.da08.musicplayerproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.da08.musicplayerproject.domain.Const;
import com.example.da08.musicplayerproject.domain.SearchData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by iyeongjun on 2017. 7. 14..
 */

public class YoutubeDialog extends Dialog {
    public static String nextPageInfo = null;
    @BindView(R.id.recylerview)
    RecyclerView recyclerView;
    public static String searchQuery;
    Retrofit retrofit;
    JSONObject jsonObject;
    List<SearchData> searchDatas = new ArrayList<>();
    Context context;
    public YoutubeDialog(@NonNull Context context, String name, String artist) {
        super(context);

        this.context = context;
        searchQuery = name + "-" + artist;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dialog);
        ButterKnife.bind(this);
        getDataForYoutube();

    }

    private void success(){
        for(int i = 0 ; i < searchDatas.size() ; i++){
            Log.i("myLog", searchDatas.get(i).getTitle()+"");
        }
        Toast.makeText(context, searchDatas.size()+"", Toast.LENGTH_SHORT).show();
        recyclerView.setAdapter(new ThumbNailAdapter(searchDatas,context));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public void getDataForYoutube(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com")
                .build();
        InterfaceForSerachData interfaceForSerachData
                = retrofit.create(InterfaceForSerachData.class);
        Call<ResponseBody> result
                = interfaceForSerachData.getYoutubeData(searchQuery,
                                                        Const.ParseQuery.SNIPPET,
                                                        Const.Auth.API_KEY,
                                                        Const.ParseQuery.MAX_RESULT);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    ResponseBody responseBody = response.body();
                    try {
                        searchDatas = setSearchData(responseBody.string());
                        Log.i("Result", "결과값="+ responseBody);
                        Log.i("Result", "결과 스트링" + responseBody.string());
                        success();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        Log.e("Result", "에러code="+response.errorBody().string());
                        Toast.makeText(context, "에러남", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("Result", "에러="+response.message());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    interface InterfaceForSerachData{
        @GET("/youtube/v3/search")
        Call<ResponseBody> getYoutubeData(@Query("q")String searchQuery,
                                          @Query("part")String snippet,
                                          @Query("key")String key,
                                          @Query("maxResult")int maxResult);
    }
    private List<SearchData> setSearchData(String str){
        List<SearchData> datas = new ArrayList<>();
        JSONArray jsonArray=null;
        try {
            JSONObject object = new JSONObject(str);
            nextPageInfo = object.getString("nextPageToken");
            Log.i("myLog",nextPageInfo+"");
            jsonArray = object.getJSONArray("items");
            if (jsonArray == null){

            }else{
                for(int i = 0; i < jsonArray.length() ; i++){
                    JSONObject item = jsonArray.getJSONObject(i);
                    SearchData data = new SearchData();
                    data.setId(item.getJSONObject("id").getString("videoId"));
                    data.setTitle(item.getJSONObject("snippet").getString("title"));
                    data.setUrl(item.getJSONObject("snippet").getJSONObject("thumbnails")
                            .getJSONObject("default").getString("url"));
                    data.setDescription(item.getJSONObject("snippet").getString("description"));

                    Log.i("myLog","videoId==="+data.getId() +" url === " +data.getUrl() +
                            " title====" + data.getTitle() +" description===" + data.getDescription());
                    datas.add(data);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datas;
    }
}
