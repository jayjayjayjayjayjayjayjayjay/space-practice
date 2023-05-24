package org.newdawn.spaceinvaders;

import org.newdawn.spaceinvaders.dataBase.FirebaseAdminSDK;
import org.newdawn.spaceinvaders.frame.LoginPage;
import org.newdawn.spaceinvaders.frame.MainFrame;
import org.newdawn.spaceinvaders.user.Player;

import java.io.File;

public class Main {
    public static void main(String argv[]) {
        new FirebaseAdminSDK().initFirebase();
        LoginPage loginPage = new LoginPage();

        File f = new File("src/main/resources/sprites/ship/ship.png");
    }
}
