package com.zividig.mobilesafe.activity.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.zividig.mobilesafe.R;
import com.zividig.mobilesafe.activity.view.mobilesafe.MobileSafe;
import com.zividig.mobilesafe.activity.service.LocationService;

/**
 * 接收短信的广播
 * Created by Administrator on 2016-05-17.
 */
public class SmsReceiver extends BroadcastReceiver {

    private SharedPreferences sp;

    @Override
    public void onReceive(Context context, Intent intent) {

        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        String format = intent.getStringExtra("format");

        for (Object object : objects) {
            SmsMessage message = SmsMessage.createFromPdu((byte[]) object);

            String originatingAddress = message.getOriginatingAddress(); //获取发送短信的号码
            String messageBody = message.getMessageBody();  //获取短息的内容

            System.out.println(originatingAddress + ":" + messageBody);

            if ("#*alarm*#".equals(messageBody)){
                //播放报警音乐
                System.out.println("相等");
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.huanqin);
                mediaPlayer.setVolume(1f,1f); //设置音量
                mediaPlayer.setLooping(true); //单曲循环
                mediaPlayer.start();
                abortBroadcast(); //中断短信的传递，这样系统短信App就收不到了
            }else if ("#*location*#".equals(messageBody)){
                //回传定位数据
                System.out.println("开启定位服务");
                context.startService(new Intent(context, LocationService.class));


                String location = sp.getString("location", "get location^^");
                System.out.println(location);

                abortBroadcast();
            }else if ("#*wipedata*#".equals(messageBody)){
                System.out.println("远程删除数据");

                boolean admin = sp.getBoolean("admin", false);
                if (admin){
                    MobileSafe.mDPM.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE); //删除数据
                }else{
                    Toast.makeText(context,"请先激活设备",Toast.LENGTH_SHORT).show();
                }

                abortBroadcast();
            }else if("#*lockscreen*#".equals(messageBody)){
                System.out.println("远程锁屏");

                boolean admin = sp.getBoolean("admin", false);
                if (admin){
                    MobileSafe.mDPM.lockNow(); //锁屏
                }else{
                    Toast.makeText(context,"请先激活设备",Toast.LENGTH_SHORT).show();
                }

                abortBroadcast();
            }
        }
    }
}
