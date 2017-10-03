package io.equalexperts.hotel_test.steps;

import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.equalexperts.hotel_test.misc.Session;
import io.equalexperts.hotel_test.pages.BookingPage;
import net.serenitybdd.core.Serenity;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class BookingSteps {

    // we shouldn't have this hard-coded here, but as it is a code test I think it's OK
    private static final String URL = "http://hotel-test.equalexperts.io/";
    // this is also here for simplicity - it could be in a Serenity session if needed to be shared among steps files
    private WebDriver driver;

    @Before
    public void setUp() {
        // I will keep things simple and assume we only care about Chrome
        driver = new ChromeDriver();
    }

    @After
    public void tearDown() {
        if (null != driver) {
            driver.quit();
        }
    }

    @Given("^I am on the booking page$")
    public void onTheBookingPage() {
        driver.get(URL);
    }

    @Given("^I have a booking$")
    public void haveBooking() {
        onTheBookingPage();
        enterDataIntoTheBookingForm();
    }

    @When("^I enter data into the booking form and save it$")
    public void enterDataIntoTheBookingForm(DataTable table) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // List<YourType>, List<List<E>>, List<Map<K,V>> or Map<K,V>.
        // E,K,V must be a scalar (String, Integer, Date, enum etc)
        Map<String, String> data = table.asMap(String.class, String.class);
        String firstName = data.get("firstName");
        String lastName = data.get("lastName");
        String price = data.get("price");
        Boolean depositPaid = Boolean.getBoolean(data.get("deposit"));
        String checkin = data.get("checkin");
        String checkout = data.get("checkout");

        Serenity.setSessionVariable(Session.FIRST_NAME).to(firstName);
        Serenity.setSessionVariable(Session.LAST_NAME).to(lastName);
        Serenity.setSessionVariable(Session.PRICE).to(price);
        Serenity.setSessionVariable(Session.DEPOSIT).to(depositPaid);
        Serenity.setSessionVariable(Session.CHECKIN).to(checkin);
        Serenity.setSessionVariable(Session.CHECKOUT).to(checkout);

        new BookingPage(driver)
                .enterFirstName(firstName)
                .enterSurname(lastName)
                .enterPrice(price)
                .selectDepositPaid(depositPaid)
                .selectCheckin(checkin)
                .selectCheckout(checkout)
                .clickSave();
    }

    private void enterDataIntoTheBookingForm() {
        // yep, this is ugly and should be done in a loop or using streams.
        List<List<String>> data = new ArrayList<List<String>>();
        List<String> firstName = new ArrayList<String>();
        firstName.add("firstName");
        firstName.add("TestName");
        data.add(firstName);

        List<String> lastName = new ArrayList<String>();
        lastName.add("lastName");
        lastName.add("testLastName");
        data.add(lastName);

        List<String> price = new ArrayList<String>();
        price.add("price");
        price.add("3111");
        data.add(price);

        List<String> deposit = new ArrayList<String>();
        deposit.add("deposit");
        deposit.add("true");
        data.add(deposit);

        List<String> checkin = new ArrayList<String>();
        checkin.add("checkin");
        checkin.add("2018-01-01");
        data.add(checkin);

        List<String> checkout = new ArrayList<String>();
        checkout.add("checkout");
        checkout.add("2018-02-01");
        data.add(checkout);

        enterDataIntoTheBookingForm(DataTable.create(data));
    }

    @When("^I click the delete button for that booking$")
    public void clickDeleteButton() {
        new BookingPage(driver).clickDelete();
    }

    @Then("^the booking is complete$")
    public void theBookingIsComplete() {
        BookingPage page = new BookingPage(driver);
        assertThat(page.getLastFirstname(), equalTo(Serenity.sessionVariableCalled(Session.FIRST_NAME)));
        assertThat(page.getLastSurname(), equalTo(Serenity.sessionVariableCalled(Session.LAST_NAME)));
        assertThat(page.getLastPrice(), equalTo(Serenity.sessionVariableCalled(Session.PRICE)));
        assertThat(page.getLastDeposit(), equalTo(Serenity.sessionVariableCalled(Session.DEPOSIT)));
        assertThat(page.getLastCheckin(), equalTo(Serenity.sessionVariableCalled(Session.CHECKIN)));
        assertThat(page.getLastCheckout(), equalTo(Serenity.sessionVariableCalled(Session.CHECKOUT)));
    }

    @Then("^the booking is deleted$")
    public void bookingIsDeleted() {
        BookingPage page = new BookingPage(driver);
        assertThat(page.getCurrentBookingCount(),
                equalTo(((Integer)Serenity.sessionVariableCalled(Session.BOOKING_COUNT) - 1)));
    }
}
