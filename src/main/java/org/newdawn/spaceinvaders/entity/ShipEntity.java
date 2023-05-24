package org.newdawn.spaceinvaders.entity;

import org.newdawn.spaceinvaders.Game;
import org.newdawn.spaceinvaders.Skin.Skin;
import org.newdawn.spaceinvaders.Sprite;
import org.newdawn.spaceinvaders.SpriteStore;
import org.newdawn.spaceinvaders.user.Player;

/**
 * The entity that represents the players ship
 * 
 * @author Kevin Glass
 */
public class ShipEntity extends Entity {
	/** The game in which the ship exists */
	private Game game;
	private int health = 3;
	private boolean shieldActive;
	private long shieldStartTime;
	private long SHIELD_DURATION = 5000;
	private String normalImage = "sprites/ship/ship.png";
	private String shieldedImage = "sprites/ship/shieldShip.png";
	private Player player;
	private Skin skin;


	public Skin getSkin() {
		return skin;
	}

	/**
	 * Create a new entity to represent the players ship
	 *  
	 * @param game The game in which the ship is being created
	 * @param ref The reference to the sprite to show for the ship
	 * @param x The initial x location of the player's ship
	 * @param y The initial y location of the player's ship
	 */
	public ShipEntity(Game game,String ref,int x,int y, Player player) {
		super(ref,x,y);
		this.game = game;
		this.player = player;
		this.shieldActive = false;

		if (player.getSelectedSkinId() == 1) {health=4;} else if (player.getSelectedSkinId() == 2) {health=5;}
	}

	//캐릭터의 이미지를 설정하는 메소드
	public void setShipImage(String imagePath) {
		this.normalImage = imagePath;
		setNormalImage();
	}


	/**
	 * Request that the ship move itself based on an elapsed ammount of
	 * time
	 * 
	 * @param delta The time that has elapsed since last move (ms)
	 */
	public void move(long delta) {
		// if we're moving left and have reached the left hand side
		// of the screen, don't move
		if ((dx < 0) && (x < 10)) {
			return;
		}
		// if we're moving right and have reached the right hand side
		// of the screen, don't move
		if ((dx > 0) && (x > 750)) {
			return;
		}
		
		super.move(delta);
	}
	
	/**
	 * Notification that the player's ship has collided with something
	 * 
	 * @param other The entity with which the ship has collided
	 */

	public void collidedWith(Entity other) {
		// if its an alien, notify the game that the player
		// is dead

		if (other instanceof AlienEntity || other instanceof ObstacleEntity) {
			if (!this.isShieldActive()) {
				if (health == 1)
					game.notifyDeath();
				else
					health--;
			}
		}
	}

	public int getHP(){ return health; }
	public void setHP(int health){
		this.health = health;
	}

	public void activateShield() {
		this.shieldActive = true;
		this.shieldStartTime = System.currentTimeMillis();
		setShieldedImage();
	}

	public void updateShieldStatus() {
		if (this.shieldActive && System.currentTimeMillis() - this.shieldStartTime > SHIELD_DURATION) {
			this.shieldActive = false;
			setNormalImage();
		}
	}

	public boolean isShieldActive() {
		return this.shieldActive;
	}
	public void setNormalImage() {
		try {
			Sprite sprite = SpriteStore.get().getSprite(player.getSkin().getShipImage());
			setSprite(sprite);
		} catch (Exception e) {
			System.out.println("Failed to load normal image: " + e.getMessage());
		}
	}

	public void setShieldedImage() {
		try {
			Sprite sprite = SpriteStore.get().getSprite(player.getSkin().getShipShieldImage());
			setSprite(sprite);
		} catch (Exception e) {
			System.out.println("Failed to load shielded image: " + e.getMessage());
		}
	}

}