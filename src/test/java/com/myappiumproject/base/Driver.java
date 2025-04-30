package com.myappiumproject.base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class Driver {
    private static AppiumDriver driver; // ✅ 统一 AppiumDriver，不再分安卓和iOS
    private static Driver instance;

    // 这里控制平台，建议后续用 config 文件或 system property 替代
    private static final String PLATFORM = "iOS"; // 👉 切换 Android 或 iOS

    private Driver() {}

    public static Driver getInstance() {
        if (instance == null) {
            instance = new Driver();
        }
        return instance;
    }

    public AppiumDriver start() {
        if (driver == null) {
            DesiredCapabilities caps = new DesiredCapabilities();
            URL url = null;

            try {
                url = new URL("http://127.0.0.1:4723/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if ("Android".equalsIgnoreCase(PLATFORM)) {
                System.out.println("这是android");
                caps.setCapability("platformName", "Android");
                caps.setCapability("deviceName", "RFCT420QSDF");
                caps.setCapability("appPackage", "com.house.user");
                caps.setCapability("appActivity", "com.house.user.MainActivity");
                caps.setCapability("automationName", "UiAutomator2");

                driver = new AndroidDriver(url, caps);
            } else if ("iOS".equalsIgnoreCase(PLATFORM)) {
                System.out.println("这是iOS");
                caps.setCapability("platformName", "iOS");
                caps.setCapability("deviceName", "iPhone12");
                caps.setCapability("platformVersion", "17.5.1");
                caps.setCapability("automationName", "XCUITest");
                caps.setCapability("bundleId", "com.2house.service.house");
                caps.setCapability("udid", "00008101-001879001AC0001E");
                caps.setCapability("noReset", false);
                caps.setCapability("useNewWDA", false);
                caps.setCapability("usePrebuiltWDA", true);
                caps.setCapability("shouldStartWDA", false);
                caps.setCapability("showXcodeLog", true);
                caps.setCapability("showIOSLog", true);
                caps.setCapability("xcodeOrgId", "V5C9YMA3NY");
                caps.setCapability("xcodeSigningId", "iPhone Developer");
                caps.setCapability("wdaLocalPort", 8100);

                driver = new IOSDriver(url, caps);
            } else {
                throw new RuntimeException("平台不支持: " + PLATFORM);
            }
        }

        return driver;
    }

    public void quit() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}