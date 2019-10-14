package com.infodart.ui;

import java.util.Date;

import org.apache.log4j.Logger;

import com.infodart.apiconsumer.APIConsumer;
import com.infodart.constant.ApplicationProperties;
import com.infodart.constant.Constants;
import com.infodart.model.CantFindURL;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class CannotFindYourSizeWebView extends Application {
	final static Logger logger = Logger.getLogger(CannotFindYourSizeWebView.class);

	CantFindURL cannotfindurl2 = new CantFindURL();
	boolean runFl = true;
	Button nextButton;
	TextField barcodeTextfield;
	public static String cannotfindurl;
	int appIdleTime;
	VBox root;
	boolean isIdle = false;
	private Long lastInteractionTime;

	public CannotFindYourSizeWebView() {

	}

	public CannotFindYourSizeWebView(String cannotfindurl) {
		this.cannotfindurl = cannotfindurl;
		/*
		 * this.appIdleTime =
		 * Integer.parseInt(ApplicationProperties.properties.getProperty(
		 * "APPLICATION_IDLE_TIME"));
		 * logger.info("Application Idle time setting: "+appIdleTime);
		 */
	}

	@Override
	public void start(Stage CFYSwebStage) throws Exception {

		HomePage.closeOnScreenKeyBoard();
		resetLastInteractionTime();
		isIdle = false;

		

		buildUI(CFYSwebStage);
	}

	public void buildUI(Stage CFYSwebStage) throws Exception {
		nextButton = new Button();
		barcodeTextfield = new TextField();
		barcodeTextfield.requestFocus();
		barcodeTextfield.setPromptText(Constants.BARCODE_PLACEHOLDER);
		barcodeTextfield.setPrefHeight(40);
		barcodeTextfield.setMaxWidth(Double.min(170, 170));
		nextButton.setTranslateX(1260);
		nextButton.setTranslateY(30);
		barcodeTextfield.setTranslateX(1050);
		barcodeTextfield.setTranslateY(-10);
		barcodeTextfield.setFocusTraversable(false);

		Button bakButton = new Button();
		bakButton.setTranslateX(10);
		bakButton.setTranslateY(103);
		Button homeButton = new Button();
		homeButton.setTranslateX(15);
		homeButton.setTranslateY(65);

		barcodeTextfield.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {

				if (newValue.length() == 0 || newValue.equals("")) {
					barcodeTextfield.requestFocus();
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
					barcodeTextfield.requestFocus();
					resetLastInteractionTime();
				} else if (barcode != null && !barcode.isEmpty()) {
					if ((kc.equals(KeyCode.ENTER))) {

						if (barcode.length() == 0) {
							root.setStyle("-fx-opacity: 0.5;");
							CommonUtil.displayErrorDialog(ConstantsIfc.ENTER_ITEM_ERROR);
							HomePage.closeOnScreenKeyBoard();
							root.setStyle(null);
							barcodeTextfield.requestFocus();
							resetLastInteractionTime();
						} else {

							HomePage.closeOnScreenKeyBoard();

							try {
								cannotfindurl2 = APIConsumer.CantFindURLapiConsumer(CanNotFindYourSize
										.getBarcodeRequestURL(barcode)/* getBarcodeRequest(barcode) */,
										HomePage.userAction);
								logger.info("Web view URL: " + cannotfindurl2.getCantFingurl());
							} catch (Exception e1) {
								logger.error(e1);
								root.setStyle("-fx-opacity: 0.5;");
								CommonUtil.displayErrorDialog(ConstantsIfc.WEB_SERVICE_ERROR);
								root.setStyle(null);
								barcodeTextfield.setText("");
								barcodeTextfield.requestFocus();
								resetLastInteractionTime();
							}
							if (cannotfindurl2.getValidateMessage().equals("Y")) {
								CannotFindYourSizeWebView cannotFindYourSizeWebView = new CannotFindYourSizeWebView(
										cannotfindurl2.getCantFingurl());
								try {
									cannotFindYourSizeWebView.start(CFYSwebStage);
								} catch (Exception e) {
									logger.error(e);
								}
							}

							if (cannotfindurl2.getValidateMessage().equals("N")) {
								root.setStyle("-fx-opacity: 0.5;");
								CommonUtil.displayErrorDialog(cannotfindurl2.getServerErrormsg());
								HomePage.closeOnScreenKeyBoard();
								root.setStyle(null);
								barcodeTextfield.setText("");
								barcodeTextfield.requestFocus();
								resetLastInteractionTime();
							}
						}
					}
				}
			}
		});

		nextButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String barcode = barcodeTextfield.getText();

				resetLastInteractionTime();

				if (barcode.length() == 0) {
					root.setStyle("-fx-opacity: 0.5;");
					CommonUtil.displayErrorDialog(ConstantsIfc.ENTER_ITEM_ERROR);
					HomePage.closeOnScreenKeyBoard();
					root.setStyle(null);
					barcodeTextfield.requestFocus();
				} else {
					HomePage.closeOnScreenKeyBoard();
					try {
						cannotfindurl2 = APIConsumer.CantFindURLapiConsumer(
								CanNotFindYourSize.getBarcodeRequestURL(barcode)/* getBarcodeRequest(barcode) */,
								HomePage.userAction);
					} catch (Exception e1) {
						logger.error(e1);
						root.setStyle("-fx-opacity: 0.5;");
						CommonUtil.displayErrorDialog(ConstantsIfc.WEB_SERVICE_ERROR);
						root.setStyle(null);
						barcodeTextfield.setText("");
						barcodeTextfield.requestFocus();
					}
					if (cannotfindurl2 != null && cannotfindurl2.getValidateMessage().equals("Y")) {
						CannotFindYourSizeWebView cannotFindYourSizeWebView = new CannotFindYourSizeWebView(
								cannotfindurl2.getCantFingurl());
						try {
							cannotFindYourSizeWebView.start(CFYSwebStage);
						} catch (Exception e) {
							logger.error(e);
						}
					}
					if (cannotfindurl2 == null || cannotfindurl2.getValidateMessage().equals("N")) {
						root.setStyle("-fx-opacity: 0.5;");
						CommonUtil.displayErrorDialog(cannotfindurl2.getServerErrormsg());
						HomePage.closeOnScreenKeyBoard();
						root.setStyle(null);
						barcodeTextfield.setText("");
						barcodeTextfield.requestFocus();
					}
				}
			}
		});

		WebView webView = new WebView();
		webView.getEngine().load(cannotfindurl);
		webView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// barcodeTextfield.requestFocus();
				HomePage.closeOnScreenKeyBoard();
				HomePage.openOnScreenKeyBoard();
				resetLastInteractionTime();
			}
		});

		webView.setOnTouchPressed(new EventHandler<TouchEvent>() {

			@Override
			public void handle(TouchEvent event) {
				HomePage.closeOnScreenKeyBoard();
				HomePage.openOnScreenKeyBoard();
				// barcodeTextfield.requestFocus();

				resetLastInteractionTime();
			}
		});

		webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {

			if (Worker.State.SUCCEEDED.equals(newValue)) {
			
				startUserInactivityDetectThread(
						Integer.parseInt(ApplicationProperties.properties.getProperty("APPLICATION_IDLE_TIME")), CFYSwebStage);

			}
			barcodeTextfield.requestFocus();
			HomePage.closeOnScreenKeyBoard();

		});

		webView.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				HomePage.closeOnScreenKeyBoard();

				if (event.getCode() == KeyCode.ENTER) {
					// event.getCharacter()

				}

			}

		});

		bakButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				CanNotFindYourSize canNotFindYourSize = new CanNotFindYourSize();
				try {
					canNotFindYourSize.start(CFYSwebStage);
				} catch (Exception e) {
					logger.error(e);
				}
			}
		});

		homeButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				root.setStyle("-fx-opacity: 0.5;");
				CommonUtil.displayHomeErrorDialog(CFYSwebStage, barcodeTextfield);
				root.setStyle(null);
			}
		});

		Image backButtonImage = new Image(ApplicationProperties.properties.getProperty("BACK_BUTTON_IMAGE"));
		ImageView backButtonImageView = new ImageView();
		backButtonImageView.setImage(backButtonImage);
		backButtonImageView.setFitHeight(30);
		backButtonImageView.setPreserveRatio(true);
		backButtonImageView.setSmooth(true);
		backButtonImageView.setCache(true);
		bakButton.setGraphic(backButtonImageView);

		Image homeButtonImage = new Image(ApplicationProperties.properties.getProperty("HOME_ICON"));
		ImageView homeButtonImageView = new ImageView();
		homeButtonImageView.setImage(homeButtonImage);
		homeButtonImageView.setFitHeight(30);
		homeButtonImageView.setPreserveRatio(true);
		homeButtonImageView.setSmooth(true);
		homeButtonImageView.setCache(true);
		homeButton.setGraphic(homeButtonImageView);

		Image nextButtonImage = new Image(ApplicationProperties.properties.getProperty("BARCODE_ICON"));
		ImageView nextButtonImageView = new ImageView();
		nextButtonImageView.setImage(nextButtonImage);
		nextButtonImageView.setFitHeight(30);
		nextButtonImageView.setPreserveRatio(true);
		nextButtonImageView.setSmooth(true);
		nextButtonImageView.setCache(true);
		nextButton.setGraphic(nextButtonImageView);

		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		root = new VBox();
		root.setPadding(new Insets(0));
		root.setSpacing(0);
		root.setTranslateY(-50);

		root.getChildren().addAll(homeButton, nextButton, barcodeTextfield);
		root.getChildren().add(webView);

		Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());

		barcodeTextfield.requestFocus();
		CFYSwebStage.setTitle(ConstantsIfc.APPLICATION_NAME);
		CFYSwebStage.setMaximized(true);
		CFYSwebStage.setScene(scene);
		CFYSwebStage.show();
		HomePage.closeOnScreenKeyBoard();
	}

	public void startUserInactivityDetectThread(int idleTime, Stage CFYSwebStage) {
		resetLastInteractionTime();

		Service<Boolean> createFxService = new Service<Boolean>() {
			@Override
			protected Task<Boolean> createTask() {
				return new Task<Boolean>() {
					@Override
					protected Boolean call() throws Exception {
						logger.info("Configured Idle Time : " + idleTime);
						logger.info("Idle time in miliseconds : " + (idleTime * 60000));

						boolean runFlag = true;

						while (runFlag) {

							boolean flag = (getLastInteractionTime() >= (idleTime * 30000));
							logger.info("Flag : " + flag);
							if (flag) {
								isIdle = true;
								logger.info("System is idle.");
								// displayErrorDialog("APPLICATION IS IDLE.");
								runFlag = false;
							}
							try {
								if (!isIdle) {
									logger.info("Sleep for configured time.");
									Thread.sleep(idleTime * 30);
								} else {
									logger.info("Sleep for one minute.");
									Thread.sleep(30);
								}
							} catch (InterruptedException e) {
								logger.error(e);
							}
						}
						return runFlag;

					}
				};
			}
		};
		createFxService.setOnCancelled(w -> {

		});
		createFxService.setOnFailed(w -> {

		});

		createFxService.setOnSucceeded(w -> {
			Boolean value = createFxService.getValue();
			if (!value) {

				startPopupInactivity(1, CFYSwebStage);
				root.setStyle("-fx-opacity: 0.5;");

				CommonUtil.displayErrorDialog("APPLICATION IS IDLE.");
				root.setStyle(null);

	

			}

		});

		createFxService.start();

	}

	public void startPopupInactivity(int time, Stage CFYSwebStage) {

		resetLastInteractionTime();
		Service<Boolean> createFxService = new Service<Boolean>() {

			@Override
			protected Task<Boolean> createTask() {
				return new Task<Boolean>() {
					@Override
					protected Boolean call() throws Exception {
						logger.info("Configured Idle Time : " + time);
						logger.info("Idle time in miliseconds : " + (time * 60000));

						boolean runFlag = true;

						while (runFlag) {

							boolean flag = (getLastInteractionTime() >= (time * 30000));
							logger.info("Flag : " + flag);
							if (flag) {
								isIdle = true;
								logger.info("System is idle.");
								// displayErrorDialog("APPLICATION IS IDLE.");
								runFlag = false;
							}
							try {
								if (!isIdle) {
									logger.info("Sleep for configured time.");
									Thread.sleep(time * 30);
								} else {
									logger.info("Sleep for one minute.");
									Thread.sleep(30);
								}
							} catch (InterruptedException e) {
								logger.error(e);
							}
						}
						return runFlag;

					}
				};
			}
		};

		createFxService.setOnSucceeded(w -> {
			runFl = false;
			if (!runFl) {
				java.net.CookieHandler.setDefault(new java.net.CookieManager());
				HomePage homePage = new HomePage();
				try {
					homePage.start(CFYSwebStage);
				} catch (Exception e) {
					logger.error(e);
				}
			}

		});

		createFxService.start();

	}

	public long getLastInteractionTime() {

		Date d = new Date();
		Long time = d.getTime();

		return (time - lastInteractionTime);
	}

	public void setLastInteractionTime(Long lastInteractionTime) {
		this.lastInteractionTime = lastInteractionTime;
	}

	public void resetLastInteractionTime() {

		Date d = new Date();
		Long time = d.getTime();
		logger.info("Last Interaction Time : " + time);
		this.setLastInteractionTime(time);
	}
}
