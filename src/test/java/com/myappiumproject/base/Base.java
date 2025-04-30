package com.myappiumproject.base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.google.common.collect.ImmutableMap;

import java.time.Duration;
import java.util.*;

public class Base {
    protected AppiumDriver driver;
    protected WebDriverWait wait;

    public Base(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
//    protected void clickWithWait(By locator) {
//        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
//        element.click();
//    }

    protected void type(By locator, String text) {
        WebElement element = driver.findElement(locator);
        element.click();
        element.sendKeys(text);
    }


        // 点击元素并添加显示等待
    protected void clickWithWait(By locator) {
        // 增强的等待条件：同时检查可见性和可点击性
        WebElement element = wait.until(driver -> {
            WebElement el = driver.findElement(locator);
            return (el.isDisplayed() && el.isEnabled()) ? el : null;
        });

        // iOS特殊处理：使用W3C标准点击
        if (driver instanceof IOSDriver) {
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1);
            tap.addAction(finger.createPointerMove(
                    Duration.ofMillis(0),
                    PointerInput.Origin.viewport(),
                    element.getLocation().getX() + 10,
                    element.getLocation().getY() + 10
            ));
            tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(new Pause(finger, Duration.ofMillis(100)));
            tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            ((IOSDriver)driver).perform(Collections.singletonList(tap));
        } else {
            element.click();
        }
    }

//    protected void type(By locator, String text) {
//        try {
//            // 1. 等待元素可见且可交互
//            WebElement element = wait.until(driver -> {
//                WebElement el = driver.findElement(locator);
//                return (el.isDisplayed() && el.isEnabled()) ? el : null;
//            });
//
//            // 2. 点击元素确保获取焦点
//            element.click();
//            Thread.sleep(300); // 等待键盘弹出
//
//            // 3. iOS专用文本输入方式
//            if (driver instanceof IOSDriver) {
//                // 方式1: 使用mobile: typeText命令
//                Map<String, Object> args = new HashMap<>();
//                args.put("element", ((RemoteWebElement)element).getId());
//                args.put("text", text);
//                ((JavascriptExecutor)driver).executeScript("mobile: typeText", args);
//
//                // 或者方式2: 使用setValue
//                // Map<String, Object> params = new HashMap<>();
//                // params.put("id", ((RemoteWebElement)element).getId());
//                // params.put("value", text);
//                // driver.executeScript("mobile: setValue", params);
//            }
//            // 4. Android处理
//            else {
//                element.clear();
//                element.sendKeys(text);
//            }
//
//        } catch (Exception e) {
//            // 备用方案: 尝试直接通过sendKeys
//            try {
//                WebElement element = driver.findElement(locator);
//                element.clear();
//                element.sendKeys(text);
//            } catch (Exception ex) {
//                throw new RuntimeException("所有输入方式均失败: " + ex.getMessage(), ex);
//            }
//        }
//    }
    // 通用滑动操作（iOS/Android 兼容）
    protected void swipe() {
        ((JavascriptExecutor) driver).executeScript("mobile: swipeGesture", ImmutableMap.of(
                "left", 840,
                "top", 1120,
                "width", 140,
                "height", 1120,
                "direction", "left",
                "percent", 0.95
        ));
    }

    protected void tap(int x, int y) {
        if (driver instanceof IOSDriver) {
            // iOS 专用方式
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1);
            tap.addAction(finger.createPointerMove(Duration.ofMillis(0),
                    PointerInput.Origin.viewport(), x, y));
            tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(new Pause(finger, Duration.ofMillis(100))); // 添加短暂停顿更可靠
            tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            ((IOSDriver)driver).perform(Collections.singletonList(tap));
        } else {
            // Android 方式
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("x", x);
            parameters.put("y", y);
            driver.executeScript("mobile: clickGesture", parameters);
        }
    }


    public class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    protected void taps(int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Point tapPoint = new Point(x, y);  // 763, 1965
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ofMillis(0),
                        PointerInput.Origin.viewport(), tapPoint.x, tapPoint.y))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(50)))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(tap));
    }

    protected void swipes(int startX, int startY, int endX, int endY) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Point start = new Point(startX, startY);  //912, 1163
        Point end = new Point(endX, endY);//160, 1165
        Sequence swipe = new Sequence(finger, 1);
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0),
                PointerInput.Origin.viewport(), start.x, start.y));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(200),
                PointerInput.Origin.viewport(), end.x, end.y));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(swipe));
    }


    // 滑动到元素
