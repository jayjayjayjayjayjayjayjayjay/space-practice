package org.newdawn.spaceinvaders.theme;

import org.newdawn.spaceinvaders.user.Player;

public class CatTheme extends Theme{

    @Override
    public String getBackGroundImageFolderPath() {
        return path1 + "Cat/";
    }
    @Override
    public String getEntityImageFolderPath() {
        return path2 + "Cat/";
    }
}
