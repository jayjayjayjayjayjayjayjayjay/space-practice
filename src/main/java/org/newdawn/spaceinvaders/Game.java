package org.newdawn.spaceinvaders;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;


import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.*;
import org.newdawn.spaceinvaders.dataBase.DB;
import org.newdawn.spaceinvaders.frame.LoginPage;
import org.newdawn.spaceinvaders.entity.*;
import org.newdawn.spaceinvaders.item.*;
import org.newdawn.spaceinvaders.item.AddBulletItem;
import org.newdawn.spaceinvaders.item.HealItem;
import org.newdawn.spaceinvaders.item.SpeedUpItem;
import org.newdawn.spaceinvaders.theme.*;
import org.newdawn.spaceinvaders.Skin.*;

import org.newdawn.spaceinvaders.user.Inventory;
import org.newdawn.spaceinvaders.user.Player;

public class Game extends Canvas
{
	private int timer;
	/** The stragey that allows us to use accelerate page flipping */
	private BufferStrategy strategy;
	/** True if the game is currently "running", i.e. the game loop is looping */
	private boolean gameRunning = true;
	/** The list of all the entities that exist in our game */
	private ArrayList entities = new ArrayList();
	/** The list of entities that need to be removed from the game this loop */
	private ArrayList removeList = new ArrayList();
	/** The entity representing the player */
	private ShipEntity ship;
	/** The speed at which the player's ship should move (pixels/sec) */
	private double moveSpeed = 300;
	/** The time at which last fired a shot */
	private long lastFire = 0;
	/** The interval between our players shot (ms) */
	private double firingInterval = 200;
	/** The number of aliens left on the screen */
	private int alienCount;

	/** The message to display which waiting for a key press */
	private String message = "";
	/** True if we're holding up game play until a key has been pressed */
	private boolean waitingForKeyPress = true;
	/** True if the left cursor key is currently pressed */
	private boolean leftPressed = false;
	/** True if the right cursor key is currently pressed */
	private boolean rightPressed = false;
	/** True if we are firing */
	private boolean firePressed = false;
	/** True if game logic needs to be applied this loop, normally as a result of a game event */
	private boolean logicRequiredThisLoop = false;
	/** The last time at which we recorded the frame rate */
	private long lastFpsTime;
	/** The current number of frames recorded */
	private int fps;
	/** The normal title of the game window */
	private String windowTitle = "Space Invaders 102";
	/** The game window that we'll update with the frame count */
	private JFrame container;
	private Image background;
	private Image CharacterSkin;
	private JButton backButton;
	private int currentLevel;
	private int level;
	/** item관련 변수 */
	private Player player;
	private Inventory inventory;
	private AddBulletItem addBulletItem;
	private int bulletCount = 1;
	private int spaceBetweenBullets = 30;
	private HealItem healItem;
	private SpeedUpItem speedUpItem;
	private ReLoadSpeedUpItem reLoadSpeedItem;
	private ShieldItem shieldItem;

	private DatabaseReference myRef;
	private boolean useAddBulletItemPressed = false;
	private boolean useHealItemPressed = false;
	private boolean useSpeedUpItemPressed = false;
	private boolean useShieldItemPressed = false;
	private boolean useReLoadSpeedItemPressed = false;
	private boolean qKeyPressed = false;
	private boolean wKeyPressed = false;
	private boolean eKeyPressed = false;
	private boolean rKeyPressed = false;
	private boolean tKeyPressed = false;
	private long lastAddBulletItemUse = 0;
	private long lastHealItemUse = 0;
	private long lastSpeedUpItemUse = 0;
	private long lastShieldItemUse = 0;
	private long lastReLoadSpeedItemUse = 0;

	private static final long ITEM_USE_INTERVAL = 3000;

	private DB db;
	private Theme theme;
	private Skin skin;



