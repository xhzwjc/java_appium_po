package com.myappiumproject.base;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.MalformedURLException;
import java.net.URL;

public class Driver {
    // 声明一个静态的 AppiumDriver 对象，用于管理驱动程序
    private static AndroidDriver androidDriver;
    // 声明一个静态的 Driver 对象，用于获取单例实例
    private static Driver driver;

    // 私有构造方法，确保只能在类内部实例化
    private Driver() {}

    // 获取单例实例的方法
    public static Driver getInstance() {
        // 如果 driver 对象为空，则创建一个新的 Driver 实例并赋值给 driver
        if (driver == null) {
            driver = new Driver();
        }
        // 返回 driver 实例
        return driver;
    }

    // 启动 Appium 驱动程序的方法
    public AndroidDriver start() {
        // 如果 appiumDriver 对象为空，则进行初始化
        if (androidDriver == null) {
            // 创建一个 DesiredCapabilities 对象，用于设置 Appium 启动参数
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("deviceName", "RFCT420QSDF"); // 设置设备名称
            caps.setCapability("platformName", "Android"); // 设置平台名称为 Android
            caps.setCapability("appPackage", "com.house.user"); // 设置应用的包名
            caps.setCapability("appActivity", "com.house.user.MainActivity"); // 设置应用的启动 Activity
            caps.setCapability("automationName", "UiAutomator2"); // 设置自动化引擎为 UiAutomator2

            // 定义 Appium 服务器的 URL
            URL url = null;
            try {
                url = new URL("http://127.0.0.1:4723/"); // 设置 Appium 服务器的地址
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            // 初始化 Android 驱动程序并将实例赋值给 appiumDriver
            androidDriver = new AndroidDriver(url, caps);

//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        }
        // 返回 appiumDriver 实例
        return androidDriver;
    }

    // 停止并退出 Appium 驱动程序的方法
    public void quit() {
        // 如果 appiumDriver 对象不为空，则执行退出操作
        if (androidDriver != null) {
            androidDriver.quit(); // 退出驱动程序
            androidDriver = null; // 将 appiumDriver 置为空，释放资源
        }
    }
}
