package org.newdawn.spaceinvaders.theme;

import org.newdawn.spaceinvaders.user.Player;

public abstract class Theme {
    protected Player player;

    protected String backgroundImage = "Background.png";
    protected String alienEntityImage = "alien.png";
    protected String bossEntityImage = "bossAlien";
    protected String obstacleImage = "obstacle.png";
    protected String path1 = "src/main/resources/background/";
    protected String path2 = "background/";

    public abstract String getBackGroundImageFolderPath();
    public abstract String getEntityImageFolderPath();
    public String getBackgroundImage(){
        return getBackGroundImageFolderPath() + backgroundImage;
    }
    public String getAlienEntityImage(){ return getEntityImageFolderPath() + alienEntityImage; }
    public String getBossEntityImage(){
        return getBackGroundImageFolderPath() + bossEntityImage;
    }
    public String getObstacleImage(){
        return getEntityImageFolderPath() + obstacleImage;
    }
}
