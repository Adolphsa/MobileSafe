package com.zividig.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zividig.mobilesafe.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 闪屏页面
 * Created by Administrator on 2016-04-15.
 */
public class SplashActivity extends Activity {

    private final static int CODE_ENTER_MAIN_ACTIVITY = 0;

    //服务器返回的信息
    private String mVersionName;
    private int mVersionCode;
    private String mDesrc;
    private String mDownloadUrl;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case CODE_ENTER_MAIN_ACTIVITY:
                    enterMainActivity();
                    break;
            }
        }
    };
    private TextView tvProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView versionName = (TextView) findViewById(R.id.tv_version_code);
        tvProgress = (TextView) findViewById(R.id.tv_progress);
        versionName.setText("版本号" + getVersionName());

        checkVersion();

//        //延时2S进入到主界面
//        mHandler.sendEmptyMessageDelayed(CODE_ENTER_MAIN_ACTIVITY, 2000);
    }

    /**
     * 检查版本更新
     */
    private void checkVersion() {
        final long startTime = System.currentTimeMillis();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObject = new JsonObjectRequest("http://192.168.1.101:8080/mobile_safe_update.json", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        try {
                            mVersionName = response.getString("versionName"); //版本名称
                            mVersionCode = response.getInt("versionCode"); //版本号
                            mDesrc = response.getString("description"); //版本描述
                            mDownloadUrl = response.getString("downloadUrl"); //版本升级地址

                            //比较本地软件版本号和服务器上的版本号
                            if (mVersionCode > getVerdionCode()){
                                showDialog(); //显示升级对话框
                            }else{
                                enterMainActivity();
                            }

                            System.out.println("版本名称:" + mVersionName + "\n版本号:" + mVersionCode + "\n版本描述:" + mDesrc + "\n版本升级地址:" + mDownloadUrl);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("JSON解析错误");
                            enterMainActivity();
                        }finally {
                            long endTime = System.currentTimeMillis();
                            long timeUsed = endTime - startTime;
                            System.out.println("花费的时间" + timeUsed);
                            if (timeUsed < 2000){
                                try {
                                    Thread.sleep(2000 - timeUsed);
                                    System.out.println("当前线程" + Thread.currentThread());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("数据错误" + error.toString());
                        Toast.makeText(SplashActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                        enterMainActivity();
                    }
                });
        queue.add(jsonObject);
    }

    /**
     * 显示对话框
     */
    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("版本更新");
        builder.setMessage(mDesrc);

        //确定按钮
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("立即更新");
                download();
            }
        });

        //取消按钮
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("以后再说");
                enterMainActivity();
            }
        });

        //按返回键时回调此方法
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterMainActivity();
            }
        });
        builder.show();
    }

    /**
     * 下载更新文件
     */
    private void download(){

        //首先是否有内存卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            tvProgress.setVisibility(View.VISIBLE);
            String target = Environment.getExternalStorageDirectory() + "/update.apk";

            HttpUtils downUtils = new HttpUtils();
            downUtils.download(mDownloadUrl, target, new RequestCallBack<File>() {

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);

                    System.out.println("下载进度:" + current + "/" + total);
                    tvProgress.setText("下载进度:" + current * 100 / total + "%");

                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载成功后就自动跳到程序安装界面
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(responseInfo.result),
                            "application/vnd.android.package-archive");
                    // startActivity(intent);
                    startActivityForResult(intent, 0);// 如果用户取消安装的话,
                    // 会返回结果,回调方法onActivityResult
                    android.os.Process.killProcess(android.os.Process.myPid());
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(SplashActivity.this, "下载失败!",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(SplashActivity.this,"没有找到SD卡",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 取消安装的时候回调此方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterMainActivity();
    }

    /**
     * 获取版本号
     *
     * @return versionCode
     */
    private int getVerdionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            System.out.println("版本号为" + versionCode);
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取版本名称
     *
     * @return versionName
     */
    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            System.out.println("版本号为" + versionCode);
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 进入主页
     */
    private void enterMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
