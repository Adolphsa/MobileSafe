package com.zividig.mobilesafe.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bm.library.PhotoView;
import com.zividig.mobilesafe.R;

/**
 * 手机防盗
 * Created by Administrator on 2016-04-19.
 */
public class MobileSafe extends Activity {

    private static String mImgUrl = "http://192.168.1.102:8080/test_picture.jpg";
    private PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_safe);

        photoView = (PhotoView) findViewById(R.id.img);
        // 启用图片缩放功能
        photoView.enable();

        getImage();
    }

    public void getImage() {
        RequestQueue queue = Volley.newRequestQueue(this);
        ImageRequest imageRequest = new ImageRequest(mImgUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        //下载成功后回调此方法
                        photoView.setImageBitmap(response);

                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {

                   @Override
                   public void onErrorResponse(VolleyError error) {
                    //下载失败回调此方法
                       Toast.makeText(MobileSafe.this,"图片下载失败",Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(imageRequest);
    }
}
