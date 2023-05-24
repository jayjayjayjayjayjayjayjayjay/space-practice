package org.newdawn.spaceinvaders.Skin;

import org.newdawn.spaceinvaders.user.Player;

public abstract class Skin {
    protected Player player;
    protected String shipImage = "ship.png";
    protected String shieldShipImage = "shieldShip.png";
    protected String shipShotImage = "shot.png";
    protected String path = "sprites/ship/";

    public abstract String getShipImageFolderPath();

    public String getShipImage() {return getShipImageFolderPath()+shipImage;}
    public String getShipShieldImage() {return getShipImageFolderPath()+shieldShipImage;}
    public String getShipShotImage() {return getShipImageFolderPath()+shipShotImage;}



}