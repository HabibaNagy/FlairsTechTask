package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UserManagementPageLocators extends BasePage {

    public UserManagementPageLocators(WebDriver driver) {
        super(driver);
    }

    @FindBy(name = "username")
    WebElement usernameField;

    @FindBy(name = "password")
    WebElement passwordField;

    @FindBy(xpath = "//button[normalize-space()='Login']")
    WebElement login;

    @FindBy(tagName = "h6")
    WebElement homePage;
    @FindBy(xpath = "//span[normalize-space()='Admin']")
    WebElement adminTab;

    @FindBy(css = "div[class='orangehrm-horizontal-padding orangehrm-vertical-padding'] span[class='oxd-text oxd-text--span']")
    WebElement recordsCount;

    @FindBy(xpath = "//button[normalize-space()='Add']")
    WebElement addUser;

    @FindBy(css = "#app > div.oxd-layout.orangehrm-upgrade-layout > div.oxd-layout-container > div.oxd-layout-context > div > div > form > div:nth-child(1) > div > div:nth-child(1) > div > div:nth-child(2)")
    WebElement userRole;
    @FindBy(xpath = "//div[contains(@class,'oxd-select-wrapper')]//div[text()='Admin']")
    WebElement userRoleOptionXpath;
    @FindBy(xpath = "(//input[@placeholder='Type for hints...'])[1]")
    WebElement employeeNameField;

    @FindBy(xpath = "(//i[@class='oxd-icon bi-caret-up-fill oxd-select-text--arrow'])[1]")
    WebElement selectState;

    @FindBy(xpath = "(//div[@class='oxd-input-group oxd-input-field-bottom-space'])[4]")
    WebElement newUsername;
    @FindBy(xpath = "(//input[@type='password'])[1]")
    WebElement newPassword;
    @FindBy(xpath = "(//input[@type='password'])[2]")
    WebElement confirmPassword;

    @FindBy(xpath = "//button[normalize-space()='Save']")
    WebElement saveBtn;


    @FindBy(css = "#app > div.oxd-layout.orangehrm-upgrade-layout > div.oxd-layout-container > div.oxd-layout-context > div > div.oxd-table-filter > div.oxd-table-filter-area > form > div.oxd-form-row > div > div:nth-child(1) > div > div:nth-child(2) > input")
    WebElement searchField;
    @FindBy(xpath = "(//button[normalize-space()='Search'])[1]")
    WebElement searchBtn;
    @FindBy(xpath = "(//button[@type='button'])[7]")
    WebElement deleteUser;

    @FindBy(xpath = "//*[@id='app']/div[3]/div/div/div/div[3]/button[2]")
    WebElement confirmDelete;

    public void userEntersCredentials(String username, String password) {
        setText(usernameField, username);
        setText(passwordField, password);
    }

    public void userClicksLogin() {
        login.click();
    }

    public void verifyLogin() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement dashboardHeader = wait.until(ExpectedConditions.visibilityOf(homePage));

        if (!dashboardHeader.getText().equals("Dashboard")) {
            throw new AssertionError("Login failed. Expected 'Dashboard' but got: " + dashboardHeader.getText());
        }
    }

    public void userClicksAdminTab() {
        adminTab.click();
    }

    public String getRecordsCount() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement numberOfRecords = wait.until(ExpectedConditions.visibilityOf(recordsCount));
        String recordCountTxt = numberOfRecords.getText();
        return recordCountTxt;
    }

    public void adminClicksAddUser() {
        addUser.click();
    }


    public void selectUserRole() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // 1. Click the dropdown to open it
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(userRole));
        dropdown.click();
        Actions actions = new Actions(driver);

        // Press ARROW_DOWN once to go to the first option (Admin in your case)
        actions.sendKeys(dropdown, Keys.ARROW_DOWN).perform();
        actions.sendKeys(dropdown, Keys.ARROW_DOWN).perform();
        actions.sendKeys(dropdown, Keys.ARROW_DOWN).perform();
        // 2. Wait for the option with text "Admin" to appear and click it
        WebElement adminOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'oxd-select-text-input') and text()='Admin']"))
        );
        adminOption.click();
        System.out.println("After selection: " + dropdown.getText());
    }


    public void userEntersEmployeeName(String employeeName) throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // 1. Click the dropdown to open it
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(employeeNameField));
        // 2. Use JavaScript to set the value
        String typeScript = String.format(
                "var input = arguments[0]; " +
                        "input.value = '%s'; " +
                        "input.focus(); " +
                        "var inputEvent = new InputEvent('input', { bubbles: true }); " +
                        "var changeEvent = new Event('change', { bubbles: true }); " +
                        "input.dispatchEvent(inputEvent); " +
                        "input.dispatchEvent(changeEvent);",
                employeeName
        );
        ((JavascriptExecutor) driver).executeScript(typeScript, dropdown);

        WebElement employeeOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@class='oxd-autocomplete-option' and contains(., '" + employeeName + "')]")
        ));

        employeeOption.click();
        System.out.println("After selection: " + dropdown.getText());


    }

    public void selectEmployee(String employeeName) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // 1. Find the autocomplete input and type the employee name hint
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'oxd-autocomplete-text-input')]//input")
        ));
        input.clear();
        input.sendKeys(employeeName);

        // 2. Wait for the dropdown options to appear
        WebElement option = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'oxd-autocomplete-option')]//*[contains(text(), '" + employeeName + "')]")
        ));
        Actions actions = new Actions(driver);
        actions.sendKeys(option, Keys.ARROW_DOWN).perform();
        actions.sendKeys(option, Keys.ENTER).perform();
        // 3. Click the option using JS if normal click fails
        try {
            option.click();
        } catch (Exception e) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", option);
        }

        // Debug print
        System.out.println("Selected employee: " + employeeName);
    }

    public void selectState() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // 1. Click the dropdown to open it
        WebElement dropdownState = wait.until(ExpectedConditions.elementToBeClickable(selectState));
        dropdownState.click();
        Actions actions = new Actions(driver);

        // Press ARROW_DOWN once to go to the first option (Admin in your case)
        actions.sendKeys(dropdownState, Keys.ARROW_DOWN).perform();
        actions.sendKeys(dropdownState, Keys.ARROW_DOWN).perform();
        actions.sendKeys(dropdownState, Keys.ARROW_DOWN).perform();
        actions.sendKeys(dropdownState, Keys.ENTER).perform();
        actions.sendKeys(dropdownState, Keys.ENTER).perform();
        System.out.println("After selection: " + dropdownState.getText());
    }

    public void userEntersNewPassword(String newPasswordTxt) {
        setText(newPassword, newPasswordTxt);
    }

    public void userEntersNewUsername(String newUsernameTxt) {
        setText(newUsername, newUsernameTxt);
    }

    public void userEntersConfirmPassword(String confirmPasswordTxt) {
        setText(confirmPassword, confirmPasswordTxt);
    }

    public void saveNewUser() {
        saveBtn.click();
    }

    public void userEntersDataToSearch(String username) {
        setText(searchField, username);
    }

    public void userClicksSearch() {
        searchBtn.click();
    }

    public void deleteUser() {
        deleteUser.click();
    }

    public void confirmDeleteUser() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", confirmDelete);


    }

    public int extractCount() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));


        WebElement recordLabel = wait.until(ExpectedConditions.visibilityOf(recordsCount));


        String textCount = recordLabel.getText();
        System.out.println("Record label text: " + textCount);


        String number = textCount.replaceAll("[^0-9]", "");

        if (number.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(number);
    }

    public void assertOnTheRecordCount() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOf(recordsCount));

    }
}
