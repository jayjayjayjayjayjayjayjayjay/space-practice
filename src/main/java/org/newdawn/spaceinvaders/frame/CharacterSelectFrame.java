package org.newdawn.spaceinvaders.frame;

import org.newdawn.spaceinvaders.entity.ShipEntity;
import org.newdawn.spaceinvaders.user.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class CharacterSelectFrame extends JFrame{

    final int buttonCount = 3; // 버튼 개수
    private JButton[] selectButton;
    private JLabel[] characterIcon; // 이미지를 표시할 JLabel 배열
    private int iconSize = 128;
    private int[] iconX = {100, 350, 600};
    private int iconY = 200;
    private int[] buttonX = {140, 390, 640};
    private int buttonY = 350;
    private String[] iconImage = {"src/main/resources/sprites/ship/space/", "src/main/resources/sprites/ship/cat/", "src/main/resources/sprites/ship/astronaut/"};
    private JButton backButton;
    private Player player;
    private ShipEntity playerShip;

    public CharacterSelectFrame(Player player, ShipEntity playerShip) {
        super("Character Select");

        this.player = player;
        this.playerShip = playerShip;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        setContentPane(new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                Image backgroundImage = new ImageIcon(player.getTheme().getBackgroundImage()).getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                repaint();
            }
        });

        setIgnoreRepaint(false);
        getContentPane().setLayout(null);

        characterIcon = new JLabel[buttonCount];
        for (int i = 0; i < buttonCount; i++) {
            characterIcon[i] = new JLabel();
            characterIcon[i].setBounds(iconX[i], iconY, iconSize, iconSize);
            try {
                ImageIcon imageIcon = new ImageIcon(ImageIO.read(new File(iconImage[i]+"ship.png")));
                Image image = imageIcon.getImage();
                Image scaledImage = image.getScaledInstance(iconSize, iconSize, java.awt.Image.SCALE_SMOOTH);  // 스케일링
                characterIcon[i].setIcon(new ImageIcon(scaledImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            getContentPane().add(characterIcon[i]);
            getContentPane().setComponentZOrder(characterIcon[i], 0); // 라벨을 버튼 위에 배치
        }



        // select 버튼
        selectButton = new JButton[buttonCount];
        for (int i = 0; i < buttonCount; i++) {
            selectButton[i] = createSkinSelectButton(i);
            selectButton[i].setOpaque(false);  // 버튼 투명하게 만들기
            selectButton[i].setContentAreaFilled(false);
            selectButton[i].setBorderPainted(false);
            selectButton[i].setBounds(buttonX[i], buttonY, 80, 30);
            getContentPane().add(selectButton[i]);
            getContentPane().setComponentZOrder(selectButton[i], 1); // 버튼을 라벨 아래에 배치
        }

        // back 버튼
        backButton = new JButton("Back");
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false); // 배경
        backButton.setBorderPainted(false); // 배경
        backButton.setForeground(Color.WHITE); // 글자색
        backButton.setFocusPainted(false); // 테두리
        backButton.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 20)); // 폰트
        backButton.setBounds(0, 500, 100, 20); // set position and size
        getContentPane().add(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame mainFrame = new MainFrame(player);
                setVisible(false);
            }
        });

        setVisible(true);
    }
    private JButton createSkinSelectButton(int index) {
        JButton button = new JButton("Select");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "해당 스킨을 설정하겠습니까?", "스킨 설정", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    player.setSkin(index);
                    repaint();
                }
            }
        });
        return button;
    }
}
