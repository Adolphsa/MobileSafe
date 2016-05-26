package com.zividig.mobilesafe;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.zividig.mobilesafe.activity.view.callsafe.BlackNumberDao;
import com.zividig.mobilesafe.activity.view.callsafe.BlackNumberInfo;

import java.util.List;
import java.util.Random;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testadd() {
        BlackNumberDao blackNumberDao = new BlackNumberDao(getContext());
        Random random = new Random();
        for (int i = 0; i < 200; i++) {
            Long number = 13480995620l + i;
            blackNumberDao.add(number + "",String.valueOf(random.nextInt(3) + 1));
        }

    }
//
//    public void testdelete() {
//        BlackNumberDao blackNumberDao = new BlackNumberDao(getContext());
//        blackNumberDao.delete("13480995620");
//    }
//
//    public void testupdate() {
//        BlackNumberDao blackNumberDao = new BlackNumberDao(getContext());
//        blackNumberDao.update("13480995620", "2");
//
//    }
//
//    public void testfind() {
//        BlackNumberDao blackNumberDao = new BlackNumberDao(getContext());
//        String find = blackNumberDao.find("13480995620");
//        System.out.println(find);
//    }
//
//    public void testfindAll(){
//        BlackNumberDao blackNumberDao = new BlackNumberDao(getContext());
//        List<BlackNumberInfo> all = blackNumberDao.findAll();
//        System.out.println(all);
//    }

//    public void testdeleteAll(){
//        BlackNumberDao blackNumberDao = new BlackNumberDao(getContext());
//        blackNumberDao.deleteAll(getContext());
//    }
}