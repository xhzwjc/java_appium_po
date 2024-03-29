package com.myappiumproject.tests;

import com.myappiumproject.base.Driver;
import com.myappiumproject.pages.LoginPage;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.appium.java_client.android.AndroidDriver;

public class LoginTest {
    private AndroidDriver driver;
    private LoginPage loginPage;

    @BeforeClass
    public void setUp() {
        // 初始化 AppiumDriver，返回 AndroidDriver
        driver = Driver.getInstance().start();
        loginPage = new LoginPage(driver); // 初始化 LoginPage 对象
    }

    @Test
    public void testLogin() throws InterruptedException {

        // 执行登录操作
        loginPage.clickOnSomeElement();

        // 在这里可以添加断言来验证登录后的页面元素是否正确显示，或者验证登录功能是否正常

//        // 示例断言：验证某个元素是否显示
//        Assert.assertTrue(loginPage.isSomeElementDisplayed(), "登录后某个元素未正确显示");
    }

    @AfterClass
    public void tearDown() {
        Driver.getInstance().quit(); // 退出驱动
    }
}
