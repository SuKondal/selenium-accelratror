package test.java;

import static org.testng.AssertJUnit.assertTrue;


import org.openqa.selenium.*;


public class HA_CancelBooking {
  private WebDriver driver1;
  private String baseUrl;
  private boolean acceptNextAlert = true;
 
  public void Call(WebDriver driver)
  {
    this.driver1= driver;
	  driver.get(baseUrl + "/HotelApp/index.php");
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("adactin123");
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("adactin123");
    driver.findElement(By.id("login")).click();
    driver.findElement(By.linkText("Booked Itinerary")).click();
    driver.findElement(By.xpath(".//*[@id='order_id_text']")).sendKeys("3S2N8YS0MY");
    driver.findElement(By.xpath(".//*[@id='search_hotel_id']")).click();
    driver.findElement(By.id("btn_id_666")).click();
    assertTrue(closeAlertAndGetItsText().matches("^Are you sure, you want to cancel the booking with Order no\\. 2YWS2DUOCV[\\s\\S]$"));
  }

  
  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver1.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
