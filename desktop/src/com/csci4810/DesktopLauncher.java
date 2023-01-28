package com.csci4810;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        int fps = 1;
        config.setIdleFPS(fps);
        config.setForegroundFPS(fps);
        config.setTitle("CSCI 4810");
        config.setWindowedMode(1920, 1080);
        new Lwjgl3Application(new Homework1());
    }
}
