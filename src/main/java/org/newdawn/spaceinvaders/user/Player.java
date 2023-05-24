package org.newdawn.spaceinvaders.user;

import com.google.firebase.auth.FirebaseAuthException;
import org.newdawn.spaceinvaders.Game;
import org.newdawn.spaceinvaders.Skin.AstronautSkin;
import org.newdawn.spaceinvaders.Skin.CatSkin;
import org.newdawn.spaceinvaders.Skin.SpaceSkin;
import org.newdawn.spaceinvaders.Skin.Skin;
import org.newdawn.spaceinvaders.dataBase.DB;
import org.newdawn.spaceinvaders.entity.ShipEntity;
import org.newdawn.spaceinvaders.theme.CatTheme;
import org.newdawn.spaceinvaders.theme.DesertTheme;
import org.newdawn.spaceinvaders.theme.SpaceTheme;
import org.newdawn.spaceinvaders.theme.Theme;

public class Player {
    private DB db;
    private int coins;
    private Inventory inventory;
    private Game game;
    private int characterId;
    private Theme theme;
    private Theme configTheme = new SpaceTheme();
    private ShipEntity playerShip;
    private Skin configSkin = new SpaceSkin();

    private int selectedSkinId; // 스킨 ID를 저장하기 위한 필드 추가

    public Player(){
        try {
            this.db = new DB();
            db.getCoin(coins -> {
                this.setCoins(coins);
            });
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }
        this.inventory = new Inventory();
        setCharacterId(this.characterId);
    }
    public ShipEntity getPlayerShip() {
        return this.playerShip;
    }

    public void setPlayerShip(ShipEntity playerShip) {
        this.playerShip = playerShip;
    }

    public void setGame(Game game){
        this.game = game;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void addItemToInventory(String item) {
        inventory.addItem(item);
    }

    public void setCharacterId(int characterId) { this.characterId = characterId; }
    public void setTheme(int themeId) {
        switch (themeId) {
            case 1:
                this.configTheme = new CatTheme();
                break;
            case 2:
                this.configTheme = new DesertTheme();
                break;
            default:
                this.configTheme = new SpaceTheme();
                break;
        }
    }

    public Theme getTheme() {
        return this.configTheme;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void setSkin(int skinId) {
        switch (skinId) {
            case 1:
                this.configSkin = new CatSkin();
                selectedSkinId =1;
                break;
            case 2:
                this.configSkin = new AstronautSkin();
                selectedSkinId =2;
                break;
            default:
                this.configSkin = new SpaceSkin();
                selectedSkinId =0;
                break;
        }
    }

    // 현재 선택된 스킨 ID를 반환하는 메소드 추가
    public int getSelectedSkinId() {
        return selectedSkinId;
    }
    public Skin getSkin() {return this.configSkin;}

}