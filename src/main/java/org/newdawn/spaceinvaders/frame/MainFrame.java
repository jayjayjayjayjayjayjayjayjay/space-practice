package org.newdawn.spaceinvaders.frame;

import com.google.firebase.auth.FirebaseAuthException;
import org.newdawn.spaceinvaders.entity.ShipEntity;
import org.newdawn.spaceinvaders.theme.*;
import org.newdawn.spaceinvaders.user.Player;
import org.newdawn.spaceinvaders.dataBase.Rank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class MainFrame extends JFrame{
    private JButton themeConfig;
    private JButton startButton;
    private JButton myPageButton;
    private JButton shopButton;
    private JButton RankButton;
    private Player player;
    private JButton CharacterSelectButton;
    private JButton gameintroduction;
    private Theme theme;



    public MainFrame(Player player) {
        super("Main Page");

        this.player = player;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // JFrame 닫히면 프로그램 종료
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null); // 창을 화면 중앙에 배치

        // get hold the content of the frame and set up the resolution of the game
        setContentPane(new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                Image backgroundImage = new ImageIcon(player.getTheme().getBackgroundImage()).getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                repaint();
            }
        });

        setIgnoreRepaint(false);

        getContentPane().setLayout(null);
        // 버튼 추가
        startButton = new JButton("Start");
        // 버튼 서식
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false); // 배경
        startButton.setBorderPainted(false); // 배경
        startButton.setForeground(Color.WHITE); // 글자색
        startButton.setFocusPainted(false); // 테두리
        startButton.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 20)); // 폰트
        startButton.setBounds(0, 300, 200, 50);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StageFrame stageFrame = new StageFrame(player);
                setVisible(false);
            }
        });

        myPageButton = new JButton("MyPage");
        // 버튼 서식
        myPageButton.setOpaque(false);
        myPageButton.setContentAreaFilled(false); // 배경
        myPageButton.setBorderPainted(false); // 배경
        myPageButton.setForeground(Color.WHITE); // 글자색
        myPageButton.setFocusPainted(false); // 테두리
        myPageButton.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 20)); // 폰트
        myPageButton.setBounds(200, 300, 200, 50);

        myPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 마이페이지 넘어가는 로직
                try {
                    MyPageFrame myPageFrame = new MyPageFrame(player);
                } catch (FirebaseAuthException ex) {
                    throw new RuntimeException(ex);
                }
                setVisible(false);
            }
        });

        shopButton = new JButton("Shop");
        // 버튼 서식
        shopButton.setOpaque(false);
        shopButton.setContentAreaFilled(false); // 배경
        shopButton.setBorderPainted(false); // 배경
        shopButton.setForeground(Color.WHITE); // 글자색
        shopButton.setFocusPainted(false); // 테두리
        shopButton.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 20)); // 폰트
        shopButton.setBounds(400, 300, 200, 50);

        shopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ShopFrame shop = new ShopFrame(player);
                } catch (FirebaseAuthException ex) {
                    throw new RuntimeException(ex);
                }
                setVisible(false);
            }
        });
        // Add a JPanel to contain the button
        JPanel buttonPanel = new JPanel();
        getContentPane().add(buttonPanel);

        // Create a button to open the image change panel
        gameintroduction = new JButton("게임 설명");
        // 버튼 서식
        gameintroduction.setOpaque(false);
        gameintroduction.setContentAreaFilled(false); // 배경
        gameintroduction.setBorderPainted(false); // 외곽선
        gameintroduction.setForeground(Color.WHITE); // 글자색
        gameintroduction.setFocusPainted(false); // 테두리
        buttonPanel.add(gameintroduction);
        // Add action listener to the button
        gameintroduction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameIntroductionDialog();

            }
        });


        gameintroduction.setBounds(600, 300, 200, 50);

        themeConfig = new JButton("Config");
        // 버튼 서식
        themeConfig.setOpaque(false);
        themeConfig.setContentAreaFilled(false); // 배경
        themeConfig.setBorderPainted(false); // 배경
        themeConfig.setForeground(Color.WHITE); // 글자색
        themeConfig.setFocusPainted(false); // 테두리
        themeConfig.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 20)); // 폰트 buttonPanel.add(themeConfig);
        // Add action listener to the button
        themeConfig.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ThemeFrame themeFrame = new ThemeFrame(player);
                setVisible(false);
            }
        });

        themeConfig.setBounds(300, 350, 200, 50);

        getContentPane().add(startButton);
        getContentPane().add(myPageButton);
        getContentPane().add(shopButton);
        getContentPane().add(gameintroduction);
        getContentPane().add(themeConfig);



        CharacterSelectButton = new JButton("Character");
        CharacterSelectButton.setOpaque(false);
        CharacterSelectButton.setContentAreaFilled(false); // 배경
        CharacterSelectButton.setBorderPainted(false); // 배경
        CharacterSelectButton.setForeground(Color.WHITE); // 글자색
        CharacterSelectButton.setFocusPainted(false); // 테두리
        CharacterSelectButton.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 20)); // 폰트
        CharacterSelectButton.setBounds(700, 500, 100, 50);

        CharacterSelectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Move to Character Select Frame
                CharacterSelectFrame characterSelectFrame = new CharacterSelectFrame(player, player.getPlayerShip());
                setVisible(false);
            }
        });
        getContentPane().add(CharacterSelectButton);



        setVisible(true);
    }

    private void gameIntroductionDialog() {
        String introductionText = "1. 코인 획득 방법 : 몬스터 피격시 일정 확률로 코인이 drop 됩니다.\n" +
                "2. 상점 이용 방법 : 획득한 코인은 DB에 저장됩니다. 저장된 코인으로 아이템을 구매하세요!\n" +
                "3. Level 시스템 : 총 5가지의 단계로 게임은 구성되어 있습니다.\n" +
                "4. Login 시스템 : 당신의 고유 아이디로 게임의 정보를 저장하고 플레이하세요!";
        String highlightedText = "Introduction";

        JTextPane introductionPane = new JTextPane();
        introductionPane.setEditable(false);
        introductionPane.setBackground(new Color(0, 0, 0, 0)); // 배경 투명
        introductionPane.setOpaque(false);
        introductionPane.setForeground(Color.BLACK); // 텍스트 색상

        // 기본 텍스트 스타일 설정
        SimpleAttributeSet defaultAttributes = new SimpleAttributeSet();
        StyleConstants.setFontFamily(defaultAttributes, "Verdana");
        StyleConstants.setFontSize(defaultAttributes, 14);

        // 강조 텍스트 스타일 설정
        SimpleAttributeSet highlightedAttributes = new SimpleAttributeSet();
        StyleConstants.setFontFamily(highlightedAttributes, "Verdana");
        StyleConstants.setFontSize(highlightedAttributes, 20);
        StyleConstants.setBold(highlightedAttributes, true);

        // JTextPane의 Document에 텍스트와 스타일 적용
        Document doc = introductionPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), highlightedText + "\n\n", highlightedAttributes);
            doc.insertString(doc.getLength(), introductionText, defaultAttributes);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(this, introductionPane, "게임 설명", JOptionPane.INFORMATION_MESSAGE);
    }

}
