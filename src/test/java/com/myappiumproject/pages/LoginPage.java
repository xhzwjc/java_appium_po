package com.myappiumproject.pages;

import com.myappiumproject.base.Base;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import io.appium.java_client.AppiumBy;


public class LoginPage extends Base {

//    private WebElement el1 = driver.findElement(By.xpath("//android.view.View[@content-desc='同意']"));

    private By el1 = AppiumBy.accessibilityId("同意");
    //    private WebElement el2  = driver.findElement(AppiumBy.xpath("//android.view.View[@content-desc='立即体验']"));
    private By el2 = AppiumBy.accessibilityId("立即体验");
    private By el3 = AppiumBy.accessibilityId("assets/images/tabbar/mine_normal.svg");
    private By el4 = AppiumBy.xpath("//*[@index='0' and @class='android.widget.EditText']");
    private By el5 = AppiumBy.xpath("//*[@index='1' and @class='android.widget.EditText']");
    private By el6 = AppiumBy.className("android.widget.CheckBox");
    private By el7 = AppiumBy.accessibilityId("登录");


    // 构造函数
    public LoginPage(AndroidDriver driver) {
        super(driver);
    }

    // 页面操作
    public void clickOnSomeElement() throws InterruptedException {
//        clickWithWait(el1);
//
//        Thread.sleep(1000);
//
//        for (int i = 0; i < 3; i++) {
//            swipe();
//            Thread.sleep(500);
//        }
//
//        clickWithWait(el2);
//
//        Thread.sleep(1000);
//
//        tap(el3);
//
//        type(el4,"19825852335");
//
//        type(el5,"6543");
//
//        clickWithWait(el6);
//
//        clickWithWait(el7);
//
        Thread.sleep(3000);

        tap(763, 1965);
        Thread.sleep(500);

        for (int i = 0; i < 3; i++) {
            swipe();
//            swipes(912, 1165,160, 1165);
            Thread.sleep(300);
        }
        Thread.sleep(500);
        tap(919, 206);

        Thread.sleep(500);
        tap(946, 2207);

        Thread.sleep(3000);

    }
}