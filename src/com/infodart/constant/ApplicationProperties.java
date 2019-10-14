package com.infodart.constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.infodart.ui.MainUI;

public class ApplicationProperties {
	final static Logger logger = Logger.getLogger(ApplicationProperties.class);
	static ApplicationProperties applicationProperties = new ApplicationProperties();

	public static Properties properties;
	public static Properties propertiesForService;
	
	public static void load() 
	{
		propertiesForService = new Properties();
		properties = new Properties();
		try {
			InputStream fileStream = new FileInputStream(new File("./resources/print.properties"));
			InputStream fileStreamForService = new FileInputStream(new File("./resources/webservice.properties"));
			properties.load(fileStream);
			propertiesForService.load(fileStreamForService);
		} catch (IOException e) {
			logger.error(e);
		}
	}

}