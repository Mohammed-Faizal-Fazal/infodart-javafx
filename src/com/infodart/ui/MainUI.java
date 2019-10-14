package com.infodart.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.SAXException;

import com.infodart.apiconsumer.APIConsumer;
import com.infodart.constant.ApplicationProperties;
import com.infodart.constant.Constants;
import com.infodart.model.StoreDetail;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainUI extends Application {

	final static Logger logger = Logger.getLogger(MainUI.class);

	Button lifeStyleButton;
	Button maxButton;
	Button splashButton;
	Button homeCenterButton;
	Label label;
	File file = new File("resources/print.properties");
	public static boolean flag = false;
	public static List<StoreDetail> storeList;

	@Override
	public void start(Stage stage) throws Exception {
		MainUI mainui = new MainUI();
		Properties properties = new Properties();
		InputStream fileStream = new FileInputStream(new File("./resources/print.properties"));
		properties.load(fileStream);
		if (properties.getProperty("MAXOU_TYPE_FLAG").equals("0")) {
			String[] args1 = { ApplicationProperties.propertiesForService.getProperty("HOST_AND_PORT") };
			InstallCert.main(args1);
			mainui.writeData(properties);
			selectstoreUi(stage);
		} else {
			HomePage homePage = new HomePage();
			homePage.start(stage);
		}
	}

	public void selectstoreUi(Stage stage) {

		lifeStyleButton = new Button();
		maxButton = new Button();
		splashButton = new Button();
		homeCenterButton = new Button();
		label = new Label(Constants.CHOOSE_OU);
		lifeStyleButton.setLayoutX(300);
		lifeStyleButton.setLayoutY(180);
		maxButton.setLayoutX(700);
		maxButton.setLayoutY(180);
		splashButton.setLayoutX(300);
		splashButton.setLayoutY(350);
		homeCenterButton.setLayoutX(700);
		homeCenterButton.setLayoutY(350);
		label.setLayoutX(520);
		label.setLayoutY(50);
		label.setStyle("-fx-font-size: 50px;");
		label.setTextFill(Color.web("#FFFAFF"));

		maxButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String ouCode = ApplicationProperties.properties.getProperty("MAX_OU_CODE");
				logger.info("OU Code: " + ouCode);
				try {
					logger.info(getOUclickRequest(ouCode));
					storeList = APIConsumer.apiConsumer(getOUclickRequest(ouCode), ConstantsIfc.OU_CLICK_MAX_ACTION);
					logger.info(storeList);
				} catch (IOException | ParserConfigurationException | SAXException e1) {
					logger.error(e1);
				}
				SelectStore selectStore = new SelectStore();
				try {
					selectStore.start(stage);
				} catch (Exception e) {
					logger.error(e);
					CommonUtil.displayErrorDialog(ConstantsIfc.WEB_SERVICE_ERROR);
				}
			}
		});

		Image lifeStyleImageLogo = new Image(ApplicationProperties.properties.getProperty("LIFESTYLE_LOGO"));
		ImageView lifeStyleLogoImageView = new ImageView();
		lifeStyleLogoImageView.setImage(lifeStyleImageLogo);
		lifeStyleLogoImageView.setFitHeight(134);
		lifeStyleLogoImageView.setFitWidth(376);
		lifeStyleButton.setGraphic(lifeStyleLogoImageView);

		Image maxLogoImage = new Image(ApplicationProperties.properties.getProperty("MAX_LOGO"));
		ImageView maxLogoImageView = new ImageView();
		maxLogoImageView.setImage(maxLogoImage);
		maxLogoImageView.setFitHeight(134);
		maxLogoImageView.setFitWidth(376);
		maxButton.setGraphic(maxLogoImageView);

		Image splashLogoImage = new Image(ApplicationProperties.properties.getProperty("SPLASH_LOGO"));
		ImageView splashLogoImageView = new ImageView();
		splashLogoImageView.setImage(splashLogoImage);
		splashLogoImageView.setFitHeight(134);
		splashLogoImageView.setFitWidth(376);
		splashButton.setGraphic(splashLogoImageView);

		Image homecenterLogoImage = new Image(ApplicationProperties.properties.getProperty("HOMECENTER_LOGO"));
		ImageView homecenterLogoImageView = new ImageView();
		homecenterLogoImageView.setImage(homecenterLogoImage);
		homecenterLogoImageView.setFitHeight(134);
		homecenterLogoImageView.setFitWidth(376);
		homeCenterButton.setGraphic(homecenterLogoImageView);

		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		Image bgImage = new Image(ApplicationProperties.properties.getProperty("MAINUI_BACKGROUND_IMAGE"));
		ImageView bgImageView = new ImageView();
		bgImageView.setImage(bgImage);
		bgImageView.setFitHeight(screenBounds.getWidth());
		bgImageView.setFitWidth(screenBounds.getWidth());
		bgImageView.setPreserveRatio(true);
		bgImageView.setSmooth(true);
		bgImageView.setCache(true);

		Pane root = new Pane();
		root.getChildren().add(bgImageView);
		root.getChildren().addAll(lifeStyleButton, maxButton, splashButton, homeCenterButton, label);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle(ConstantsIfc.APPLICATION_NAME);
		stage.getIcons().add(new Image(ApplicationProperties.properties.getProperty("APPLICATION_ICON")));
		stage.setMaximized(true);
		// stage.initStyle(StageStyle.UNDECORATED);
		stage.show();
	}

	public static void main(String[] args) throws Exception {

		PropertyConfigurator.configure("./resources/log4j.properties");
		logger.info("Smart Kiosk Starting.....");
		ApplicationProperties.load();
		launch(args);
	}

	void writeData(Properties p) throws IOException {
		FileOutputStream fos = null;
		fos = new FileOutputStream(file);
		p.store(fos, "Properties");
		fos.close();
		ApplicationProperties.load();
	}

	private String getOUclickRequest(String ouSelected) {

		String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.tcs.mobapps.webservice\">\r\n"
				+ "   <soapenv:Header/>\r\n" + "   <soapenv:Body>\r\n"
				+ "      <com:getAllStoresLocInfoBasedOnOuCode>\r\n" + "         <!--Optional:-->\r\n"
				+ "         <ouCode>" + ouSelected + "</ouCode>\r\n"
				+ "      </com:getAllStoresLocInfoBasedOnOuCode>\r\n" + "   </soapenv:Body>\r\n" + "</soapenv:E";
		return request;
	}

}
