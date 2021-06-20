package com.uniovi.tests.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.uniovi.tests.util.SeleniumUtils;

public class PO_SearchOferta {
	
	static public void searchOferta(WebDriver driver, String titulop) {
		SeleniumUtils.esperarSegundos(driver, 5);
		WebElement titulo = driver.findElement(By.name("titleInputSearch"));
		titulo.clear();
		titulo.sendKeys(titulop);
		By boton = By.className("btn");
		driver.findElement(boton).click();
	}
	
	static public void buyOferta(WebDriver driver) {
		WebElement link = driver.findElement(By.name("buyOferta"));
		link.click();
	}

}
