package com.zividig.mobilesafe;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.zividig.mobilesafe.activity.view.callsafe.BlackNumberDao;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testadd() {
        BlackNumberDao blackNumberDao = new BlackNumberDao(getContext());
        blackNumberDao.add("119", "1");
    }

    public void testdelete() {
        BlackNumberDao blackNumberDao = new BlackNumberDao(getContext());
        blackNumberDao.delete("119");
    }

    public void testupdate() {
        BlackNumberDao blackNumberDao = new BlackNumberDao(getContext());
        blackNumberDao.update("119", "2");

    }

    public void testfind() {
        BlackNumberDao blackNumberDao = new BlackNumberDao(getContext());
        String find = blackNumberDao.find("119");
        System.out.println(find);
    }




}