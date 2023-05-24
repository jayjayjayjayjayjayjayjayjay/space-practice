package org.newdawn.spaceinvaders.frame;

import org.newdawn.spaceinvaders.entity.ShipEntity;
import org.newdawn.spaceinvaders.theme.CatTheme;
import org.newdawn.spaceinvaders.theme.DesertTheme;
import org.newdawn.spaceinvaders.theme.SpaceTheme;
import org.newdawn.spaceinvaders.theme.Theme;
import org.newdawn.spaceinvaders.user.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class ThemeFrame extends JFrame {
    private JButton[] skinSelectButton;

    private JButton backButton;
    private Player player;
    private ShipEntity playerShip;
    private Theme theme;

    public ThemeFrame(Player player) {
        super("Configuration");

        this.player = player;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // JFrame 닫히면 프로그램 종료
        setSize(800, 600);
        setLocationRelativeTo(null); // 창을 화면 중앙에 배치

        // get hold the content of the frame and set up the resolution of the game
        setContentPane(new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                Image backgroundImage = new ImageIcon(player.getTheme().getBackgroundImage()).getImage();
                g.drawImage(backgroundImage, 0, 0, 800, 600, this);
                repaint();
            }
        });

        setIgnoreRepaint(false);
        getContentPane().setLayout(null);

        skinSelectButton = new JButton[3];
        for (int i = 0; i < skinSelectButton.length; i++) {
            skinSelectButton[i] = createSkinSelectButton(i);
            skinSelectButton[i].setOpaque(true);
            skinSelectButton[i].setBackground(Color.BLACK);
            skinSelectButton[i].setForeground(Color.WHITE);
            skinSelectButton[i].setFocusPainted(false);
            skinSelectButton[i].setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 25));
            skinSelectButton[i].setBounds(100 + i * 220, 350, 150, 30);
            getContentPane().add(skinSelectButton[i]);
        }

        // 버튼 추가
        backButton = new JButton("Back");
        // 버튼 서식
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false); // 배경
        backButton.setBorderPainted(false); // 배경
        backButton.setForeground(Color.WHITE); // 글자색
        backButton.setFocusPainted(false); // 테두리
        backButton.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 20)); // 폰트
        backButton.setBounds(0, 500, 100, 20); // set position and size

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame mainFrame = new MainFrame(player);
                setVisible(false);
            }
        });
        getContentPane().add(backButton);

//        add(spaceThemeButton);
//        add(jungleThemeButton);
//        add(desertThemeButton);


        // finally make the window visible
//         pack();
//         setResizable(false);
        setVisible(true);

    }

    private JButton createSkinSelectButton(int index) {
        JButton button = new JButton("Select");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "해당 테마를 설정하겠습니까?", "테마 설정", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    player.setTheme(index);
                    repaint();
                }
            }
        });
        return button;
    }

}