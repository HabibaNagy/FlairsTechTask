package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
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

    @FindBy(css = "body > div:nth-child(3) > div:nth-child(1) > div:nth-child(2) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > form:nth-child(3) > div:nth-child(1) > div:nth-child(1) > div:nth-child(3) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > div:nth-child(1)")
    WebElement selectState;

    @FindBy(css = "div[class='oxd-form-row'] div[class='oxd-grid-2 orangehrm-full-width-grid'] div[class='oxd-grid-item oxd-grid-item--gutters'] div[class='oxd-input-group oxd-input-field-bottom-space'] div input[class='oxd-input oxd-input--active']")
    WebElement newUsernameField;
    @FindBy(xpath = "(//input[@type='password'])[1]")
    WebElement newPassword;
    @FindBy(xpath = "(//input[@type='password'])[2]")
    WebElement confirmPassword;
    @FindBy(css = "button[type='submit']")
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

    public void adminClicksAddUser() {
        addUser.click();
    }

    public void selectUserRole(String roleName) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // 1. Click the dropdown to open it
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(userRole));
        dropdown.click();

        // 2. Wait for the options to appear
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class,'oxd-select-option') and contains(., '" + roleName + "')]")));

        Actions actions = new Actions(driver);

        // 3. Press ARROW_DOWN to navigate (if needed)
        actions.sendKeys(Keys.ARROW_DOWN).perform();
        Thread.sleep(500);
        actions.sendKeys(Keys.ARROW_DOWN).perform();
        Thread.sleep(500);

        // 4. Press ENTER to select
        actions.sendKeys(Keys.ENTER).perform();

        // 5. CRUCIAL: Wait for the dropdown to close and value to update
        Thread.sleep(2000); // Wait for UI update

        // 6. Verify the selection PROPERLY
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String currentValue = (String) js.executeScript("return arguments[0].textContent;", dropdown);
        System.out.println("After selection: " + currentValue);

        // Alternative verification: Check if the value contains the selected role
        if (currentValue.contains(roleName)) {
            System.out.println(" Successfully selected: " + roleName);
        } else {
            System.out.println(" Selection may have failed. Expected: " + roleName + ", Got: " + currentValue);
        }
    }


    public void userEntersEmployeeName(String employeeName) throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // 1. Click the dropdown to open it
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(employeeNameField));
        setText(dropdown, employeeName);

        WebElement dropdownOptions = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class,'oxd-autocomplete-option')]//*[contains(text(), '" + employeeName + "')]")));
        // Method 1: Using the WebElement directly
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script2 = "return document.querySelector('input[placeholder=\"Type for hints...\"]').value;";
        String empTxt = (String) js.executeScript(script2);
        System.out.println("Employee field name :" + empTxt);
        Actions actions = new Actions(driver);
        while (!(empTxt.equalsIgnoreCase(employeeName))) {
            actions.sendKeys(dropdownOptions, Keys.ARROW_DOWN).perform();
            Thread.sleep(4000);
            empTxt = (String) js.executeScript(script2);
        }
        actions.sendKeys(dropdownOptions, Keys.ENTER).perform();
        Thread.sleep(2000);
        //  System.out.println("After selection: " + dropdownOptions.getText());


    }


    public void selectState(String state) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // 1. Click the dropdown to open it
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(selectState));
        dropdown.click();

        // 2. Wait for the options to appear
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class,'oxd-select-option') and contains(., '" + state + "')]")));

        Actions actions = new Actions(driver);

        // 3. Press ARROW_DOWN to navigate (if needed)
        actions.sendKeys(Keys.ARROW_DOWN).perform();
        Thread.sleep(500);

        // 4. Press ENTER to select
        actions.sendKeys(Keys.ENTER).perform();

        // 5. CRUCIAL: Wait for the dropdown to close and value to update
        Thread.sleep(500); // Wait for UI update

        // 6. Verify the selection PROPERLY
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String currentValue = (String) js.executeScript("return arguments[0].textContent;", dropdown);
        System.out.println("After selection: " + currentValue);

        // Alternative verification: Check if the value contains the selected role
        if (currentValue.contains(state)) {
            System.out.println("✓ Successfully selected: " + state);
        } else {
            System.out.println("✗ Selection may have failed. Expected: " + state + ", Got: " + currentValue);
        }
    }


    public void userEntersNewUsername(String newUsernameTxt) {
        setText(newUsernameField, newUsernameTxt);
    }

    public void userEntersNewPassword(String newPasswordTxt) {
        setText(newPassword, newPasswordTxt);
    }

    public void userEntersConfirmPassword(String confirmPasswordTxt) {
        setText(confirmPassword, confirmPasswordTxt);
    }

    public void saveNewUser() {
        saveBtn.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Wait for the success toast message to appear
        WebElement successToast = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".oxd-toast.oxd-toast--success.oxd-toast-container--toast")));
        // Get the success message text
        String successMessage = successToast.getText();
        System.out.println("Success: " + successMessage);

        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".oxd-toast.oxd-toast--success.oxd-toast-container--toast")));
        } catch (Exception e) {

        }
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
