package org.newdawn.spaceinvaders.item;

import org.newdawn.spaceinvaders.Game;
import org.newdawn.spaceinvaders.user.Inventory;

public abstract class Item {
    protected Inventory inventory;
    protected Game game;
    protected String name;
    protected int coins;

    public Item(Inventory inventory){
        this.inventory = inventory;
    }
    public int getPrice() {
        return coins;
    }

    public String getName() {
        return name;
    }
    public abstract void useItem(Game game);
}

