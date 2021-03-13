package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import com.udacity.jwdnd.course1.cloudstorage.services.NotesService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@Autowired
	NotesService notesService;

	@Autowired
	UserService userService;

	@Autowired
	CredentialsService credentialsService;

	@LocalServerPort
	private int port;

	private final String USER_FIRSTNAME = "a";
	private final String USER_LASTNAME = "a";
	private final String USER_USERNAME = "a";
	private final String USER_PASSWORD = "a";

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {

		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	private void createUserAndLogin() {
		driver.get("http://localhost:" + this.port + "/signup");
		driver.findElement(By.id("inputFirstName")).sendKeys("value", USER_FIRSTNAME);
		driver.findElement(By.id("inputLastName")).sendKeys("value", USER_LASTNAME);
		driver.findElement(By.id("inputUsername")).sendKeys("value", USER_USERNAME);
		driver.findElement(By.id("inputPassword")).sendKeys("value", USER_PASSWORD);
		driver.findElement(By.id("inputPassword")).submit();

		// Login (page redirects to login after signup)
		driver.get("http://localhost:" + this.port + "/login");
		driver.findElement(By.id("inputUsername")).sendKeys("value", USER_USERNAME);
		driver.findElement(By.id("inputPassword")).sendKeys("value", USER_PASSWORD);
		driver.findElement(By.id("inputPassword")).submit();
	}

	// Write a test that verifies that an unauthorized user can only access the login and signup pages.
	@Test
	public void visitHomePageWithoutLogin() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	// Write a test that signs up a new user, logs in, verifies that the home page is accessible,
	// logs out, and verifies that the home page is no longer accessible.
	@Test
	public void signupUserAndAccessHomePage() {
		createUserAndLogin();
		Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());
		driver.findElement(By.id("logoutForm")).submit();
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	// Write a test that creates a note, and verifies it is displayed.
	@Test
	public void writeNoteAndVerifiesItsDisplayed() {
		createUserAndLogin();
		driver.get("http://localhost:" + this.port + "/home?notes");

		String noteTitle = "My note title";
		String noteDescription = "My note description";

		driver.findElement(By.id("addNote")).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));

		driver.findElement(By.id("note-title")).sendKeys(noteTitle);
		driver.findElement(By.id("note-description")).sendKeys(noteDescription);
		driver.findElement(By.id("noteForm")).submit();

		Note note = notesService.getByUserId(1L).get(0);
		WebElement element = driver.findElement(By.id("note-"+note.getId()+"-title"));

		Assertions.assertEquals(element.getText(), note.getTitle());
	}

	// Write a test that edits an existing note and verifies that the changes are displayed.
	@Test
	public void editNoteAndVerifiesItsDisplayed() {
		createUserAndLogin();
		Note note = new Note();
		note.setTitle("Hello");
		note.setDescription("World");
		note.setUserId(1L);
		User user = userService.findAll().get(0);
		notesService.insert(note, user);

		driver.get("http://localhost:" + this.port + "/home?notes");
		String noteTitle = "edit My note title";
		String noteDescription = "edit My note description";

		driver.findElement(By.id("note-"+note.getId()+"-edit")).click();

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));

		driver.findElement(By.id("note-title")).clear();
		driver.findElement(By.id("note-title")).sendKeys(noteTitle);
		driver.findElement(By.id("note-description")).clear();
		driver.findElement(By.id("note-description")).sendKeys(noteDescription);
		driver.findElement(By.id("noteForm")).submit();

		WebElement titleElement = driver.findElement(By.id("note-"+note.getId()+"-title"));
		Assertions.assertEquals(titleElement.getText(), "edit My note title");

		WebElement descriptionElement = driver.findElement(By.id("note-"+note.getId()+"-description"));
		Assertions.assertEquals(descriptionElement.getText(), "edit My note description");
	}

	// Write a test that deletes a note and verifies that the note is no longer displayed.
	@Test
	public void deleteNoteAndVerifiesItsNotDisplayed() {
		createUserAndLogin();
		Note note = new Note();
		note.setTitle("Hello");
		note.setDescription("World");
		note.setUserId(1L);
		User user = userService.findAll().get(0);
		notesService.insert(note, user);

		driver.get("http://localhost:" + this.port + "/home?notes");

		driver.findElement(By.id("note-"+note.getId()+"-delete")).submit();

		Assertions.assertEquals(0, driver.findElements(By.id("note-"+note.getId()+"-title")).size());
		Assertions.assertEquals(0, notesService.getAll().size());
	}

	// Write a test that creates a set of credentials, verifies that they are displayed, and verifies that the displayed password is encrypted.
	@Test
	public void createCredentialAndVerifiesItsDisplayed() throws InterruptedException {
		createUserAndLogin();
		driver.get("http://localhost:" + this.port + "/home?credentials");

		User user = userService.findAll().get(0);

		List<Credential> creds = new ArrayList<>();
		creds.add(new Credential("https://google.com", "google", "google_password"));
		creds.add(new Credential("https://facebook.com", "facebook", "facebook_password"));
		creds.add(new Credential("https://udacity.com", "udacity", "udacity_password"));

		for(Credential cred: creds) {
			Thread.sleep(500); // Loops over clicks gets broken for some reason.. have to wait couple of millie seconds
			driver.findElement(By.id("credential-new")).click();
			WebDriverWait wait2 = new WebDriverWait(driver, 5);
			wait2.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
			driver.findElement(By.id("credential-url")).sendKeys(cred.getUrl());
			driver.findElement(By.id("credential-username")).sendKeys(cred.getUsername());
			driver.findElement(By.id("credential-password")).sendKeys(cred.getPassword());
			driver.findElement(By.id("credential-password")).submit();

			List<Credential> credsList = credentialsService.getByUserId(user.getUserId());
			Credential targetCred = credsList.get(credsList.size()-1);

			WebElement urlElement = driver.findElement(By.id("credential-"+targetCred.getCredentialid()+"-url"));
			Assertions.assertEquals(urlElement.getText(), cred.getUrl());

			WebElement usernameElement = driver.findElement(By.id("credential-"+targetCred.getCredentialid()+"-username"));
			Assertions.assertEquals(usernameElement.getText(), cred.getUsername());

			WebElement passwordElement = driver.findElement(By.id("credential-"+targetCred.getCredentialid()+"-password"));
			Assertions.assertEquals(passwordElement.getText(), targetCred.getPassword());
		}
	}

	// Write a test that views an existing set of credentials, verifies that the viewable password is unencrypted,
	// edits the credentials, and verifies that the changes are displayed.
	@Test
	public void editCredentialAndVerifiesItsDisplayed() {
		createUserAndLogin();
		User user = userService.findAll().get(0);

		credentialsService.save(new Credential("https://google.com", "google", "google_password"), user);

		driver.get("http://localhost:" + this.port + "/home?credentials");

		Credential cred = credentialsService.getByUserId(user.getUserId()).get(0);

		String newUrl = "https://twitter.com";
		String newUserName = "twitter";
		String newPassword = "twitter_password";

		driver.findElement(By.id("credential-edit-"+cred.getCredentialid())).click();
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		driver.findElement(By.id("credential-url")).clear();
		driver.findElement(By.id("credential-url")).sendKeys(newUrl);
		driver.findElement(By.id("credential-username")).clear();
		driver.findElement(By.id("credential-username")).sendKeys(newUserName);
		driver.findElement(By.id("credential-password")).clear();
		driver.findElement(By.id("credential-password")).sendKeys(newPassword);
		driver.findElement(By.id("credential-password")).submit();

		WebElement urlElement = driver.findElement(By.id("credential-"+cred.getCredentialid()+"-url"));
		Assertions.assertEquals(urlElement.getText(), newUrl);

		WebElement usernameElement = driver.findElement(By.id("credential-"+cred.getCredentialid()+"-username"));
		Assertions.assertEquals(usernameElement.getText(), newUserName);
	}

	// Write a test that deletes an existing set of credentials and verifies that the credentials are no longer displayed.
	@Test
	public void deleteCredentialAndVerifiesItsDisplayed() {
		createUserAndLogin();
		User user = userService.findAll().get(0);

		credentialsService.deleteAll();
		credentialsService.save(new Credential("https://google.com", "google", "google_password"), user);

		driver.get("http://localhost:" + this.port + "/home?credentials");

		Credential cred = credentialsService.getByUserId(user.getUserId()).get(0);

		driver.findElement(By.id("credential-delete-"+cred.getCredentialid())).submit();

		Assertions.assertEquals(0, driver.findElements(By.id("credential-"+cred.getCredentialid()+"-url")).size());
		Assertions.assertEquals(0, credentialsService.getByUserId(user.getUserId()).size());
	}
}
