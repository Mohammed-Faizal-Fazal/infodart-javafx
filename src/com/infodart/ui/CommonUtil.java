package com.infodart.ui;

import org.apache.log4j.Logger;

import com.infodart.constant.ApplicationProperties;

import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CommonUtil {
	
	final static Logger logger = Logger.getLogger(CommonUtil.class);

	public static void displayErrorDialog(String errorMsg) {
		ButtonType ok = new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE);

		Alert alert = new Alert(AlertType.ERROR, "", ok);

		alert.setHeaderText("");
		alert.initStyle(StageStyle.UNDECORATED);
		//alert.getDialogPane().setBackground(Background.EMPTY);

		ImageView DIALOG_HEADER_ICON = new ImageView(ApplicationProperties.properties.getProperty("HOME_ERROR_IMAGE"));
		DIALOG_HEADER_ICON.setFitHeight(78);
		DIALOG_HEADER_ICON.setFitWidth(78);
		alert.getDialogPane().setGraphic(DIALOG_HEADER_ICON);
		//alert.setContentText("\n" + errorMsg);
		
		Text txt = new Text("\n" + errorMsg);
		Font f = new Font(20);
		txt.setFont(f);
		
		alert.getDialogPane().setContent(txt);
		
		alert.setResizable(true);
		alert.getDialogPane().setPrefSize(400, 200);
		
		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

		alert.setX((bounds.getMaxX() / 2) - 200);
		alert.setY((bounds.getMaxY() / 2) - 100);
		
		alert.showAndWait().ifPresent(rs -> {
			if (rs == ButtonType.OK) {
				logger.info("OK Pressed.");
			}
		});
	}
	
	public static void displayHomeErrorDialog(Stage CFYSwebStage, TextField barcodeTextfield) {
		ButtonType contShop = new ButtonType("CONTINUE SHOPPING", ButtonBar.ButtonData.CANCEL_CLOSE);
		ButtonType home = new ButtonType("GO TO HOME PAGE", ButtonBar.ButtonData.OK_DONE);
		
		Alert alert = new Alert(AlertType.CONFIRMATION, "", home, contShop);
		// alert.setContentText("\nCHANGE WILL NOT BE SAVED.");
		alert.setHeaderText("");
		alert.initStyle(StageStyle.UNDECORATED);
		//alert.getDialogPane().setBackground(Background.EMPTY);

		ImageView DIALOG_HEADER_ICON = new ImageView(ApplicationProperties.properties.getProperty("HOME_ERROR_IMAGE"));
		DIALOG_HEADER_ICON.setFitHeight(78);
		DIALOG_HEADER_ICON.setFitWidth(78);
		alert.getDialogPane().setGraphic(DIALOG_HEADER_ICON);

		Text txt = new Text("\nCHANGE WILL NOT BE SAVED.");
		Font f = new Font(20);
		txt.setFont(f);

		alert.getDialogPane().setContent(txt);

		alert.setResizable(true);
		alert.getDialogPane().setPrefSize(400, 200);

		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

		alert.setX((bounds.getMaxX() / 2) - 200);
		alert.setY((bounds.getMaxY() / 2) - 100);

		Button yesButton = (Button) alert.getDialogPane().lookupButton(home);
	    yesButton.setDefaultButton( false );
	    
	    Button noButton = (Button) alert.getDialogPane().lookupButton(contShop);
	    noButton.setDefaultButton( true );

		alert.showAndWait().ifPresent(rs -> {
			if (rs == home) {
				HomePage.closeOnScreenKeyBoard();
				java.net.CookieHandler.setDefault(new java.net.CookieManager());
				HomePage homePage = new HomePage();
				try {
					homePage.start(CFYSwebStage);
				} catch (Exception e) {
					logger.error(e);
				}
			} else {
				barcodeTextfield.setText("");
				barcodeTextfield.requestFocus();
			}
		});
	}
	
}
