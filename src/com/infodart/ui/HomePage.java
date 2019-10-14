package com.infodart.ui;

import java.io.IOException;
import java.util.Optional;

import org.apache.log4j.Logger;

import com.infodart.constant.ApplicationProperties;
import com.infodart.constant.Constants;


import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HomePage extends Application {

	final static Logger logger = Logger.getLogger(HomePage.class);

	Button cannotFindSizebutton;
	Button findMoreColorButton;
	Button shopMensWearButton;
	Button shopWesternWearButton;
	Button shopIndianWearButton;
	Button shopBoysWearButton;
	Button shopGirlsWearButton;
	Button backButton;
	Button popupButton;
	Label homePagelabel;
	TextField employeeIdtextField;
	Pane root;

	public static String empId = "";
	public static String userAction = "";
	boolean submitPressed = true;

	@Override
	public void start(Stage homeStage) throws Exception {

		empId = null;
		userAction = null;
		popupButton = new Button();
		cannotFindSizebutton = new Button();
		findMoreColorButton = new Button();
		shopMensWearButton = new Button();
		shopWesternWearButton = new Button();
		shopIndianWearButton = new Button();
		shopBoysWearButton = new Button();
		shopGirlsWearButton = new Button();
		backButton = new Button();
		homePagelabel = new Label(Constants.HOMEPAGE_LEVEL);
		employeeIdtextField = new TextField();
		employeeIdtextField.setPromptText(Constants.SELECT_EMP_ID);
		employeeIdtextField.setPrefWidth(200);
		employeeIdtextField.setPrefHeight(40);
		employeeIdtextField.setLayoutX(1100);
		employeeIdtextField.setLayoutY(600);
		cannotFindSizebutton.setLayoutX(110);
		cannotFindSizebutton.setLayoutY(290);
		findMoreColorButton.setLayoutX(700);
		findMoreColorButton.setLayoutY(290);
		shopMensWearButton.setLayoutX(110);
		shopMensWearButton.setLayoutY(510);
		popupButton.setLayoutX(1250);
		popupButton.setLayoutY(650);
		shopWesternWearButton.setLayoutX(342);
		shopWesternWearButton.setLayoutY(510);
		shopIndianWearButton.setLayoutX(574);
		shopIndianWearButton.setLayoutY(510);
		shopBoysWearButton.setLayoutX(806);
		shopBoysWearButton.setLayoutY(510);
		shopGirlsWearButton.setLayoutX(1038);
		shopGirlsWearButton.setLayoutY(510);
		backButton.setLayoutX(10);
		backButton.setLayoutY(10);
		homePagelabel.setLayoutX(450);
		homePagelabel.setLayoutY(475);
		homePagelabel.setStyle("-fx-font-size: 20px;");
		homePagelabel.setTextFill(Color.web("#FFFAFF"));

		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		Image bgImage = new Image(ApplicationProperties.properties.getProperty("MAX_BACKGROUND_IMAGE"));
		ImageView bgImageView = new ImageView();
		bgImageView.setImage(bgImage);

		bgImageView.setFitHeight(screenBounds.getWidth());
		bgImageView.setFitWidth(screenBounds.getWidth());
		bgImageView.setPreserveRatio(true);
		bgImageView.setSmooth(true);
		bgImageView.setCache(true);

		Image cantFindSizeImage = new Image(ApplicationProperties.properties.getProperty("CANNOT_FIND_SIZE_BUTTON"));
		ImageView cantFindSizeImageView = new ImageView();
		cantFindSizeImageView.setFitHeight(160);
		cantFindSizeImageView.setImage(cantFindSizeImage);
		cantFindSizeImageView.setPreserveRatio(true);
		cantFindSizeImageView.setSmooth(true);
		cannotFindSizebutton.setGraphic(cantFindSizeImageView);

		Image findMoreColorImage = new Image(ApplicationProperties.properties.getProperty("FIND_MORE_COLOR"));
		ImageView findMoreColorImageView = new ImageView();
		findMoreColorImageView.setFitHeight(160);
		findMoreColorImageView.setImage(findMoreColorImage);
		findMoreColorImageView.setPreserveRatio(true);
		findMoreColorImageView.setSmooth(true);
		findMoreColorButton.setGraphic(findMoreColorImageView);

		cannotFindSizebutton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				userAction = ConstantsIfc.CANT_FIND_SIZE_ACTION;
				CanNotFindYourSize canNotFindYourSize = new CanNotFindYourSize();
				try {
					canNotFindYourSize.start(homeStage);
				} catch (Exception e) {
					logger.error(e);
				}
			}
		});

		findMoreColorButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				userAction = ConstantsIfc.FIND_MORE_COLORS_ACTION;
				CanNotFindYourSize canNotFindYourSize = new CanNotFindYourSize();
				try {
					canNotFindYourSize.start(homeStage);
				} catch (Exception e) {
					logger.error(e);
				}
			}
		});

		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				KioskConfiguration kioskConfiguration = new KioskConfiguration();
				try {
					kioskConfiguration.start(homeStage);
				} catch (Exception e) {
					logger.error(e);
				}
			}
		});

		Image backButtonImage = new Image(ApplicationProperties.properties.getProperty("BACK_BUTTON_IMAGE"));
		ImageView backButtonImageView = new ImageView();
		backButtonImageView.setImage(backButtonImage);
		backButtonImageView.setFitHeight(30);
		backButtonImageView.setPreserveRatio(true);
		backButtonImageView.setSmooth(true);
		backButtonImageView.setCache(true);
		backButton.setGraphic(backButtonImageView);

		Image popupButtonImage = new Image(ApplicationProperties.properties.getProperty("EMPLOYEE_IMAGE"));
		ImageView popupButtonImageView = new ImageView();
		popupButtonImageView.setImage(popupButtonImage);
		popupButtonImageView.setFitHeight(30);
		popupButtonImageView.setPreserveRatio(true);
		popupButtonImageView.setSmooth(true);
		popupButtonImageView.setCache(true);
		popupButton.setGraphic(popupButtonImageView);
		// popupButton.setBackground(Background.EMPTY);
		popupButton.setStyle("-fx-background-color: transparent");

		popupButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				showEmployeeDialog(root, false);
			}
		});
		root = new Pane();
		root.getChildren().add(bgImageView);
		root.getChildren().addAll(popupButton, cannotFindSizebutton, findMoreColorButton);

		Scene scene = new Scene(root);
		homeStage.setScene(scene);
		homeStage.setTitle(ConstantsIfc.APPLICATION_NAME);
		homeStage.getIcons().add(new Image(ApplicationProperties.properties.getProperty("APPLICATION_ICON")));
		// homeStage.initStyle(StageStyle.UNDECORATED);
		homeStage.setMaximized(true);
		homeStage.show();
	}

	public static void openOnScreenKeyBoard() {

		try {
			if (ApplicationProperties.properties.getProperty("ENABLE_ONSCREEN_KEYBOARD").equals("Y")) {
				Runtime.getRuntime().exec("cmd /c osk.exe");
			}

		} catch (Exception e) {
			logger.error(e);
		}
	}

	public static void closeOnScreenKeyBoard() {

		try {
			if (ApplicationProperties.properties.getProperty("ENABLE_ONSCREEN_KEYBOARD").equals("Y")) {
				
                     Runtime.getRuntime().exec("cmd /c taskkill /IM osk.exe");
			}
                 	
		} catch (Exception e2) {
			
			logger.error(e2);
		}
	}

	private void showEmployeeDialog(Pane root, boolean employeeErrorFlag) {

		submitPressed = true;

		ButtonType cancel = new ButtonType("CANCEL", ButtonBar.ButtonData.OK_DONE);
		ButtonType submit = new ButtonType("SUBMIT", ButtonBar.ButtonData.CANCEL_CLOSE);

		TextInputDialog dialog = new TextInputDialog("");

		dialog.setHeaderText("");

		if (employeeErrorFlag)
		{
			dialog.setContentText("**Please Enter Employee Id.");
		}
		else
		{
			dialog.setContentText("Employee Id");
		}

		dialog.getEditor().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {

				if (newValue.length() == 0 || newValue.equals("")) {
				} else {
					if (newValue.length() >= 8) {
						dialog.getEditor().setText(oldValue);
					} else {
						try {
							Integer.parseInt(newValue);
						} catch (NumberFormatException ex) {
							dialog.getEditor().setText(oldValue);
						}
					}
				}
			}
		});

		dialog.setTitle("");
		dialog.initStyle(StageStyle.UNDECORATED);
		//dialog.getDialogPane().setBackground(Background.EMPTY);
		dialog.getDialogPane().getButtonTypes().remove(0);
		dialog.getDialogPane().getButtonTypes().remove(0);
		dialog.getDialogPane().getButtonTypes().addAll(cancel);
		dialog.getDialogPane().getButtonTypes().addAll(submit);

		dialog.setResizable(true);
		dialog.getDialogPane().setPrefSize(400, 200);
		
		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        dialog.setX((bounds.getMaxX() / 2) - 200);
        dialog.setY((bounds.getMaxY() / 2) - 100);
        
		ImageView DIALOG_HEADER_ICON = new ImageView(ApplicationProperties.properties.getProperty("EMPLOYEE_IMAGE"));
		DIALOG_HEADER_ICON.setFitHeight(78);
		DIALOG_HEADER_ICON.setFitWidth(78);
		dialog.getDialogPane().setGraphic(DIALOG_HEADER_ICON);
		openOnScreenKeyBoard();
		root.setStyle("-fx-opacity: 0.5;");

		dialog.getEditor().setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				KeyCode kc = ke.getCode();
				if (kc == KeyCode.ENTER) {
					String text = dialog.getEditor().getText();
					if (text == null || text.equals("")) {
						showEmployeeDialog(root, true);
					} else {
						empId = text;
					}
				}
			}
		});

		Button yesButton = (Button) dialog.getDialogPane().lookupButton(cancel);
	    yesButton.setDefaultButton( false );
	    
	    Button noButton = (Button) dialog.getDialogPane().lookupButton(submit);
	    noButton.setDefaultButton( true );
	    
		Optional<String> result = dialog.showAndWait();

		result.ifPresent(name -> {

			if (name.isEmpty()) {
				submitPressed = false;
			} else {
				submitPressed = false;
			}
		});
		if (submitPressed) {
			String text = dialog.getEditor().getText();
			if (text == null || text.equals("")) {
				showEmployeeDialog(root, true);
			} else {
				empId = text;
			}
		}

		closeOnScreenKeyBoard();
		root.setStyle(null);
		logger.info("Employee Id : " + empId);
	}

}
