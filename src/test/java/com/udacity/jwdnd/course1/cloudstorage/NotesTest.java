package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.selenium.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.selenium.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.selenium.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotesTest {
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
    public void testNoteCreated() throws InterruptedException {
        createNote("TestNote", "password1");
    }

    private void createNote(String name, String password) throws InterruptedException {
        signupAndLogin(name, password);

        // Go to home
        driver.get("http://localhost:" + port + "/home");
        WebDriverWait wait = new WebDriverWait(driver, 3);
        WebElement homeMarker = wait.until(webDriver -> webDriver.findElement(By.id("nav-files-tab")));
        assertNotNull(homeMarker);

        // Enter note
        homePage.enterNote("NoteTitle", "NoteText");
        Thread.sleep(1000);

        // Check note added
        assertEquals("NoteTitle", homePage.getFirstNoteTitle());
        assertEquals("NoteText", homePage.getFirstNoteText());
    }

    private void signupAndLogin(String name, String password) {
        //Signup
        signupPage.signUp("TestFirst", "TestLast", name, password);

        //Login
        driver.get("http://localhost:" + port + "/login");
        loginPage.enterUsernameAndPassword(name, password);
    }

    @Test
    public void testNoteEdited() throws InterruptedException {
        createNote("testNote", "password1");
        homePage.editNote("EditTitle", "EditText");
        Thread.sleep(1000);

        assertEquals("EditTitle", homePage.getFirstNoteTitle());
        assertEquals("EditText", homePage.getFirstNoteText());
    }

    @Test
    public void testNoteDeleted() throws InterruptedException {
        createNote("deleteNote", "password1");
        homePage.deleteNote();
        Thread.sleep(1000);

        try {
            homePage.getFirstNoteTitle();
            fail("Note not deleted");
        } catch (NoSuchElementException e) {
            assertTrue(true);
        }
    }
}
