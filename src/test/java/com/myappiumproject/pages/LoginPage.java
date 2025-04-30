package com.myappiumproject.pages;

import com.myappiumproject.base.Base;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LoginPage extends Base {
    private AppiumDriver driver;

    private By elAgreeBtn;
    private By elExperienceBtn;
    private By elMineTab;
    private By elPhoneInput;
    private By elPasswordInput;
    private By elCheckBox;
    private By elLoginBtn;
    private By elLoginBtn1;


    public LoginPage(AppiumDriver driver) {
        super(driver);
        this.driver = driver;

        String platform = driver.getCapabilities().getCapability("platformName").toString().toLowerCase();

        if (platform.equalsIgnoreCase("Android")) {
            System.out.println("走的android元素定位");
            elAgreeBtn = AppiumBy.accessibilityId("同意");
            elExperienceBtn = AppiumBy.accessibilityId("立即体验");
            elMineTab = AppiumBy.accessibilityId("assets/images/tabbar/mine_normal.svg");
//            elPhoneInput = AppiumBy.xpath("//*[@index='0' and @class='android.widget.EditText']");
            elPasswordInput = AppiumBy.xpath("//*[@index='1' and @class='android.widget.EditText']");
            elCheckBox = AppiumBy.className("android.widget.CheckBox");
            elLoginBtn = AppiumBy.accessibilityId("登录");
        } else {
            System.out.println("走的iOS元素定位");
            elAgreeBtn = AppiumBy.iOSNsPredicateString("name == '同意'");
            elExperienceBtn = AppiumBy.iOSNsPredicateString("name == '立即体验'");
            elMineTab = AppiumBy.iOSNsPredicateString("name CONTAINS '我的'");

//            elPhoneInput = AppiumBy.xpath("//XCUIElementTypeTextField[@name=\"请输入11位手机号\"]");
//            elPhoneInput = AppiumBy.iOSNsPredicateString("name == \"请输入11位手机号\"\n");
            elPhoneInput = AppiumBy.accessibilityId("请输入11位手机号");
            elPasswordInput = AppiumBy.accessibilityId("请输入验证码");

//            elPasswordInput = AppiumBy.iOSNsPredicateString("name == \"请输入验证码\"\n");
//            elCheckBox = AppiumBy.iOSNsPredicateString("tXCUIElementTypeSwitch\n");
//            elCheckBox = AppiumBy.iOSClassChain("**/XCUIElementTypeSwitch[`value == \"0\"`]\n");
            elCheckBox = AppiumBy.className("tXCUIElementTypeSwitch\n");
//            elLoginBtn = AppiumBy.iOSNsPredicateString("name == \"登录\"");
            elLoginBtn = AppiumBy.accessibilityId("登录");
//            elLoginBtn1 = AppiumBy.accessibilityId("包年服务\n");
            elLoginBtn1 = AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`name == \"包年服务\"`]\n");
//            elLoginBtn = AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`name == \"登录\"`]\n");
//            elLoginBtn = AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeButton' AND name == '登录'");
        }
    }

    // 页面操作
    public void clickOnSomeElement() throws InterruptedException {

        clickWithWait(elLoginBtn1);
    }
}

//    // 页面操作
//    public void clickOnSomeElement() throws InterruptedException {
////        clickWithWait(elAgreeBtn);
////        Thread.sleep(500);
////
////        for (int i = 0; i < 3; i++) {
////            swipe();
////            Thread.sleep(300);
////        }
////
////        clickWithWait(elExperienceBtn);
//        Thread.sleep(3000);
//        System.out.println("等待3秒后");
////        tap(946, 2207);
//        tap(363, 768);
//        System.out.println("一点击");
//        Thread.sleep(1000);
//
////        // ✅ 打印当前页面结构，确认元素是否存在
////        System.out.println("当前页面结构:\n" + driver.getPageSource());
////
////        // ✅ 尝试直接查找元素（不等待）
////        try {
////            WebElement phoneInput = driver.findElement(elPhoneInput);
////            System.out.println("✅ 元素存在: " + phoneInput);
////        } catch (Exception e) {
////            System.out.println("❌ 元素不存在: " + e.getMessage());
////        }
//
//        tap(33 + 162, 187 + 23); // (x + width/2, y + height/2) 点击输入框中心
//        Thread.sleep(5000);
//
//
//
////        clickWithJS(elPhoneInput);
////        clickWithWait(elPhoneInput);
//        System.out.println("点击成功111111");
//        type(elPhoneInput, "19825852335");
//        System.out.println("输入账号成功");
//
//        int codeX = 33 + 110; // x=33 + width/2
//        int codeY = 250 + 23; // y=250 + height/2
//        tap(codeX, codeY);
//
//        type(elPasswordInput, "6543");
//        System.out.println("输入密码成功");
//
//        int switchX = 17 + (26 / 2);  // x + width/2
//        int switchY = 391 + (27 / 2); // y + height/2
//        tap(switchX, switchY);
//
////        clickWithWait(elCheckBox);
//        clickWithWait(elLoginBtn);
//    }
//}