	/**
	 * Construct our game and set it running.
	 */
	public Game(JFrame frame, Player player) throws FirebaseAuthException {
		container = frame;
		this.player = player;
		this.theme = player.getTheme();
		this.skin = player.getSkin();
//
////		// create a frame to contain our game
////		container = new JFrame("Space Invaders 102");
////
////		// get hold the content of the frame and set up the resolution of the game
////		JPanel panel = (JPanel) container.getContentPane();
////		panel.setPreferredSize(new Dimension(800,600));
////		panel.setLayout(null);
//
		// setup our canvas size and put it into the content of the frame
		setBounds(0,0,800,600);
		container.getContentPane().add(this);
//
//		// Tell AWT not to bother repainting our canvas since we're
//		// going to do that our self in accelerated mode
//		setIgnoreRepaint(true);
//
//		// finally make the window visible
//		container.pack();
//		container.setResizable(false);
//		container.setVisible(true);

		// add a listener to respond to the user closing the window. If they
		// do we'd like to exit the game

		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});


		// add a key input system (defined below) to our canvas
		// so we can respond to key pressed
		addKeyListener(new KeyInputHandler());

		// request the focus so key events come to us
		requestFocus();

		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		inventory = player.getInventory();

		addBulletItem  = new AddBulletItem(inventory);
		healItem = new HealItem(inventory);
		speedUpItem = new SpeedUpItem(inventory);
		reLoadSpeedItem = new ReLoadSpeedUpItem(inventory);
		shieldItem = new ShieldItem(inventory);

		myRef = FirebaseDatabase.getInstance().getReference("users").child(LoginPage.getUserName());
		db = new DB();

		// initialise the entities in our game so there's something
		// to see at startup

		initEntities();
	}

	/**
	 * Start a fresh game, this should clear out any old data and
	 * create a new set.
	 */
	private void startGame() {
		// clear out any existing entities and intialise a new set
		entities.clear();
		initEntities();

		// blank out any keyboard settings we might currently have
		leftPressed = false;
		rightPressed = false;
		firePressed = false;
	}

	/**
	 * Initialise the starting state of the entities (ship and aliens). Each
	 * entitiy will be added to the overall list of entities in the game.
	 */
	int alienkill=0;
	// 민재형 이부분 캐릭터 사진 변경임
	private void initEntities() {
		// create the player ship and place it roughly in the center of the screen
		ship = new ShipEntity(this,this.player.getSkin().getShipImage(),370,500, this.player);
//		ImageIcon i = new ImageIcon("ship.png");
		entities.add(ship);

		// create a block of aliens (5 rows, by 12 aliens, spaced evenly)
		alienCount = 0;
		db.getPlayCount(count -> {
			if (count != 0 && count % 5 == 0) {
				setLevel(6);
			} else {
				setLevel(currentLevel);
			}
		});
		if (level == 1) {
			for (int row = 0; row < 4; row++) {
				for (int col = 0; col < 6; col++) {
					Entity alien = new AlienEntity(this, 100 + (col * 110), (50) + row * 40, this.player);
					entities.add(alien);
					alienCount++;
				}
			}
		} else if (level == 2) {
			for (int row = 0; row < 5; row++) {
				for (int col = 0; col < 8; col++) {
					Entity alien = new AlienEntity(this, 100 + (col * 80), (50) + row * 30, this.player);
					entities.add(alien);
					alienCount++;
				}
			}
		} else if (level == 6) {
			for (int n = 0; n < 15; n++) {
				Entity alien = new BossAlienEntity(this, 700, 50);
				entities.add(alien);
				alienCount++;
			}

		} else {
			for (int row = 0; row < 5; row++) {
				for (int col = 0; col < 12; col++) {
					Entity alien = new AlienEntity(this, 100 + (col * 50), (50) + row * 30, this.player);
					entities.add(alien);
					alienCount++;
				}
			}
		}

		if (player.getSelectedSkinId() == 1) {bulletCount=2; moveSpeed=100;firingInterval=700;}
		else if (player.getSelectedSkinId() ==2) {bulletCount=3; moveSpeed=150;firingInterval=2000;}
	}
	public ShipEntity getShip(){
		return ship;
	}
	private void useAddBulletItem() {
		if(inventory.getItemCount(addBulletItem.getName()) > 0) {
			addBulletItem.useItem(this);
			System.out.println("총알 증가!");
		}
	}

	private void useHealItem() {
		if(inventory.getItemCount(healItem.getName()) > 0) {
			healItem.useItem(this);
			System.out.println("체력 증가");
		}
	}

	private void useSpeedUpItem() {
		if(inventory.getItemCount(speedUpItem.getName()) > 0) {
			speedUpItem.useItem(this);
			System.out.println("속도 증가!");
		}
	}
	private void useShieldItem() {
		if(inventory.getItemCount(shieldItem.getName()) > 0) {
			shieldItem.useItem(this);
			System.out.println("실드 착용!");
		}
	}

	private void useReLoadSpeedItem() {
		if (inventory.getItemCount(reLoadSpeedItem.getName()) > 0) {
			reLoadSpeedItem.useItem(this);
			System.out.println("발사 속도 증가!");
		}
	}

	private boolean canUseItem(long lastItemUse) {
		return System.currentTimeMillis() - lastItemUse >= ITEM_USE_INTERVAL;
	}

	public ShipEntity getEntity(){
		return (ShipEntity) entities.get(0);
	}

	public void setSpeed(double moveSpeed){
		this.moveSpeed = moveSpeed;
	}
	public double getSpeed() {
		return moveSpeed;
	}
	public void setFireSpeed(double firingInterval){
		this.firingInterval = firingInterval;
	}
	public double getFireSpeed(){
		return firingInterval;
	}
	public void increaseBulletCount() {
		bulletCount++;
	}
	/**
	 * Notification from a game entity that the logic of the game
	 * should be run at the next opportunity (normally as a result of some
	 * game event)
	 */
	public void updateLogic() {
		logicRequiredThisLoop = true;
	}

	/**
	 * Remove an entity from the game. The entity removed will
	 * no longer move or be drawn.
	 *
	 * @param entity The entity that should be removed
	 */
	public void removeEntity(Entity entity) {
		removeList.add(entity);
	}

	/**
	 * Notification that the player has died.
	 */



	public void notifyDeath() {
		message = "Level "+level+", Score :"+ alienkill	;
		waitingForKeyPress = true;
		updatePlayInfo(timer, coinCount);
		coinCount = 0;
//		updateHighScore();
		alienkill=0;
		coinCount = 0;
	   //Rank.setScore((alienkill/(timer/1000)));
	}

	/**
	 * Notification that the player has won since all the aliens
	 * are dead.
	 */
	public void notifyWin() {
		message = "Level " + level + ", Score :" + alienkill;
		waitingForKeyPress = true;
		db.storeHighScore(alienkill);
		alienkill = 0;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		updatePlayInfo(timer, coinCount);
	}

	public void updatePlayInfo(int timer, int coin) {
		db.increasePlayCount();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		db.updatePlayTime(timer / 100);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		db.updateCoin(coin);
		coinCount = 0;
	}

