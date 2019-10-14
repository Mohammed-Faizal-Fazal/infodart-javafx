package com.infodart.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.infodart.apiconsumer.APIConsumer;
import com.infodart.constant.ApplicationProperties;
import com.infodart.constant.Constants;
import com.infodart.model.TabList;
import com.infodart.model.ValidateMacAddress;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class KioskConfiguration extends Application {
	final static Logger logger = Logger.getLogger(KioskConfiguration.class);

	public static List<TabList> tabList;
	public static List<String> tabListtosetdata;
	public String listdata;
	public TabList list = null;
	public static String storeCode;
	Button backButton;
	Button nextButton;
	Label kioskLevel;
	ComboBox kioskComboBox;
	private File file;
	private KioskConfiguration kioskConfiguration;
	private Properties properties;
	private ValidateMacAddress validateMacAddress;
	private String macaddress = "";

	public KioskConfiguration() {

	}

	public KioskConfiguration(String storeCode) {
		macaddress = "";
		try {
			macaddress = getMacAddress();
		} catch (UnknownHostException | SocketException e1) {
			logger.error(e1);
		}

		try {
			validateMacAddress = APIConsumer.validateMacAddressapiConsumer(getValidateMacAddressRequest(),
					ConstantsIfc.VALIDATE_MAC_ADDRESS_ACTION);
		} catch (Exception e1) {
			logger.error(e1);
			CommonUtil.displayErrorDialog(ConstantsIfc.WEB_SERVICE_ERROR);
		}
		this.storeCode = storeCode;
		try {
			tabList = APIConsumer.tabListapiConsumer(getTestRequest(), ConstantsIfc.KIOSK_LIST);
			tabListtosetdata = new ArrayList<String>();
			for (TabList tabList2 : tabList) {
				listdata = new String();
				listdata = tabList2.getTabName();
				tabListtosetdata.add(listdata);
			}

		} catch (Exception e) {
			logger.error(e);
			CommonUtil.displayErrorDialog(ConstantsIfc.WEB_SERVICE_ERROR);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage kioskStage) throws Exception {

		backButton = new Button();
		nextButton = new Button();
		kioskLevel = new Label(Constants.KIOSK_LEVEL);
		backButton.setLayoutX(10);
		backButton.setLayoutY(10);
		nextButton.setLayoutX(720);
		nextButton.setLayoutY(350);
		kioskLevel.setLayoutX(460);
		kioskLevel.setLayoutY(300);
		kioskLevel.setStyle("-fx-font-size: 25px;");
		kioskLevel.setTextFill(Color.web("#FFFAFF"));

		kioskComboBox = new ComboBox(FXCollections.observableArrayList(tabListtosetdata));
		kioskComboBox.setLayoutX(690);
		kioskComboBox.setLayoutY(300);
		kioskComboBox.setPrefWidth(200);
		kioskComboBox.setPrefHeight(40);

		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				SelectStore selectStore = new SelectStore();
				try {
					selectStore.start(kioskStage);
				} catch (Exception e) {
					logger.error(e);
				}
			}
		});

		nextButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				kioskConfiguration = new KioskConfiguration();
				String kiosk = (String) kioskComboBox.getValue();
				if (kiosk != null) {
					properties = new Properties();
					try {
						properties.load(kioskConfiguration.getClass().getClassLoader()
								.getResourceAsStream("./resources/print.properties"));
					} catch (IOException e1) {
						logger.error(e1);
					}
					for (TabList tabList2 : tabList) {
						if (tabList2.getTabName().equals(kiosk)) {
							String tabId = tabList2.getTabId();
							String tabName = tabList2.getTabName();
							String tabNo = tabList2.getTabNo();
							file = new File("resources/print.properties");
							properties.setProperty("TABID", tabId);
							properties.setProperty("TABNAME", tabName);
							properties.setProperty("TABNO", tabNo);
							properties.setProperty("STORECODE", storeCode);
							properties.setProperty("MAXOU_TYPE_FLAG", "1");

							if (validateMacAddress.getStatusMessage().equals("N")) {
								String register = "RegisterMacAddress";
								try {
									ValidateMacAddress registermacAddress = APIConsumer.registerMacAddressapiConsumer(
											getRegisterMacAddressRequest(tabId, ConstantsIfc.KIOSK_STATUS), register);
								} catch (Exception e) {
									logger.error(e);
									CommonUtil.displayErrorDialog(ConstantsIfc.WEB_SERVICE_ERROR);
								}
							}
						}
					}
					try {
						writeData(properties);
					} catch (IOException e1) {
						logger.error(e1);
					}
					if (kiosk != null) {
						HomePage homePage = new HomePage();
						try {
							homePage.start(kioskStage);
						} catch (Exception e) {
							logger.error(e);
						}
					}
				} else {

					CommonUtil.displayErrorDialog(ConstantsIfc.SELECT_KIOSK_ERROR);
				}
			}
		});

		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		Image bgImage = new Image(ApplicationProperties.properties.getProperty("MAX_BACKGROUND_IMAGE"));
		ImageView bgImageView = new ImageView();
		bgImageView.setImage(bgImage);
		bgImageView.setFitHeight(screenBounds.getWidth());
		bgImageView.setFitWidth(screenBounds.getWidth());
		bgImageView.setPreserveRatio(true);
		bgImageView.setSmooth(true);
		bgImageView.setCache(true);

		Image backButtonImage = new Image(ApplicationProperties.properties.getProperty("BACK_BUTTON_IMAGE"));
		ImageView backButtonImageView = new ImageView();
		backButtonImageView.setImage(backButtonImage);
		backButtonImageView.setFitHeight(30);
		backButtonImageView.setPreserveRatio(true);
		backButtonImageView.setSmooth(true);
		backButtonImageView.setCache(true);
		backButton.setGraphic(backButtonImageView);

		Image nextButtonImage = new Image(ApplicationProperties.properties.getProperty("NEXT_BUTTON_IMG"));
		ImageView nextButtonImageView = new ImageView();
		nextButtonImageView.setImage(nextButtonImage);
		nextButtonImageView.setFitHeight(30);
		nextButtonImageView.setPreserveRatio(true);
		nextButtonImageView.setSmooth(true);
		nextButtonImageView.setCache(true);
		nextButton.setGraphic(nextButtonImageView);

		Pane root = new Pane();
		root.getChildren().add(bgImageView);
		root.getChildren().addAll(backButton, nextButton, kioskLevel, kioskComboBox);
		Scene scene = new Scene(root);
		kioskStage.setScene(scene);

		kioskStage.setTitle(ConstantsIfc.APPLICATION_NAME);
		kioskStage.getIcons().add(new Image(ApplicationProperties.properties.getProperty("APPLICATION_ICON")));
		// kioskStage.initStyle(StageStyle.UNDECORATED);
		kioskStage.setMaximized(true);
		kioskStage.show();
	}

	void writeData(Properties p) throws IOException {
		FileOutputStream fos = null;
		fos = new FileOutputStream(file);
		p.store(fos, "Properties");
		fos.close();
		ApplicationProperties.load();
	}

	public static String getMacAddress() throws UnknownHostException, SocketException {
		InetAddress ipAddress = InetAddress.getLocalHost();
		NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ipAddress);
		byte[] macAddressBytes = networkInterface.getHardwareAddress();
		StringBuilder macAddressBuilder = new StringBuilder();

		for (int macAddressByteIndex = 0; macAddressByteIndex < macAddressBytes.length; macAddressByteIndex++) {
			String macAddressHexByte = String.format("%02X", macAddressBytes[macAddressByteIndex]);
			macAddressBuilder.append(macAddressHexByte);

			if (macAddressByteIndex != macAddressBytes.length - 1) {
				macAddressBuilder.append(":");
			}
		}
		logger.info("Kiosk Mac address : " + macAddressBuilder.toString());
		return macAddressBuilder.toString();
	}

	private String getValidateMacAddressRequest() {

		String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.tcs.mobapps.webservice\">\r\n"
				+ "   <soapenv:Header/>\r\n" + "   <soapenv:Body>\r\n" + "      <com:validateTab>\r\n"
				+ "         <!--Optional:-->\r\n" + "         <storeId>" + storeCode + "</storeId>\r\n"
				+ "         <!--Optional:-->\r\n" + "         <macId>" + macaddress + "</macId>\r\n"
				+ "      </com:validateTab>\r\n" + "   </soapenv:Body>\r\n" + "</soapenv:Envelope>";
		return request;
	}

	private String getRegisterMacAddressRequest(String tabId, String status) {

		String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.tcs.mobapps.webservice\">\r\n"
				+ "   <soapenv:Header/>\r\n" + "   <soapenv:Body>\r\n" + "      <com:mapOrUpdateEComTab>\r\n"
				+ "         <!--Optional:-->\r\n" + "         <storeId>" + storeCode + "</storeId>\r\n"
				+ "         <!--Optional:-->\r\n" + "         <oldTabId>null</oldTabId>\r\n"
				+ "         <!--Optional:-->\r\n" + "         <newTabId>" + tabId + "</newTabId>\r\n"
				+ "         <!--Optional:-->\r\n" + "         <status>" + status + "</status>\r\n"
				+ "         <!--Optional:-->\r\n" + "         <usrId>" + storeCode + "</usrId>\r\n"
				+ "         <!--Optional:-->\r\n" + "         <macId>" + macaddress + "</macId>\r\n"
				+ "      </com:mapOrUpdateEComTab>\r\n" + "   </soapenv:Body>\r\n" + "</soapenv:Envelope>";
		return request;
	}

	private String getTestRequest() {
		String testRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.tcs.mobapps.webservice\">\r\n"
				+ "   <soapenv:Header/>\r\n" + "   <soapenv:Body>\r\n" + "      <com:getTabList>\r\n"
				+ "         <!--Optional:-->\r\n" + "         <storeId>" + storeCode + "</storeId>\r\n"
				+ "      </com:getTabList>\r\n" + "   </soapenv:Body>\r\n" + "</soapenv:Envelope>";

		return testRequest;
	}

}
