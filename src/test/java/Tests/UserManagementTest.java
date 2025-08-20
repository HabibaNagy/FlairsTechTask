package Tests;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.UserManagementPageLocators;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;


public class UserManagementTest extends BaseTest {

    private WebDriver driver;
    private UserManagementPageLocators userManagementPageLocators;
    String beforeCount;
    int beforeDelete;
    String afterCount;
    int afterDelete;

    @Given("user is on login page")
    public void user_is_on_login_page() {
        // Hooks have already run -> driver is initialized
        driver = getDriver();
        userManagementPageLocators = new UserManagementPageLocators(driver);

        driver.navigate().to("https://opensource-demo.orangehrmlive.com/");
    }

    @When("user enters valid username and password")
    public void user_enters_valid_username_and_password() {
        userManagementPageLocators.userEntersCredentials("Admin", "admin123");
        userManagementPageLocators.userClicksLogin();
    }

    @Then("user should be redirected to the homepage")
    public void user_should_be_redirected_to_the_homepage() {
        userManagementPageLocators.verifyLogin();
    }

    @And("user clicks on Admin tab")
    public void user_clicks_on_Admin_tab() {
        userManagementPageLocators.userClicksAdminTab();
    }

    @And("user gets the number of records")
    public void user_gets_the_number_of_records() {
        beforeCount = userManagementPageLocators.getRecordsCount();
        beforeDelete = userManagementPageLocators.extractCount();
        System.out.println(beforeCount);
    }

    @And("user clicks Add button")
    public void user_clicks_Add_button() {
        //userManagementPageLocators.adminClicksAddUser();
    }

    @And("user fills the required data")
    public void user_fills_the_required_data() throws InterruptedException {
//        userManagementPageLocators.selectUserRole();
//        userManagementPageLocators.selectEmployee("Peter Mac Anderson");
//        userManagementPageLocators.selectState();
//        userManagementPageLocators.userEntersNewUsername("NewTest2");
//        userManagementPageLocators.userEntersNewPassword("Test@123");
//        userManagementPageLocators.userEntersConfirmPassword("Test@123");

    }

    @And("user clicks Save button")
    public void user_clicks_Save_button() {
        //userManagementPageLocators.saveNewUser();
    }

    @Then("the number of records should increase by 1")
    public void the_number_of_records_should_increase_by_1() {

    }

    @When("user searches for the new user")
    public void user_searches_for_the_new_user() {

        userManagementPageLocators.userEntersDataToSearch("User99");
        userManagementPageLocators.userClicksSearch();
        userManagementPageLocators.assertOnTheRecordCount();
    }

    @And("user deletes the new user")
    public void admin_deletes_the_new_user() {
        userManagementPageLocators.deleteUser();
        userManagementPageLocators.confirmDeleteUser();
        driver.navigate().refresh();
        userManagementPageLocators.assertOnTheRecordCount();
        afterCount = userManagementPageLocators.getRecordsCount();
    }

    @Then("the number of records should decrease by 1")
    public void the_number_of_records_should_decrease_by_1() {
        afterDelete = userManagementPageLocators.extractCount();
        System.out.println(afterCount);
        Assert.assertEquals(afterDelete, beforeDelete - 1,
                "Record count did not decrease by 1");
    }


}