//	public void increasePlayCount() {
//		myRef.runTransaction(new Transaction.Handler() {
//		@Override
//		public Transaction.Result doTransaction(MutableData mutableData) {
//			Integer playCount = mutableData.child("playCount").getValue(Integer.class);
//			if (playCount == null) {
//				mutableData.child("playCount").setValue(1);
//			} else {
//				mutableData.child("playCount").setValue(playCount + 1);
//			}
//			return Transaction.success(mutableData);
//		}
//
//		@Override
//		public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//			if (databaseError != null) {
//				System.out.println("Transaction failed.");
//			} else {
//				System.out.println("Transaction completed.");
//			}
//		}
//	});
//	}

	private int playtime;
	private int playCount;
	private int score = 0;
	private int highScore;

	// myframe에서 Playcount,highscore 접근을 위해 getter 메소드 사용
//	public int getPlayCount() {
//		playCount = db.getPlayCount();
//		return playCount;
//	}

//	public int getHighScore() {
//		db.getHighScore(score -> {
//			return score;
//		});
////		highScore = db.getHighScore();
////		return highScore;
//	}

	public int getScore() {
		return score;
	}


	/**
	 * Notification that an alien has been killed
	 */






	//코인
	private int coinCount = 0;
	public void increaseCoinCount() {
		coinCount++;
		System.out.println("Coin Count: " + coinCount); // 콘솔에 현재 코인 개수를 출력합니다.
	}



	public void notifyAlienKilled(Entity alienEntity) {
		// reduce the alien count, if there are none left, the player has won!
		alienCount--;

		alienkill ++;
		if (alienCount == 0) {
			notifyWin();
		}

		Random rand = new Random();
		int randomNum = rand.nextInt(100);

		if (randomNum < 50) { // 50%의 확률로 코인 생성
			CoinEntity coin = new CoinEntity(this, "sprites/coin.png", alienEntity.getX(), alienEntity.getY());
			entities.add(coin);
		}


		// if there are still some aliens left then they all need to get faster, so
		// speed up all the existing aliens
		for (int i=0;i<entities.size();i++) {
			Entity entity = (Entity) entities.get(i);

			if (entity instanceof AlienEntity) {
				// speed up by 2%
				entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);
			}
		}
	}


	/**
	 * Attempt to fire a shot from the player. Its called "try"
	 * since we must first check that the player can fire at this
	 * point, i.e. has he/she waited long enough between shots
	 */
	public void tryToFire() {
		// check that we have waiting long enough to fire
		if (System.currentTimeMillis() - lastFire < firingInterval) {
			return;
		}

		// if we waited long enough, create the shot entity, and record the time.
		lastFire = System.currentTimeMillis();

		for (int i = 0; i < bulletCount; i++) {
			int bulletX = ship.getX() + 10 - (bulletCount - 1) * spaceBetweenBullets / 2 + i * spaceBetweenBullets;
			ShotEntity shot = new ShotEntity(this, this.player.getSkin().getShipShotImage(), bulletX, ship.getY() - 30);
			entities.add(shot);
		}
	}

	/**
	 * The main game loop. This loop is running during all game
	 * play as is responsible for the following activities:
	 * <p>
	 * - Working out the speed of the game loop to update moves
	 * - Moving the game entities
	 * - Drawing the screen contents (entities, text)
	 * - Updating game events
	 * - Checking Input
	 * <p>
	 */String pathname;


	public void gameLoop() {
		long lastLoopTime = SystemTimer.getTime();

		new Sound("sound/bgm.wav");

		try {
			background = ImageIO.read(new File(this.theme.getBackgroundImage()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// keep looping round til the game ends
		while (gameRunning) {
			// work out how long its been since the last update, this
			// will be used to calculate how far the entities should
			// move this loop
			long delta = SystemTimer.getTime() - lastLoopTime;
			lastLoopTime = SystemTimer.getTime();

			// update the frame counter
			lastFpsTime += delta;
			fps++;
			timer ++;
			// update our FPS counter if a second has passed since
			// we last recorded
			if (lastFpsTime >= 1000) {
				container.setTitle(windowTitle+" (FPS: "+fps+")");
				lastFpsTime = 0;
				fps = 0;
			}

			// Get hold of a graphics context for the accelerated
			// surface and blank it out
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
//			g.setColor(Color.black);
//			g.fillRect(0,0,800,600);

			// 아이템 커맨드 입력
			if (useAddBulletItemPressed) {
				useAddBulletItem();
				useAddBulletItemPressed = false;
			}
			if (useHealItemPressed) {
				useHealItem();
				useHealItemPressed = false;
			}
			if (useSpeedUpItemPressed) {
				useSpeedUpItem();
				useSpeedUpItemPressed = false;
			}
			if (useShieldItemPressed) {
				useShieldItem();
				useShieldItemPressed = false;
			}
			if (useReLoadSpeedItemPressed) {
				useReLoadSpeedItem();
				useReLoadSpeedItemPressed = false;
			}

			// draw the background image
			if (background != null) {
				g.drawImage(background, 0, 0, null);
			}

			// cycle round asking each entity to move itself
			if (!waitingForKeyPress) {
				for (int i=0;i<entities.size();i++) {
					Entity entity = (Entity) entities.get(i);

					entity.move(delta);
				}
				if (level == 4) {
					if (timer % 50 == 0) {
						AddObstacle();
					}
				} else if (level == 5) {
					if (timer % 20 == 0) {
						AddObstacle();
					}
				} else if (level == 6) {
					if (timer % 20 == 0) {
						AddObstacle();
					}
				}
			}


			// cycle round drawing all the entities we have in the game
			for (int i=0;i<entities.size();i++) {
				Entity entity = (Entity) entities.get(i);

				entity.draw(g);
			}

			// brute force collisions, compare every entity against
			// every other entity. If any of them collide notify
			// both entities that the collision has occured
			for (int p=0;p<entities.size();p++) {
				for (int s=p+1;s<entities.size();s++) {
					Entity me = (Entity) entities.get(p);
					Entity him = (Entity) entities.get(s);

					if (me.collidesWith(him)) {
						me.collidedWith(him);
						him.collidedWith(me);
					}
				}
			}

			// remove any entity that has been marked for clear up
			entities.removeAll(removeList);
			removeList.clear();

			// if a game event has indicated that game logic should
			// be resolved, cycle round every entity requesting that
			// their personal logic should be considered.
			if (logicRequiredThisLoop) {
				for (int i=0;i<entities.size();i++) {
					Entity entity = (Entity) entities.get(i);
					entity.doLogic();
				}

				logicRequiredThisLoop = false;
			}

			// if we're waiting for an "any key" press then draw the
			// current message
			if (waitingForKeyPress) {
				g.setColor(Color.white);
				g.drawString(message,(800-g.getFontMetrics().stringWidth(message))/2,250);
				g.drawString("Press any key", (800 - g.getFontMetrics().stringWidth("Press any key")) / 2, 300);
				//타이머(스코어) 0 초기화
				timer =0;
			}
//			//타이머 표시
//			g.drawString("타이머 "+String.valueOf(timer/100),720,30);

			//죽인 에일리언 표시
			g.setColor(Color.white);
			g.drawString("죽인 에일리언"+String.valueOf(alienkill),30,30);
			g.drawString("HP: " + String.valueOf(ship.getHP()), 720, 30);

			// finally, we've completed drawing so clear up the graphics
			// and flip the buffer over
			g.dispose();
			strategy.show();

			// resolve the movement of the ship. First assume the ship
			// isn't moving. If either cursor key is pressed then
			// update the movement appropraitely
			ship.setHorizontalMovement(0);

			if ((leftPressed) && (!rightPressed)) {
				ship.setHorizontalMovement(-moveSpeed);
			} else if ((rightPressed) && (!leftPressed)) {
				ship.setHorizontalMovement(moveSpeed);
			}

			// if we're pressing fire, attempt to fire
			if (firePressed) {
				tryToFire();

			}

			// we want each frame to take 10 milliseconds, to do this
			// we've recorded when we started the frame. We add 10 milliseconds
			// to this and then factor in the current time to give
			// us our final value to wait for
			SystemTimer.sleep(lastLoopTime+10-SystemTimer.getTime());

			ship.updateShieldStatus();
		}
	}

	/**
	 * A class to handle keyboard input from the user. The class
	 * handles both dynamic input during game play, i.e. left/right
	 * and shoot, and more static type input (i.e. press any key to
	 * continue)
	 *
	 * This has been implemented as an inner class more through
	 * habbit then anything else. Its perfectly normal to implement
	 * this as seperate class if slight less convienient.
	 *
	 * @author Kevin Glass
	 */

	/** 장애물 */
	public void AddObstacle() {
		ObstacleEntity obstacle = new ObstacleEntity(this, this.theme.getObstacleImage(), (int) (Math.random() * 750), 10);
		entities.add(obstacle);
		if(level == 4) obstacle.setMoveSpeed(500);
		else if(level == 5) obstacle.setMoveSpeed(800);
		else if(level == 6) obstacle.setMoveSpeed(800);
	}

	/** 레벨 선택 */
	public void setLevel(int level){
		if (level != 6) {
			this.currentLevel = level;
		}
		this.level = level;
	}


	private class KeyInputHandler extends KeyAdapter {
		/**
		 * The number of key presses we've had while waiting for an "any key" press
		 */
		private int pressCount = 1;

		/**
		 * Notification from AWT that a key has been pressed. Note that
		 * a key being pressed is equal to being pushed down but *NOT*
		 * released. Thats where keyTyped() comes in.
		 *
		 * @param e The details of the key that was pressed
		 */
		public void keyPressed(KeyEvent e) {
			// if we're waiting for an "any key" typed then we don't
			// want to do anything with just a "press"
			if (waitingForKeyPress) {
				return;
			}


			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = true;
			}
			// 위아래 키 이벤트를 처리하는 코드를 추가합니다.
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				ship.setVerticalMovement(-moveSpeed);
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				ship.setVerticalMovement(moveSpeed);
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_Q && !qKeyPressed) {
				if (canUseItem(lastAddBulletItemUse)) {
					useAddBulletItemPressed = true;
					lastAddBulletItemUse = System.currentTimeMillis();
					qKeyPressed = true;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_W && !wKeyPressed) {
				if (canUseItem(lastHealItemUse)) {
					useHealItemPressed = true;
					lastHealItemUse = System.currentTimeMillis();
					wKeyPressed = true;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_E && !eKeyPressed) {
				if (canUseItem(lastSpeedUpItemUse)) {
					useSpeedUpItemPressed = true;
					lastSpeedUpItemUse = System.currentTimeMillis();
					eKeyPressed = true;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_R && !rKeyPressed) {
				if (canUseItem(lastShieldItemUse)) {
					useShieldItemPressed = true;
					lastShieldItemUse = System.currentTimeMillis();
					rKeyPressed = true;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_T && !tKeyPressed) {
				if (canUseItem(lastReLoadSpeedItemUse)) {
					useReLoadSpeedItemPressed = true;
					lastReLoadSpeedItemUse = System.currentTimeMillis();
					tKeyPressed = true;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				// 메인페이지로 돌아가
			}
		}

		/**
		 * Notification from AWT that a key has been released.
		 *
		 * @param e The details of the key that was released
		 */
		public void keyReleased(KeyEvent e) {
			// if we're waiting for an "any key" typed then we don't E
			// want to do anything with just a "released"
			if (waitingForKeyPress) {
				return;
			}

			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = false;
			}
			// 위아래 키 이벤트를 처리하는 코드를 추가합니다.
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				ship.setVerticalMovement(0);
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				ship.setVerticalMovement(0);
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = false;
				new Sound("sound/hitSound.wav");
			}
			if (e.getKeyCode() == KeyEvent.VK_Q) {
				if (e.getKeyCode() == KeyEvent.VK_Q) {
					qKeyPressed = false;
					useAddBulletItemPressed = false;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_W) {
				if (e.getKeyCode() == KeyEvent.VK_W) {
					wKeyPressed = false;
					useHealItemPressed = false;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_E) {
				if (e.getKeyCode() == KeyEvent.VK_E) {
					eKeyPressed = false;
					useSpeedUpItemPressed = false;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_R) {
				if (e.getKeyCode() == KeyEvent.VK_R) {
					rKeyPressed = false;
					useShieldItemPressed = false;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_T) {
				if (e.getKeyCode() == KeyEvent.VK_T) {
					tKeyPressed = false;
					useReLoadSpeedItemPressed = false;
				}
			}

		}

		/**
		 * Notification from AWT that a key has been typed. Note that
		 * typing a key means to both press and then release it.
		 *
		 * @param e The details of the key that was typed.
		 */
		public void keyTyped(KeyEvent e) {
			// if we're waiting for a "any key" type then
			// check if we've recieved any recently. We may
			// have had a keyType() event from the user releasing
			// the shoot or move keys, hence the use of the "pressCount"
			// counter.
			if (waitingForKeyPress) {
				if (pressCount == 1) {
					// since we've now recieved our key typed
					// event we can mark it as such and start
					// our new game
					waitingForKeyPress = false;
					startGame();
					pressCount = 0;
				} else {
					pressCount++;
				}
			}

			// if we hit escape, then quit the game
			if (e.getKeyChar() == 27) {
				System.exit(0);
			}
		}
	}


	/**
	 * The entry point into the game. We'll simply create an
	 * instance of class which will start the display and game
	 * loop.
	 *
	 * @param argv The arguments that are passed into our game
	 */
}