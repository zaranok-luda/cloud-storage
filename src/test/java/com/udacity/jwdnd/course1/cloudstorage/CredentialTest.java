package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.selenium.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.selenium.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.selenium.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialTest {
    @LocalServerPort
    private Integer port;

    private static WebDriver driver;
    private SignupPage signupPage;
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterAll
    public static void afterAll() {
        driver.quit();
    }

    @BeforeEach
    public void beforeEach() {
        driver.get("http://localhost:" + port + "/signup");
        signupPage = new SignupPage(driver);
        loginPage = new LoginPage(driver);
        homePage = new HomePage(driver);
    }

    @Test
    public void testCreateCredentials() throws InterruptedException {
        //Signup
        signUpAndGoToHome("CreateCredentials");

        List<HomePage.CredentialInTest> credentialsToSubmit = new ArrayList<>();
        credentialsToSubmit.add(new HomePage.CredentialInTest("", "http://testurl.com", "testusername", "testpassword"));
        credentialsToSubmit.add(new HomePage.CredentialInTest("", "http://testurl1.com", "testusername1", "testpassword1"));
        credentialsToSubmit.add(new HomePage.CredentialInTest("", "http://testurl2.com", "testusername2", "testpassword2"));
        credentialsToSubmit.add(new HomePage.CredentialInTest("", "http://testurl3.com", "testusername3", "testpassword3"));

        homePage.enterCredentials(credentialsToSubmit);

        List<HomePage.CredentialInTest> displayedCredentials = homePage.getDisplayedCredentials();

        for (int i = 0; i < credentialsToSubmit.size(); i++) {
            HomePage.CredentialInTest submittedCredential = credentialsToSubmit.get(i);
            HomePage.CredentialInTest displayedCredential = displayedCredentials.get(i);
            assertEquals(submittedCredential.getUrl(), displayedCredential.getUrl());
            assertEquals(submittedCredential.getUsername(), displayedCredential.getUsername());
            assertNotEquals(submittedCredential.getPassword(), displayedCredential.getPassword(), "password is not displayed");
        }
    }

    @Test
    public void testCreateCredentialsEdit() throws InterruptedException {
        signUpAndGoToHome("EditCredential");

        List<HomePage.CredentialInTest> credentialsToSubmit = new ArrayList<>();
        credentialsToSubmit.add(new HomePage.CredentialInTest("", "http://testurl.com", "testusername", "testpassword"));
        credentialsToSubmit.add(new HomePage.CredentialInTest("", "http://testurl.com", "testusername1", "testpassword1"));
        credentialsToSubmit.add(new HomePage.CredentialInTest("", "http://testurl.com", "testusername2", "testpassword2"));

        homePage.enterCredentials(credentialsToSubmit);

        List<HomePage.CredentialInTest> displayedCredentials = homePage.getDisplayedCredentials();
        List<HomePage.CredentialInTest> credentialsEdit = new ArrayList<>();

        for (int i = 0; i < displayedCredentials.size(); i++) {
            String showedPassword = homePage.getShowedPasswordForCredentialId(displayedCredentials.get(i).getId());
            assertEquals(credentialsToSubmit.get(i).getPassword(), showedPassword, "unencrypted passwords are equal");
        }

        // TEST edit
        for (int i = 0; i < displayedCredentials.size(); i++) {
            HomePage.CredentialInTest displayedCredential = displayedCredentials.get(i);
            credentialsEdit.add(new HomePage.CredentialInTest(displayedCredential.getId(), "New url " + i, "new username " + i, "new password " + i));
        }

        homePage.editCredentials(credentialsEdit);

        List<HomePage.CredentialInTest> displayedCredentialsAfterEdit = homePage.getDisplayedCredentials();
        for (int i = 0; i < displayedCredentialsAfterEdit.size(); i++) {
            HomePage.CredentialInTest displayedCredential = displayedCredentialsAfterEdit.get(i);
            String showedPassword = homePage.getShowedPasswordForCredentialId(displayedCredential.getId());
            assertEquals("new password " + i, showedPassword);
            assertEquals("new username " + i, displayedCredential.getUsername());
            assertEquals("New url " + i, displayedCredential.getUrl());
        }
    }

    @Test
    public void testDeleteCredential() throws InterruptedException {
        //Signup
        signUpAndGoToHome("DeleteCredential");

        List<HomePage.CredentialInTest> credentialsToSubmit = new ArrayList<>();
        credentialsToSubmit.add(new HomePage.CredentialInTest("", "http://testurl.com", "testusername", "testpassword"));
        credentialsToSubmit.add(new HomePage.CredentialInTest("", "http://testurl.1.com", "testusername1", "testpassword1"));
        credentialsToSubmit.add(new HomePage.CredentialInTest("", "http://testurl.2.com", "testusername2", "testpassword2"));

        homePage.enterCredentials(credentialsToSubmit);

        // TEST Credentials are created
        List<HomePage.CredentialInTest> displayedCredentials = homePage.getDisplayedCredentials();
        assertEquals(credentialsToSubmit.size(), displayedCredentials.size());

        homePage.deleteCredentials(displayedCredentials);

        List<HomePage.CredentialInTest> displayedCredentialsAfterDelete = homePage.getDisplayedCredentials();
        assertEquals(0, displayedCredentialsAfterDelete.size());
    }

    private void signUpAndGoToHome(String username) {
        signupPage.signUp("Test", "test", username, "testpassword");

        //Login
        driver.get("http://localhost:" + port + "/login");
        loginPage.enterUsernameAndPassword(username, "testpassword");

        // Go to home
        driver.get("http://localhost:" + port + "/home");
        WebDriverWait wait = new WebDriverWait(driver, 3);
        WebElement homeMarker = wait.until(webDriver -> webDriver.findElement(By.id("nav-files-tab")));
        assertNotNull(homeMarker);
    }
}
