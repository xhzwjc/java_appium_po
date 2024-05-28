package com.myappiumproject.base;

//import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 此类提供了初始化和退出Appium驱动程序的方法。
 */
public class AppiumDriver_jiu {
    private static AndroidDriver driver; // 将类型更改为AndroidDriver

    public static AndroidDriver initializeDriver() {
        if (driver == null) {
            // 为Android设备设置所需的能力
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("deviceName", "RFCT420QSDF");
            caps.setCapability("platformName", "Android");
            caps.setCapability("appPackage", "com.house.user");
            caps.setCapability("appActivity", "com.house.user.MainActivity");
            caps.setCapability("automationName", "UiAutomator2");

            // 定义Appium服务器的URL
            URL url = null;
            try {
                url = new URL("http://127.0.0.1:4723/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            System.out.println("33333333333333333");
            // 初始化Android驱动程序并返回实例
            driver = new AndroidDriver(url, caps);

        }
        System.out.println("111111111111111111");
        return driver;
    }

    /**
     * 退出Appium驱动程序。
     */
    public static void quitDriver() {
        // 如果驱动程序实例不为空，则退出驱动程序并将实例置为空
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}

