package com.myappiumproject.tests;

import com.myappiumproject.base.Driver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class YourTestClass {

    private io.appium.java_client.AppiumDriver driver;

    @BeforeTest
    public void setUp() {
        driver = Driver.getInstance().start();
    }

    @Test
    public void yourTest() {
        // 在这里编写您的测试代码
        // 在这里编写您的测试代码
    }
    @AfterTest
    public void tearDown() {
        Driver.getInstance().quit();
    }
}
