package com.infodart.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;

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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class SelectStore extends Application {
	final static Logger logger = Logger.getLogger(SelectStore.class);

	Button backButton;
	Button nextButton;
	Label selectStoreLevel;
	TextField srlectStoretextField;

	@Override
	public void start(Stage storeStage) throws Exception {

		backButton = new Button();
		nextButton = new Button();
		selectStoreLevel = new Label(Constants.SELECT_STORE_LEVEL);
		backButton.setLayoutX(10);
		backButton.setLayoutY(10);
		nextButton.setLayoutX(700);
		nextButton.setLayoutY(350);
		selectStoreLevel.setLayoutX(500);
		selectStoreLevel.setLayoutY(300);
		selectStoreLevel.setStyle("-fx-font-size: 25px;");
		selectStoreLevel.setTextFill(Color.web("#FFFAFF"));
		srlectStoretextField = new TextField();
		srlectStoretextField.setPromptText(Constants.SELECT_STORE);
		srlectStoretextField.setPrefWidth(200);
		srlectStoretextField.setPrefHeight(40);
		srlectStoretextField.setLayoutX(670);
		srlectStoretextField.setLayoutY(300);

		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MainUI mainUI = new MainUI();
				try {
					mainUI.start(storeStage);
				} catch (Exception e) {
					logger.error(e);
				}
			}
		});

		srlectStoretextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				KeyCode kc = ke.getCode();
				String barcode = srlectStoretextField.getText();

				if (barcode.length() == 0 && kc == KeyCode.ENTER) {

					CommonUtil.displayErrorDialog(ConstantsIfc.SELECT_STORE_ERROR);
				} else if (kc == KeyCode.ENTER) {

					String store = srlectStoretextField.getText();
					String[] splittest = store.split(" ");
					StringBuffer sb = new StringBuffer();
					String test = null;
					int i = 0;
					for (String tt : splittest) {
						if (i == 0) {
							test = tt;
							i++;
						}
					}

					if (!store.isEmpty() && store != null) {
						KioskConfiguration kioskConfiguration = new KioskConfiguration("0" + test);
						try {
							kioskConfiguration.start(storeStage);
						} catch (Exception e) {
							logger.error(e);
						}
					}
				}
			}
		});

		nextButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String store = srlectStoretextField.getText();
				String[] splittest = store.split(" ");
				StringBuffer sb = new StringBuffer();
				String test = null;
				int i = 0;
				for (String tt : splittest) {
					if (i == 0) {
						test = tt;
						i++;
					}
				}

				if (!store.isEmpty() && store != null) {
					KioskConfiguration kioskConfiguration = new KioskConfiguration("0" + test);
					try {
						kioskConfiguration.start(storeStage);
					} catch (Exception e) {
						logger.error(e);
					}
				} else {

					CommonUtil.displayErrorDialog(ConstantsIfc.SELECT_STORE_ERROR);
				}
			}
		});

		List<StoreDetail> test = MainUI.storeList;
		List<String> storelist = new ArrayList<String>();
		for (StoreDetail storeDetail : test) {
			String storelistwithcode = storeDetail.getStoreCode() + " " + storeDetail.getStoreName();
			storelist.add(storelistwithcode);
		}

		TextFields.bindAutoCompletion(srlectStoretextField, storelist);

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
		root.getChildren().addAll(backButton, nextButton, selectStoreLevel, srlectStoretextField);
		Scene scene = new Scene(root);
		storeStage.setScene(scene);
		storeStage.setTitle(ConstantsIfc.APPLICATION_NAME);
		storeStage.getIcons().add(new Image(ApplicationProperties.properties.getProperty("APPLICATION_ICON")));
		// storeStage.initStyle(StageStyle.UNDECORATED);
		storeStage.setMaximized(true);
		storeStage.show();

	}

}
