package com.uniovi.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_LoginView;
import com.uniovi.tests.pageobjects.PO_PrivateView;
import com.uniovi.tests.pageobjects.PO_Properties;
import com.uniovi.tests.pageobjects.PO_RegisterView;
import com.uniovi.tests.pageobjects.PO_SearchOferta;
import com.uniovi.tests.pageobjects.PO_UsersView;
import com.uniovi.tests.pageobjects.PO_View;
import com.uniovi.tests.util.SeleniumUtils;

//Ordenamos las pruebas por el nombre del método
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WallapopTests {

	static String PathFirefox65 = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
	static String Geckdriver024 = "E:\\xurde\\Clase\\3\\SDI\\PL-SDI-Sesión5-material\\PL-SDI-Sesión5-material\\geckodriver024win64.exe";
	
	static String Geckdriver024_Alberto = "D:\\Temporal\\_CLASE\\SDI\\PL-SDI-Sesión5-material\\geckodriver024win64.exe";

	static WebDriver driver = getDriver(PathFirefox65, Geckdriver024);
	static String URL = "http://localhost:8080";

	public static WebDriver getDriver(String PathFirefox, String Geckdriver) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		System.setProperty("webdriver.gecko.driver", Geckdriver);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	// Antes de cada prueba se navega al URL home de la aplicación
	@Before
	public void setUp() {
		driver.navigate().to(URL);
	}

	// Después de cada prueba se borran las cookies del navegador
	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	}

	// Antes de la primera prueba
	@BeforeClass
	static public void begin() {
	}

	// Al finalizar la última prueba
	@AfterClass
	static public void end() {
		// Cerramos el navegador al finalizar las pruebas
		driver.quit();
	}
	
	// Registro de Usuario con datos válidos
	@Test
	public void PR01() {
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		PO_RegisterView.fillForm(driver, "uwu@gmail.com", "Juan", "Perez", "77777", "77777");
		PO_View.checkElement(driver, "text", "uwu@gmail.com");
	}
	
	// Registro de Usuario con datos inválidos (email vacío, nombre vacío, apellidos vacíos).
	@Test
	public void PR02() {
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		PO_RegisterView.fillForm(driver, "", "", "", "77777", "77777");
		PO_View.checkKey(driver, "Error.empty", PO_Properties.getSPANISH());
	}
	
	// Registro de Usuario con datos inválidos (repetición de contraseña inválida)
	@Test
	public void PR03() {
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		PO_RegisterView.fillForm(driver, "ewe@gmail.com", "Juan", "Perez", "77777", "77778");
		PO_View.checkKey(driver, "Error.signup.passwordConfirm.coincidence", PO_Properties.getSPANISH());
	}
	
	// Registro de Usuario con datos inválidos (email existente).
	@Test
	public void PR04() {
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		PO_RegisterView.fillForm(driver, "uwu@gmail.com", "Juan", "Perez", "77777", "77777");
		PO_View.checkKey(driver, "Error.signup.email.duplicate", PO_Properties.getSPANISH());
	}
	
	// Inicio de sesión con datos válidos (administrador).
	@Test
	public void PR05() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
		PO_View.checkElement(driver, "text", "admin@email.com");
	}
	
	// Inicio de sesión con datos válidos (usuario estándar).
	@Test
	public void PR06() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "uwu@gmail.com", "77777");
		PO_View.checkElement(driver, "text", "uwu@gmail.com");
	}
	
	// Inicio de sesión con datos inválidos (usuario estándar, campo email y contraseña vacíos)
	@Test
	public void PR07() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "", "");
		PO_View.checkKey(driver, "Error.signup.email.wrong", PO_Properties.getSPANISH());
	}
	
	// Inicio de sesión con datos válidos (usuario estándar, email existente, pero contraseña incorrecta).
	@Test
	public void PR08() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "uwu@gmail.com", "77778");
		PO_View.checkKey(driver, "Error.login.password.wrong", PO_Properties.getSPANISH());
	}
	
	// Inicio de sesión con datos inválidos (usuario estándar, email no existente en la aplicación).
	@Test
	public void PR09() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "ewe@gmail.com", "77777");
		PO_View.checkKey(driver, "Error.signup.email.wrong", PO_Properties.getSPANISH());
	}
	
	// Hacer click en la opción de salir de sesión y comprobar que se redirige a la página de inicio de sesión (Login)
	@Test
	public void PR10() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "uwu@gmail.com", "77777");
		PO_View.checkElement(driver, "text", "uwu@gmail.com");
		PO_HomeView.clickOption(driver, "logout", "class", "btn-primary");
		PO_View.checkElement(driver, "text", "Iniciar sesión");
	}
	
	// Comprobar que el botón cerrar sesión no está visible si el usuario no está autenticado.
	@Test
	public void PR11() {
		SeleniumUtils.EsperaCargaPaginaNoTexto(driver, "Desconectar", PO_View.getTimeout());
	}
	
	// Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el sistema. 
	@Test
	public void PR12() {
		
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		PO_RegisterView.fillForm(driver, "uw1@gmail.com", "Juan", "Perez", "77777", "77777");
		driver.manage().deleteAllCookies();
		driver.navigate().to(URL);
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		PO_RegisterView.fillForm(driver, "uw2@gmail.com", "Juan", "Perez", "77777", "77777");
		driver.manage().deleteAllCookies();
		driver.navigate().to(URL);
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		PO_RegisterView.fillForm(driver, "uw3@gmail.com", "Juan", "Perez", "77777", "77777");
		driver.manage().deleteAllCookies();
		driver.navigate().to(URL);
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		PO_RegisterView.fillForm(driver, "uw4@gmail.com", "Juan", "Perez", "77777", "77777");
		driver.manage().deleteAllCookies();
		driver.navigate().to(URL);
		PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
		PO_RegisterView.fillForm(driver, "uw5@gmail.com", "Juan", "Perez", "77777", "77777");
		driver.manage().deleteAllCookies();
		driver.navigate().to(URL);
		
		
		
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
		PO_HomeView.clickOption(driver, "user/list", "class", "btn btn-danger");
		PO_View.checkElement(driver, "text", "Lolas132@yahoo.es");
		PO_View.checkElement(driver, "text", "M4ras@gmail.com");
		PO_View.checkElement(driver, "text", "12_asMar@gmail.com");
		PO_View.checkElement(driver, "text", "uwu@gmail.com");
		PO_View.checkElement(driver, "text", "uw1@gmail.com");
		PO_View.checkElement(driver, "text", "uw2@gmail.com");
		PO_View.checkElement(driver, "text", "uw3@gmail.com");
		PO_View.checkElement(driver, "text", "uw4@gmail.com");
		PO_View.checkElement(driver, "text", "uw5@gmail.com");
	}
	
	// Ir a la lista de usuarios, borrar el primer usuario de la lista, comprobar que la lista se actualiza y que el usuario desaparece
	@Test
	public void PR13() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
		PO_HomeView.clickOption(driver, "user/list", "class", "btn btn-danger");
		PO_UsersView.deleteUser(driver, 0);
		SeleniumUtils.textoNoPresentePagina(driver, "Lolas132@yahoo.es");
		PO_View.checkElement(driver, "text", "M4ras@gmail.com");
		PO_View.checkElement(driver, "text", "12_asMar@gmail.com");
		PO_View.checkElement(driver, "text", "uwu@gmail.com");
		PO_View.checkElement(driver, "text", "uw1@gmail.com");
		PO_View.checkElement(driver, "text", "uw2@gmail.com");
		PO_View.checkElement(driver, "text", "uw3@gmail.com");
		PO_View.checkElement(driver, "text", "uw4@gmail.com");
		PO_View.checkElement(driver, "text", "uw5@gmail.com");
	}
	
	// Ir a la lista de usuarios, borrar el último usuario de la lista, comprobar que la lista se actualiza	y que el usuario desaparece.
	@Test
	public void PR14() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
		PO_HomeView.clickOption(driver, "user/list", "class", "btn btn-danger");
		PO_UsersView.deleteUser(driver, 7);
		SeleniumUtils.textoNoPresentePagina(driver, "uw5@gmail.com");
		PO_View.checkElement(driver, "text", "M4ras@gmail.com");
		PO_View.checkElement(driver, "text", "12_asMar@gmail.com");
		PO_View.checkElement(driver, "text", "uwu@gmail.com");
		PO_View.checkElement(driver, "text", "uw1@gmail.com");
		PO_View.checkElement(driver, "text", "uw2@gmail.com");
		PO_View.checkElement(driver, "text", "uw3@gmail.com");
		PO_View.checkElement(driver, "text", "uw4@gmail.com");
	}
	
	// Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la lista se actualiza y que los usuarios desaparecen.
	@Test
	public void PR15() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
		
		PO_HomeView.clickOption(driver, "user/list", "class", "btn btn-danger");
		
		int[] indexes = {3,4,5};
		
		PO_UsersView.deleteUsers(driver, indexes);
		SeleniumUtils.textoNoPresentePagina(driver, "uw1@gmail.com");
		SeleniumUtils.textoNoPresentePagina(driver, "uw2@gmail.com");
		SeleniumUtils.textoNoPresentePagina(driver, "uw3@gmail.com");
		PO_View.checkElement(driver, "text", "uw4@gmail.com");
		PO_View.checkElement(driver, "text", "M4ras@gmail.com");
		PO_View.checkElement(driver, "text", "12_asMar@gmail.com");

		PO_HomeView.clickOption(driver, "logout", "class", "btn-primary");
		PO_View.checkElement(driver, "text", "Iniciar sesión");
	}
	
	
	
	
	// Ir al formulario de alta de oferta, rellenarla con datos válidos y pulsar el botón Submit. Comprobar que la oferta sale en el listado de ofertas de dicho usuario.
	@Test
	public void PR16() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "uwu@gmail.com", "77777");
		PO_View.checkElement(driver, "text", "uwu@gmail.com");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'ofertas-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'oferta/add')]");
		elementos.get(0).click();
		PO_PrivateView.fillFormAddOferta(driver, "titulo1", "detalle1", "8");
		elementos = PO_View.checkElement(driver, "text", "titulo1");
		PO_HomeView.clickOption(driver, "logout", "class", "btn-primary");
		PO_View.checkElement(driver, "text", "Iniciar sesión");
	}
	
	// Ir al formulario de alta de oferta, rellenarla con datos inválidos (campo título vacío) y pulsar	el botón Submit. Comprobar que se muestra el mensaje de campo obligatorio
	@Test
	public void PR17() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "uwu@gmail.com", "77777");
		PO_View.checkElement(driver, "text", "uwu@gmail.com");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'ofertas-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'oferta/add')]");
		elementos.get(0).click();
		PO_PrivateView.fillFormAddOferta(driver, "", "detalle1", "8");
		PO_View.checkKey(driver, "Error.empty", PO_Properties.getSPANISH());
		PO_HomeView.clickOption(driver, "logout", "class", "btn-primary");
		PO_View.checkElement(driver, "text", "Iniciar sesión");
	}
	
	// Mostrar el listado de ofertas para dicho usuario y comprobar que se muestran todas los que existen para este usuario. 
	@Test
	public void PR18() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "uwu@gmail.com", "77777");
		PO_View.checkElement(driver, "text", "uwu@gmail.com");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'ofertas-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'oferta/add')]");
		elementos.get(0).click();
		PO_PrivateView.fillFormAddOferta(driver, "titulo2", "detalle2", "8");
		elementos = PO_View.checkElement(driver, "text", "titulo1");
		elementos = PO_View.checkElement(driver, "text", "titulo2");
		PO_HomeView.clickOption(driver, "logout", "class", "btn-primary");
		PO_View.checkElement(driver, "text", "Iniciar sesión");
	}
	
	// Ir a la lista de ofertas, borrar la primera oferta de la lista, comprobar que la lista se actualiza y que la oferta desaparece.
	@Test
	public void PR19() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "uwu@gmail.com", "77777");
		PO_View.checkElement(driver, "text", "uwu@gmail.com");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'ofertas-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'oferta/list')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'titulo1')]/following-sibling::*/a[contains(@href, 'oferta/delete')]");
		elementos.get(0).click();
		SeleniumUtils.EsperaCargaPaginaNoTexto(driver, "titulo1", PO_View.getTimeout());
		PO_HomeView.clickOption(driver, "logout", "class", "btn-primary");
		PO_View.checkElement(driver, "text", "Iniciar sesión");
	}
	
	// Ir a la lista de ofertas, borrar la última oferta de la lista, comprobar que la lista se actualiza y que la oferta desaparece.
	@Test
	public void PR20() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "uwu@gmail.com", "77777");
		PO_View.checkElement(driver, "text", "uwu@gmail.com");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'ofertas-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'oferta/list')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), 'titulo2')]/following-sibling::*/a[contains(@href, 'oferta/delete')]");
		elementos.get(0).click();
		SeleniumUtils.EsperaCargaPaginaNoTexto(driver, "titulo2", PO_View.getTimeout());
		PO_HomeView.clickOption(driver, "logout", "class", "btn-primary");
		PO_View.checkElement(driver, "text", "Iniciar sesión");
	}
	
	// Hacer una búsqueda con el campo vacío y comprobar que se muestra la página que corresponde con el listado de las ofertas existentes en el sistema
	@Test
	public void PR21() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "uwu@gmail.com", "77777");
		PO_View.checkElement(driver, "text", "uwu@gmail.com");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'ofertas-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'oferta/search')]");
		elementos.get(0).click();
		PO_SearchOferta.searchOferta(driver, "");
		PO_View.checkElement(driver, "text", "Deus Ex");
		PO_HomeView.clickOption(driver, "logout", "class", "btn-primary");
		PO_View.checkElement(driver, "text", "Iniciar sesión");
	}
	
	// Hacer una búsqueda escribiendo en el campo un texto que no exista y comprobar que se	muestra la página que corresponde, con la lista de ofertas vacía
	@Test
	public void PR22() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "uwu@gmail.com", "77777");
		PO_View.checkElement(driver, "text", "uwu@gmail.com");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'ofertas-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'oferta/search')]");
		elementos.get(0).click();
		PO_SearchOferta.searchOferta(driver, "asdfasdf");
		SeleniumUtils.EsperaCargaPaginaNoTexto(driver, "Deus Ex", PO_View.getTimeout());
		PO_HomeView.clickOption(driver, "logout", "class", "btn-primary");
		PO_View.checkElement(driver, "text", "Iniciar sesión");
	}
	
	// Sobre una búsqueda determinada (a elección del desarrollador), comprar una oferta que deja un saldo positivo en el contador del comprador.
	// Comprobar que el contador se actualiza correctamente en la vista del comprador.
	@Test
	public void PR23() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "uwu@gmail.com", "77777");
		PO_View.checkElement(driver, "text", "uwu@gmail.com");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'ofertas-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'oferta/search')]");
		elementos.get(0).click();
		PO_SearchOferta.searchOferta(driver, "Deus Ex");
		PO_View.checkElement(driver, "text", "Deus Ex");
		PO_SearchOferta.buyOferta(driver);
		PO_View.checkElement(driver, "text", "Vendida");
		PO_View.checkElement(driver, "text", "88.23");
		PO_HomeView.clickOption(driver, "logout", "class", "btn-primary");
		PO_View.checkElement(driver, "text", "Iniciar sesión");
	}
	
	// Sobre una búsqueda determinada (a elección del desarrollador), comprar una oferta que deja un saldo 0 en el contador del comprador.
	// Comprobar que el contador se actualiza correctamente en la vista del comprador. 
	@Test
	public void PR24() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "uwu@gmail.com", "77777");
		PO_View.checkElement(driver, "text", "uwu@gmail.com");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'ofertas-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'oferta/search')]");
		elementos.get(0).click();
		PO_SearchOferta.searchOferta(driver, "Dark Souls");
		PO_View.checkElement(driver, "text", "Dark Souls");
		PO_SearchOferta.buyOferta(driver);
		PO_View.checkElement(driver, "text", "Vendida");
		PO_View.checkElement(driver, "text", "0.0");
		PO_HomeView.clickOption(driver, "logout", "class", "btn-primary");
		PO_View.checkElement(driver, "text", "Iniciar sesión");
	}
	
	// Sobre una búsqueda determinada (a elección del desarrollador), intentar comprar una oferta que esté por encima de saldo disponible del comprador.
	// Y comprobar que se muestra el mensaje de	saldo no suficiente.
	@Test
	public void PR25() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "uwu@gmail.com", "77777");
		PO_View.checkElement(driver, "text", "uwu@gmail.com");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'ofertas-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'oferta/search')]");
		elementos.get(0).click();
		PO_SearchOferta.searchOferta(driver, "MGS3");
		PO_View.checkElement(driver, "text", "MGS3");
		PO_SearchOferta.buyOferta(driver);
		PO_View.checkKey(driver, "noMoney.message", PO_Properties.getSPANISH());
		PO_HomeView.clickOption(driver, "logout", "class", "btn-primary");
		PO_View.checkElement(driver, "text", "Iniciar sesión");
	}
	
	// Ir a la opción de ofertas compradas del usuario y mostrar la lista. Comprobar que aparecen las ofertas que deben aparecer.
	@Test
	public void PR26() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "uwu@gmail.com", "77777");
		PO_View.checkElement(driver, "text", "uwu@gmail.com");
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'ofertas-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'oferta/listPurchased')]");
		elementos.get(0).click();
		PO_View.checkElement(driver, "text", "Deus Ex");
		PO_View.checkElement(driver, "text", "Dark Souls");
		PO_HomeView.clickOption(driver, "logout", "class", "btn-primary");
		PO_View.checkElement(driver, "text", "Iniciar sesión");
		
	}
	
	// Visualizar al menos cuatro páginas haciendo el cambio español/inglés/español	(comprobando que algunas de las etiquetas cambian al idioma correspondiente).
	// Página principal/Opciones principales de usuario/Listado de usuarios /Vista de alta de oferta.
	@Test
	public void PR27() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "admin@email.com", "admin");
		PO_View.checkElement(driver, "text", "admin@email.com");
		PO_View.checkKey(driver,"registeredAs.message", PO_Properties.getSPANISH());
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'btnLanguage')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@id, 'btnEnglish')]");
		elementos.get(0).click();
		PO_View.checkKey(driver,"registeredAs.message", PO_Properties.getENGLISH());
		elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'btnLanguage')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@id, 'btnSpanish')]");
		elementos.get(0).click();
		PO_View.checkKey(driver,"logout.message", PO_Properties.getSPANISH());
		elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'btnLanguage')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@id, 'btnEnglish')]");
		elementos.get(0).click();
		PO_View.checkKey(driver,"logout.message", PO_Properties.getENGLISH());
		elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'btnLanguage')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@id, 'btnSpanish')]");
		elementos.get(0).click();
		PO_HomeView.clickOption(driver, "user/list", "class", "btn btn-danger");
		PO_View.checkKey(driver,"usersListText.message", PO_Properties.getSPANISH());
		elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'btnLanguage')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@id, 'btnEnglish')]");
		elementos.get(0).click();
		PO_View.checkKey(driver,"usersListText.message", PO_Properties.getENGLISH());
		elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'btnLanguage')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@id, 'btnSpanish')]");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'ofertas-menu')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'oferta/add')]");
		elementos.get(0).click();
		PO_View.checkKey(driver,"amount.message", PO_Properties.getSPANISH());
		elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'btnLanguage')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@id, 'btnEnglish')]");
		elementos.get(0).click();
		PO_View.checkKey(driver,"amount.message", PO_Properties.getENGLISH());
		elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'btnLanguage')]/a");
		elementos.get(0).click();
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@id, 'btnSpanish')]");
		elementos.get(0).click();
		PO_HomeView.clickOption(driver, "logout", "class", "btn-primary");
		PO_View.checkElement(driver, "text", "Iniciar sesión");
	}
	
	// Intentar acceder sin estar autenticado a la opción de listado de usuarios del administrador. Se deberá volver al formulario de login.
	@Test
	public void PR28() {
		driver.navigate().to("http://localhost:8080/user/list");
		PO_View.checkElement(driver, "text", "Iniciar sesión");
		PO_View.checkElement(driver, "text", "Email");
		PO_View.checkElement(driver, "text", "Password");
	}
	
	// Intentar acceder sin estar autenticado a la opción de listado de ofertas propias de un usuario estándar. Se deberá volver al formulario de login
	@Test
	public void PR29() {
		driver.navigate().to("http://localhost:8080/oferta/list");
		PO_View.checkElement(driver, "text", "Iniciar sesión");
		PO_View.checkElement(driver, "text", "Email");
		PO_View.checkElement(driver, "text", "Password");
	}
	
	// Estando autenticado como usuario estándar intentar acceder a la opción de listado de	usuarios del administrador. Se deberá indicar un mensaje de acción prohibida.
	@Test
	public void PR30() {
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		PO_LoginView.fillForm(driver, "uwu@gmail.com", "77777");
		PO_View.checkElement(driver, "text", "uwu@gmail.com");
		driver.navigate().to("http://localhost:8080/user/list");
		SeleniumUtils.EsperaCargaPaginaNoTexto(driver, "Usuarios", PO_View.getTimeout());
		
	}

}
