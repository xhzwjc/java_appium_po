package com.myappiumproject.tests;

import com.myappiumproject.base.Driver;
import com.myappiumproject.pages.LoginPage;
import io.appium.java_client.AppiumDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.appium.java_client.android.AndroidDriver;

public class LoginTest {
    private AppiumDriver driver;         // ✅ 使用 AppiumDriver 统一类型
    private LoginPage loginPage;


    @BeforeClass
    public void setUp() {
        driver = Driver.getInstance().start();     // ✅ 不区分平台，复用 start 方法
        loginPage = new LoginPage(driver);         // ✅ 页面对象传 AppiumDriver
    }

    @Test
    public void testLogin() throws InterruptedException {
        // 执行登录操作
        loginPage.clickOnSomeElement();

        // 示例断言（可补充）
//        Assert.assertTrue(loginPage.isSomeElementDisplayed(), "登录后某个元素未正确显示");
    }

    @AfterClass
    public void tearDown() {
        Driver.getInstance().quit();
    }
}