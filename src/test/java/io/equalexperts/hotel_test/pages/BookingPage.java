package io.equalexperts.hotel_test.pages;

import io.equalexperts.hotel_test.misc.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class BookingPage extends AbstractPage {

    @FindBy(id = "firstname")
    private WebElement firstNameTextbox;

    @FindBy(id = "lastname")
    private WebElement lastNameTextbox;

    @FindBy(id = "totalprice")
    private WebElement priceTextbox;

    @FindBy(id = "depositpaid")
    private WebElement depositDropdown;

    @FindBy(id = "checkin")
    private WebElement checkinCalendar;

    @FindBy(id = "checkout")
    private WebElement checkoutCalendar;

    // I'm not a massive fan of this as it may not work in other languages
    // If this were a real application I would probably add an id or at least a css class in order to select this
    @FindBy(css = "input[value=\" Save \"]")
    private WebElement saveButton;

    // Same as above. However, as there can be multiple delete buttons on the page this WebElement will be used
    // in the case when we know there is only one. In order to keep things simple, this code test will always
    // assume one delete button, but in the real world this wouldn't be the case.
    @FindBy(css = "input[value=\"Delete\"]")
    private WebElement deleteButton;

    // these complicated selectors are not great - I would add something to simplify them to the app
    @FindBy(css = "#bookings>.row:last-child>.col-md-2:nth-child(1)")
    private WebElement lastFirstnameDiv;

    @FindBy(css = "#bookings>.row:last-child>.col-md-2:nth-child(2)")
    private WebElement lastSurnameDiv;

    @FindBy(css = "#bookings>.row:last-child>.col-md-1>p")
    private WebElement lastPriceDiv;

    @FindBy(css = "#bookings>.row:last-child>.col-md-2:nth-child(4)")
    private WebElement lastDepositDiv;

    @FindBy(css = "#bookings>.row:last-child>.col-md-2:nth-child(5)")
    private WebElement lastCheckinDiv;

    @FindBy(css = "#bookings>.row:last-child>.col-md-2:nth-child(6)")
    private WebElement lastCheckoutDiv;

    private static final String CURRENT_BOOKINGS = "#bookings>.row";

    public BookingPage(WebDriver driver) {
        super(driver);
    }

    public BookingPage enterFirstName(String name) {
        firstNameTextbox.sendKeys(name);
        return this;
    }

    public BookingPage enterSurname(String surname) {
        lastNameTextbox.sendKeys(surname);
        return this;
    }

    // technically this could be a double or a float
    public BookingPage enterPrice(String price) {
        priceTextbox.sendKeys(price);
        return this;
    }

    // again, this could be more flexible if necessary
    public BookingPage selectDepositPaid(boolean paid) {
        new Select(depositDropdown).selectByVisibleText(String.valueOf(paid));
        return this;
    }

    public BookingPage selectCheckin(String checkin) {
        checkinCalendar.sendKeys(checkin);
        return this;
    }

    public BookingPage selectCheckout(String checkout) {
        checkoutCalendar.sendKeys(checkout);
        return this;
    }

    public BookingPage clickSave() {
        int currentBookingCount = getCurrentBookingCount();
        saveButton.click();
        new Wait(driver, 30).forElementAdded(CURRENT_BOOKINGS, currentBookingCount);
        return new BookingPage(driver);
    }

    public int getCurrentBookingCount() {
        return driver.findElements(By.cssSelector(CURRENT_BOOKINGS)).size();
    }

    public String getLastFirstname() {
        return lastFirstnameDiv.getText();
    }

    public String getLastSurname() {
        return lastSurnameDiv.getText();
    }

    public String getLastPrice() {
        return lastPriceDiv.getText();
    }

    public boolean getLastDeposit() {
        return Boolean.valueOf(lastDepositDiv.getText());
    }

    public String getLastCheckin() {
        return lastCheckinDiv.getText();
    }

    public String getLastCheckout() {
        return lastCheckoutDiv.getText();
    }
}
