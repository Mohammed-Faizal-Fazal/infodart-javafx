package com.infodart.ui;

import java.awt.AWTException;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

import org.apache.log4j.Logger;

public class CheckIdle extends Thread {

	final static Logger logger = Logger.getLogger(CheckIdle.class);

	private Robot robot;
	private double threshHold = 0.05;
	private int activeTime;
	private int idleTime;
	private boolean idle;
	private Rectangle screenDimenstions;

	public CheckIdle(int activeTime, int idleTime) {
		this.activeTime = activeTime;
		this.idleTime = idleTime;

		int screenWidth = 0;
		int screenHeight = 0;

		GraphicsEnvironment graphicsEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] graphicsDevices = graphicsEnv.getScreenDevices();

		for (GraphicsDevice screens : graphicsDevices) {
			DisplayMode mode = screens.getDisplayMode();
			screenWidth += mode.getWidth();

			if (mode.getHeight() > screenHeight) {
				screenHeight = mode.getHeight();
			}
		}

		screenDimenstions = new Rectangle(0, 0, screenWidth, screenHeight);

		robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e1) {
			logger.error(e1);
		}

		idle = false;
	}

	public void run() {
		while (true) {
			logger.info("Check Idle Run....");
			BufferedImage screenShot = robot.createScreenCapture(screenDimenstions);

			try {
				Thread.sleep(idle ? idleTime * 10000 : activeTime * 60000);
			} catch (InterruptedException e) {
				logger.error(e);
			}

			BufferedImage screenShot2 = robot.createScreenCapture(screenDimenstions);

			if (compareScreens(screenShot, screenShot2) < threshHold) {
				idle = true;
				logger.info("idle");
				CommonUtil.displayErrorDialog("APPLICATION IS IDLE.");
			} else {
				idle = false;
				logger.info("active");
			}
		}
	}

	private double compareScreens(BufferedImage screen1, BufferedImage screen2) {
		int counter = 0;
		boolean changed = false;

		for (int i = 0; i < screen1.getWidth() && !changed; i++) {
			for (int j = 0; j < screen1.getHeight(); j++) {
				if (screen1.getRGB(i, j) != screen2.getRGB(i, j)) {
					counter++;
				}
			}
		}

		return (double) counter / (double) (screen1.getHeight() * screen1.getWidth()) * 100;
	}
}