//    protected void scrollToElement(By locator) {
//        MobileElement element = driver.findElement(locator);
//        Point location = element.getLocation();
//        Dimension size = driver.manage().window().getSize();
//        int startX = size.getWidth() / 2;
//        int startY = (int) (size.getHeight() * 0.8);
//        int endY = location.getY() - 100; // 例如，向上滑动一点
//        new TouchAction<>(driver)
//                .press(startX, startY)
//                .waitAction(Duration.ofMillis(500)) // 等待一段时间
//                .moveTo(startX, endY)
//                .release()
//                .perform();
//    }

//    protected void swipe(int startX, int startY, int endX, int endY) {
//        TouchAction touchAction = new TouchAction(driver);
//        touchAction.press(PointOption.point(startX, startY))
//                .moveTo(PointOption.point(endX, endY))
//                .release()
//                .perform();
//    }


//    public void swipe(int startX, int startY, int endX, int endY, int duration) {
//        Point position = driver.manage().window().getPosition();
//        Dimension size = driver.manage().window().getSize();
//
//        Rectangle rect = new Rectangle(position.getX(), position.getY(), size.getWidth(), size.getHeight());
//
//        Point startPoint = new Point(startX, startY);
//        Point endPoint = new Point(endX, endY);
//
//        // 对于滑动的坐标进行适当的调整，以确保在屏幕边界内
//        if (startPoint.x < 0 || startPoint.x > size.width || startPoint.y < 0 || startPoint.y > size.height) {
//            throw new IllegalArgumentException("起始点不在屏幕范围内");
//        }
//        if (endPoint.x < 0 || endPoint.x > size.width || endPoint.y < 0 || endPoint.y > size.height) {
//            throw new IllegalArgumentException("结束点不在屏幕范围内");
//        }
//
//        TouchAction touchAction = new TouchAction<>(driver);
//        touchAction.press(startPoint).waitAction(duration).moveTo(endPoint).release().perform();
//    }

//    public void swipe(int startX, int startY, int endX, int endY, int duration) {
//        Point position = driver.manage().window().getPosition();
//        Dimension size = driver.manage().window().getSize();
//
//        // 计算调整后的起始点和结束点
//        Point startPoint = new Point(startX + position.x, startY + position.y);
//        Point endPoint = new Point(endX + position.x, endY + position.y);
//
//        // 对于滑动的坐标进行适当的调整，以确保在屏幕边界内
//        if (startPoint.x < position.x || startPoint.x > position.x + size.width || startPoint.y < position.y || startPoint.y > position.y + size.height) {
//            throw new IllegalArgumentException("起始点不在屏幕范围内");
//        }
//        if (endPoint.x < position.x || endPoint.x > position.x + size.width || endPoint.y < position.y || endPoint.y > position.y + size.height) {
//            throw new IllegalArgumentException("结束点不在屏幕范围内");
//        }
//
//        TouchAction touchAction = new TouchAction<>(driver);
//        touchAction.press(PointOption.point(startPoint)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(duration))).moveTo(PointOption.point(endPoint)).release().perform();
//    }


//    protected void scrollToElement(int startX, int startY, int endX) {
//        Dimension size = driver.manage().window().getSize();
//        System.out.println("屏幕分辨率：" + size);
//        driver.performTouchAction(new io.appium.java_client.touch.TapOptions().withPosition(PointOption.point(startX, startY))
//                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(1000)))
//                .moveTo(PointOption.point(endX,startY))
//                .release()
//                .perform();
////        Duration duration = Duration.ofMillis(200);
////        TouchAction touchAction = new TouchAction(driver);
////        touchAction.press(PointOption.point(startX, startY))
////                .waitAction(WaitOptions.waitOptions(duration))
////                .moveTo(PointOption.point(endX, startY))
////                .release()
////                .perform();
//        System.out.println("结束：");
//    }


//
//    // 点击坐标位置
//    protected void clickByCoordinates(int x, int y) {
//        new TouchAction<>(driver)
//                .tap(x, y)
//                .perform();
}