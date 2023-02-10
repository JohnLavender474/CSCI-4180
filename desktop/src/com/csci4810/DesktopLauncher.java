package com.csci4810;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {

    public static final int FPS = 1;

    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setIdleFPS(FPS);
        config.setForegroundFPS(FPS);
        config.setTitle("CSCI 4810");
        config.setWindowedMode(1920, 1080);
        config.useVsync(false);
        new Lwjgl3Application(new Assignment1_2());
    }

}
