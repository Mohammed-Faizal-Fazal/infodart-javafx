package com.infodart.apiconsumer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.infodart.constant.ApplicationProperties;
import com.infodart.model.CantFindURL;
import com.infodart.model.StoreDetail;
import com.infodart.model.TabList;
import com.infodart.model.ValidateMacAddress;
import com.infodart.ui.ConstantsIfc;
import com.jayway.restassured.path.xml.XmlPath;

public class APIConsumer {

	final static Logger logger = Logger.getLogger(APIConsumer.class);

	public static List<StoreDetail> apiConsumer(String xmlinput, String maxclickmessage)
			throws ClientProtocolException, IOException, ParserConfigurationException, SAXException {
		List<StoreDetail> storeDetails = null;

		try {
			Document document = soapapiclient(xmlinput, maxclickmessage);

			NodeList nList = document.getElementsByTagName("districtList");
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node node = nList.item(temp);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;
					StoreDetail detail = null;
					NodeList childList = document.getElementsByTagName("storeList");
					storeDetails = new ArrayList<StoreDetail>();
					for (int t = 0; t < childList.getLength(); t++) {
						Node childNode = childList.item(t);
						if (childNode.getNodeType() == Node.ELEMENT_NODE) {
							Element echildElement = (Element) childNode;
							detail = new StoreDetail();
							detail.setStoreCode(echildElement.getElementsByTagName("storeId").item(0).getTextContent());
							detail.setStoreName(
									echildElement.getElementsByTagName("storeName").item(0).getTextContent());
						}
						storeDetails.add(detail);
					}
				}
			}

		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return storeDetails;
	}

	public static List<TabList> tabListapiConsumer(String xmlinput, String maxclickmessage)
			throws ClientProtocolException, IOException, ParserConfigurationException, SAXException {
		List<TabList> tabLists = new ArrayList<TabList>();
		Document document = soapapiclient(xmlinput, maxclickmessage);
		NodeList nList = document.getElementsByTagName("tabList");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node node = nList.item(temp);
			TabList tabList = null;
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				tabList = new TabList();
				tabList.setTabId(eElement.getElementsByTagName("tabId").item(0).getTextContent());
				tabList.setTabName(eElement.getElementsByTagName("tabName").item(0).getTextContent());
				tabList.setTabNo(eElement.getElementsByTagName("tabNo").item(0).getTextContent());
			}
			tabLists.add(tabList);
		}
		return tabLists;

	}

	public static CantFindURL CantFindURLapiConsumer(String request, String userAction) throws FileNotFoundException,
			ClientProtocolException, IOException, ParserConfigurationException, SAXException {
		CantFindURL cantFindURL = new CantFindURL();
		Document document = soapapiclient(request, userAction);
		if (document != null) {
			NodeList nList = document.getElementsByTagName("return");
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node node = nList.item(temp);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;
					cantFindURL.setValidateMessage(
							eElement.getElementsByTagName("validateMessage").item(0).getTextContent());
					if (cantFindURL.getValidateMessage().equals("Y"))
						cantFindURL
								.setCantFingurl(eElement.getElementsByTagName("itemLongUrl").item(0).getTextContent());
					if (cantFindURL.getValidateMessage().equals("N"))
						cantFindURL.setServerErrormsg(
								eElement.getElementsByTagName("serverErrormsg").item(0).getTextContent());
				}
			}
		}
		return cantFindURL;
	}

	/*
	 * public static FindMoreColor findmorecolorURLapiConsumer(String
	 * cantFindSizeReq, String cannotfind) throws FileNotFoundException,
	 * ClientProtocolException, IOException, ParserConfigurationException,
	 * SAXException { FindMoreColor findMoreColor = new FindMoreColor(); Document
	 * document = soapapiclient(cantFindSizeReq, cannotfind); NodeList nList =
	 * document.getElementsByTagName("return"); for (int temp = 0; temp <
	 * nList.getLength(); temp++) {
	 * 
	 * Node node = nList.item(temp); if (node.getNodeType() == Node.ELEMENT_NODE) {
	 * Element eElement = (Element) node; findMoreColor
	 * .setValidateMessage(eElement.getElementsByTagName("validateMessage").item(0).
	 * getTextContent()); if (findMoreColor.getValidateMessage().equals("Y"))
	 * findMoreColor
	 * .setMoreColorUrl(eElement.getElementsByTagName("itemLongUrl").item(0).
	 * getTextContent()); if (findMoreColor.getValidateMessage().equals("N"))
	 * findMoreColor.setServerErrormsg(
	 * eElement.getElementsByTagName("serverErrormsg").item(0).getTextContent()); }
	 * } return findMoreColor; }
	 */

	public static ValidateMacAddress validateMacAddressapiConsumer(String cantFindSizeReq, String cannotfind)
			throws FileNotFoundException, ClientProtocolException, IOException, ParserConfigurationException,
			SAXException {
		ValidateMacAddress validateMacAddress = new ValidateMacAddress();
		Document document = soapapiclient(cantFindSizeReq, cannotfind);
		NodeList nList = document.getElementsByTagName("return");
		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node node = nList.item(temp);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;

				validateMacAddress
						.setStatusMessage(eElement.getElementsByTagName("statusMessage").item(0).getTextContent());
				if (validateMacAddress.getStatusMessage().equals("Y")) {
					validateMacAddress.setTabId(eElement.getElementsByTagName("tabId").item(0).getTextContent());
					validateMacAddress.setTabName(eElement.getElementsByTagName("tabName").item(0).getTextContent());
					validateMacAddress.setTabNo(eElement.getElementsByTagName("tabNo").item(0).getTextContent());
				}
			}
		}
		return validateMacAddress;
	}

	public static ValidateMacAddress registerMacAddressapiConsumer(String macAddresstoRegister, String register)
			throws FileNotFoundException, ClientProtocolException, IOException, ParserConfigurationException,
			SAXException {
		ValidateMacAddress validateMacAddress = new ValidateMacAddress();
		Document document = soapapiclient(macAddresstoRegister, register);
		NodeList nList = document.getElementsByTagName("return");
		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node node = nList.item(temp);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;

				validateMacAddress
						.setStatusMessage(eElement.getElementsByTagName("statusMessage").item(0).getTextContent());
				if (validateMacAddress.getStatusMessage().equals("Y")) {
					validateMacAddress.setTabId(eElement.getElementsByTagName("tabId").item(0).getTextContent());
					validateMacAddress.setTabName(eElement.getElementsByTagName("tabName").item(0).getTextContent());
					validateMacAddress.setTabNo(eElement.getElementsByTagName("tabNo").item(0).getTextContent());
				}
			}
		}
		return validateMacAddress;
	}

	public static Document soapapiclient(String xmlinput, String userAction) throws IOException, FileNotFoundException,
			ClientProtocolException, ParserConfigurationException, SAXException {

		FileWriter requestOU = new FileWriter("inputparameterfile.xml");
		requestOU.write(xmlinput);
		requestOU.close();

		CloseableHttpClient client = HttpClients.createDefault();
		String soapurl = "";
		HttpPost request = null;
		if (userAction.equals(ConstantsIfc.OU_CLICK_MAX_ACTION)) {
			soapurl = ApplicationProperties.propertiesForService.getProperty("MAX_OU_WEB_SERVICE_URL");
		}
		if (userAction.equals(ConstantsIfc.KIOSK_LIST)) {
			soapurl = ApplicationProperties.propertiesForService.getProperty("MAX_KIOSK_LIST_WEB_SERVICE_URL");
		}
		if (userAction.equals(ConstantsIfc.FIND_MORE_COLORS_ACTION)
				|| userAction.equals(ConstantsIfc.CANT_FIND_SIZE_ACTION)) {
			soapurl = ApplicationProperties.propertiesForService.getProperty("MAX_BARCODE_SCAN_WEB_SERVICE_URL");
		}
		if (userAction.equals(ConstantsIfc.VALIDATE_MAC_ADDRESS_ACTION)) {
			soapurl = ApplicationProperties.propertiesForService
					.getProperty("MAX_VALIDATE_MAC_ADDRESS_WEB_SERVICE_URL");
		}
		if (userAction.equals(ConstantsIfc.REGISTER_MAC_ADDRESS_ACTION)) {
			soapurl = ApplicationProperties.propertiesForService
					.getProperty("MAX_REGISTER_MAC_ADDRESS_WEB_SERVICE_URL");
		}
		try {
			request = new HttpPost(soapurl);
			request.addHeader("Content-Type", "text/xml");
			request.setEntity(new InputStreamEntity(new FileInputStream("inputparameterfile.xml")));

			CloseableHttpResponse response = client.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			String responseString = EntityUtils.toString(response.getEntity());
			XmlPath path = new XmlPath(responseString);
			FileWriter fw = new FileWriter("test.xml");
			fw.write(responseString);
			fw.close();

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File("test.xml"));
			document.getDocumentElement().normalize();
			Element root = document.getDocumentElement();
			return document;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
	}
}
