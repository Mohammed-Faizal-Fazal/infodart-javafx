package com.infodart.ui;

import java.nio.file.Paths;

import org.apache.log4j.Logger;

import com.infodart.apiconsumer.APIConsumer;
import com.infodart.constant.ApplicationProperties;
import com.infodart.constant.Constants;
import com.infodart.model.CantFindURL;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class CanNotFindYourSize extends Application {

	final static Logger logger = Logger.getLogger(CanNotFindYourSize.class);
	Button backButton;
	Button nextButton;
	TextField barcodeTextfield;
	Pane root;

	CantFindURL cannotfindurl = new CantFindURL();

	@Override
	public void start(Stage cannotFindStage) throws Exception {

		backButton = new Button();
		nextButton = new Button();
		barcodeTextfield = new TextField();
		barcodeTextfield.requestFocus();
		barcodeTextfield.setPromptText(Constants.BARCODE_PLACEHOLDER);
		barcodeTextfield.setPrefWidth(200);
		barcodeTextfield.setPrefHeight(40);
		backButton.setLayoutX(15);
		backButton.setLayoutY(10);
		nextButton.setLayoutX(1260);
		nextButton.setLayoutY(10);
		barcodeTextfield.setLayoutX(1050);
		barcodeTextfield.setLayoutY(10);
		barcodeTextfield.setFocusTraversable(true);

		barcodeTextfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {

				if (newValue.length() == 0 || newValue.equals("")) {
				} else {
					if (newValue.length() >= 16) {
						barcodeTextfield.setText(oldValue);
					} else {
						try {
							for (char c : newValue.toCharArray()) {
								Integer.parseInt("" + c);
							}
						} catch (NumberFormatException ex) {
							barcodeTextfield.setText(oldValue);
						}
					}
				}
			}
		});
		barcodeTextfield.setOnMouseClicked(e -> {
			HomePage.openOnScreenKeyBoard();
			//for openscreen keyboard
		});
		barcodeTextfield.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				KeyCode kc = ke.getCode();
				String barcode = barcodeTextfield.getText();

				if (barcode.length() == 0 && kc == KeyCode.ENTER) {
					root.setStyle("-fx-opacity: 0.5;");
					CommonUtil.displayErrorDialog(ConstantsIfc.ENTER_ITEM_ERROR);
					HomePage.closeOnScreenKeyBoard();
					root.setStyle(null);

				} else if (!barcode.isEmpty() && barcode != null) {

					if ((kc.equals(KeyCode.ENTER))) {
						if (barcode.length() == 0) {
							root.setStyle("-fx-opacity: 0.5;");
							CommonUtil.displayErrorDialog(ConstantsIfc.ENTER_ITEM_ERROR);
							HomePage.closeOnScreenKeyBoard();
							root.setStyle(null);
						} else {
							HomePage.closeOnScreenKeyBoard();
							try {
								cannotfindurl = APIConsumer.CantFindURLapiConsumer(getBarcodeRequestURL(barcode),
										HomePage.userAction);
							} catch (Exception e1) {
								logger.error(e1);
								root.setStyle("-fx-opacity: 0.5;");
								CommonUtil.displayErrorDialog(ConstantsIfc.WEB_SERVICE_ERROR);
								root.setStyle(null);
								barcodeTextfield.setText("");
								barcodeTextfield.requestFocus();
							}
							if (/* cannotfindurl != null && */ cannotfindurl.getValidateMessage().equals("Y")) {
								CannotFindYourSizeWebView cannotFindYourSizeWebView = new CannotFindYourSizeWebView(
										cannotfindurl.getCantFingurl());
								try {
									cannotFindYourSizeWebView.start(cannotFindStage);
									/*
									 * CheckIdle chkIdle = new
									 * CheckIdle(Integer.parseInt(ApplicationProperties.properties.getProperty(
									 * "APPLICATION_IDLE_TIME")), 1); chkIdle.start();
									 */
								} catch (Exception e) {
									logger.error(e);
								}
							}

							if (/* cannotfindurl != null || */cannotfindurl.getValidateMessage().equals("N")) {
								root.setStyle("-fx-opacity: 0.5;");
								CommonUtil.displayErrorDialog(cannotfindurl.getServerErrormsg());
								HomePage.closeOnScreenKeyBoard();
								barcodeTextfield.setText("");
								barcodeTextfield.requestFocus();
								root.setStyle(null);
							}
						}
					}
				}
			}
		});

		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				HomePage.closeOnScreenKeyBoard();
				HomePage homePage = new HomePage();
				try {
					homePage.start(cannotFindStage);
				} catch (Exception e) {
					logger.error(e);
				}
			}
		});

		nextButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String barcode = barcodeTextfield.getText();

				if (barcode.length() == 0) {
					root.setStyle("-fx-opacity: 0.5;");
					CommonUtil.displayErrorDialog(ConstantsIfc.ENTER_ITEM_ERROR);
					HomePage.closeOnScreenKeyBoard();
					root.setStyle(null);
					barcodeTextfield.requestFocus();
				} else {
					HomePage.closeOnScreenKeyBoard();
					try {
						cannotfindurl = APIConsumer.CantFindURLapiConsumer(getBarcodeRequestURL(barcode),
								HomePage.userAction);
						logger.info(cannotfindurl.getCantFingurl());
					} catch (Exception e1) {
						logger.error(e1);
						root.setStyle("-fx-opacity: 0.5;");
						CommonUtil.displayErrorDialog(ConstantsIfc.WEB_SERVICE_ERROR);
						root.setStyle(null);
						barcodeTextfield.setText("");
						barcodeTextfield.requestFocus();
					}
					if (cannotfindurl.getCantFingurl() != null) {
						if (cannotfindurl.getValidateMessage().equals("Y")) {
							CannotFindYourSizeWebView cannotFindYourSizeWebView = new CannotFindYourSizeWebView(
									cannotfindurl.getCantFingurl());
							try {
								cannotFindYourSizeWebView.start(cannotFindStage);
							} catch (Exception e) {
								logger.error(e);
							}
						}
					}

					if (cannotfindurl != null) {
						if (cannotfindurl.getValidateMessage().equals("N")) {
							root.setStyle("-fx-opacity: 0.5;");
							CommonUtil.displayErrorDialog(cannotfindurl.getServerErrormsg());
							root.setStyle(null);
							barcodeTextfield.setText("");
							barcodeTextfield.requestFocus();
						}
					}
				}
			}
		});

		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		Image homeImage = new Image(ApplicationProperties.properties.getProperty("HOME_ICON"));
		ImageView homeImageView = new ImageView();
		homeImageView.setImage(homeImage);
		homeImageView.setFitHeight(30);
		homeImageView.setPreserveRatio(true);
		homeImageView.setSmooth(true);
		homeImageView.setCache(true);
		backButton.setGraphic(homeImageView);

		Image nextButtonImage = new Image(ApplicationProperties.properties.getProperty("BARCODE_ICON"));
		ImageView nextButtonImageView = new ImageView();
		nextButtonImageView.setImage(nextButtonImage);
		nextButtonImageView.setFitHeight(30);
		nextButtonImageView.setPreserveRatio(true);
		nextButtonImageView.setSmooth(true);
		nextButtonImageView.setCache(true);
		nextButton.setGraphic(nextButtonImageView);

		Media media = new Media(
				Paths.get(ApplicationProperties.properties.getProperty("MAX_ANIMATION")).toUri().toString());
		MediaPlayer player = new MediaPlayer(media);
		MediaView view = new MediaView(player);
		view.setLayoutY(72);
		view.setFitHeight(screenBounds.getWidth());
		view.setFitWidth(screenBounds.getWidth());

		player.setAutoPlay(true);
		player.play();
		player.setCycleCount(3);

		root = new Pane();
		root.getChildren().addAll(backButton, nextButton, /* label, */barcodeTextfield, view);

		Scene scene = new Scene(root);
		barcodeTextfield.requestFocus();
		cannotFindStage.setScene(scene);
		cannotFindStage.setTitle(ConstantsIfc.APPLICATION_NAME);
		cannotFindStage.setMaximized(true);
		cannotFindStage.show();
	}

	public static String getBarcodeRequestURL(String barcode) {

		String scanbarcoderequest = null;
		String storeId = ApplicationProperties.properties.getProperty("STORECODE");
		if (HomePage.userAction != null && !HomePage.userAction.equals("")
				&& HomePage.userAction.equals(ConstantsIfc.CANT_FIND_SIZE_ACTION)) {
			scanbarcoderequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.tcs.mobapps.webservice\">\r\n"
					+ "   <soapenv:Header/>\r\n" + "   <soapenv:Body>\r\n" + "      <com:sendKioskUrl>\r\n"
					+ "         <!--Optional:-->\r\n" + "         <storeId>" + storeId + "</storeId>\r\n"
					+ "         <!--Optional:-->\r\n" + "         <itemId>" + barcode + "</itemId>\r\n"
					+ "         <!--Optional:-->\r\n" + "         <reqType>"
					+ ConstantsIfc.ECOM_MISSING_SIZE_REQUEST_TYPE + "</reqType>\r\n" + "         <!--Optional:-->\r\n"
					+ "         <reqFrom>" + ApplicationProperties.properties.getProperty("TABNO") + "</reqFrom>\r\n"
					+ "         <!--Optional:-->\r\n" + "         <entName>?</entName>\r\n"
					+ "         <!--Optional:-->\r\n" + "         <usrId>" + HomePage.empId + "</usrId>\r\n"
					+ "  </com:sendKioskUrl>\r\n" + "  </soapenv:Body>\r\n" + "</soapenv:Envelope>";

			return scanbarcoderequest;
		} else if (HomePage.userAction != null && !HomePage.userAction.equals("")
				&& HomePage.userAction.equals(ConstantsIfc.FIND_MORE_COLORS_ACTION)) {
			scanbarcoderequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:com=\"com.tcs.mobapps.webservice\">\r\n"
					+ "   <soapenv:Header/>\r\n" + "   <soapenv:Body>\r\n" + "      <com:sendKioskUrl>\r\n"
					+ "         <!--Optional:-->\r\n" + "         <storeId>" + storeId + "</storeId>\r\n"
					+ "         <!--Optional:-->\r\n" + "         <itemId>" + barcode + "</itemId>\r\n"
					+ "         <!--Optional:-->\r\n" + "         <reqType>" + ConstantsIfc.ECOM_MORE_COLOR_REQUEST_TYPE
					+ "</reqType>\r\n" + "         <!--Optional:-->\r\n" + "         <reqFrom>"
					+ ApplicationProperties.properties.getProperty("TABNO") + "</reqFrom>\r\n"
					+ "         <!--Optional:-->\r\n" + "         <entName>?</entName>\r\n"
					+ "         <!--Optional:-->\r\n" + "         <usrId>" + HomePage.empId + "</usrId>\r\n"
					+ "      </com:sendKioskUrl>\r\n" + "   </soapenv:Body>\r\n" + "</soapenv:Envelope>";
			return scanbarcoderequest;
		}
		return scanbarcoderequest;
	}
}
