package org.newdawn.spaceinvaders.theme;

public class SpaceTheme extends Theme{

    @Override
    public String getBackGroundImageFolderPath() {
        return path1 + "Space/";
    }

    @Override
    public String getEntityImageFolderPath() {
        return path2 + "Space/";
    }

    // 이제 각 프레임에 경로 설정해주기
}
