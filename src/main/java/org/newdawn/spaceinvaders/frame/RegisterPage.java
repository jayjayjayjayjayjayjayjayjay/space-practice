package org.newdawn.spaceinvaders.frame;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.newdawn.spaceinvaders.user.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterPage extends JFrame {
    private JLabel titleLabel;
    private JLabel emailLabel;
    private JLabel userNameLabel;
    private JTextField emailField;
    private JPasswordField userNameField;
    private JButton addAccountButton;
    private JButton backButton;
    private Player player;
    public RegisterPage() {
        // Window Setting
        setTitle("Login");
        this.player = player;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // JFrame 닫히면 프로그램 종료
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null); // 창을 화면 중앙에 배치

        // get hold the content of the frame and set up the resolution of the game
        JPanel panel = (JPanel) getContentPane();
        panel.setPreferredSize(new Dimension(800,600));
        panel.setLayout(null);

        titleLabel = new JLabel("Register");
        titleLabel.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 35));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBounds(300, 170, 200, 55);
        panel.add(titleLabel);

        // 이메일 입력 필드
        emailLabel = new JLabel("Email");
        emailLabel.setHorizontalAlignment(JLabel.CENTER);
        emailLabel.setBounds(280, 240, 60, 30);
        emailField = new JTextField(10);
        emailField.setBounds(340, 240, 160, 30);
        getContentPane().add(emailLabel);
        getContentPane().add(emailField);

        // 비밀번호 입력 필드
        userNameLabel = new JLabel("Name");
        userNameLabel.setHorizontalAlignment(JLabel.CENTER);
        userNameLabel.setBounds(280, 280, 60, 30);
        userNameField = new JPasswordField(10);
        userNameField.setBounds(340, 280, 160, 30);
        getContentPane().add(userNameLabel);
        getContentPane().add(userNameField);

        // 계정 생성 버튼
        addAccountButton = new JButton("Create Account");
        // 버튼 서식
        addAccountButton.setOpaque(false);
//        addAccountButton.setContentAreaFilled(false); // 배경
        addAccountButton.setBackground(Color.WHITE); // 배경색
//        addAccountButton.setBorderPainted(false); // 외곽선
//        addAccountButton.setForeground(Color.WHITE); // 글자색
//        addAccountButton.setFocusPainted(false); // 테두리
        addAccountButton.setFont(new Font("Arial", Font.PLAIN, 15)); // 폰트
        addAccountButton.setBounds(300, 330, 200, 35);
        addAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                            .setEmail(emailField.getText())
                            .setEmailVerified(false)
                            .setDisplayName(String.valueOf(userNameField.getPassword()));

                    FirebaseAuth.getInstance().createUser(request);
                    Logger.getLogger(RegisterPage.class.getName()).log(Level.INFO, "SUCCESS");
                    JOptionPane.showMessageDialog(null, "SUCCESS");
                }
                catch (FirebaseAuthException ex){
                    Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
            }

                new LoginPage();
                dispose();
            }
        });
        getContentPane().add(addAccountButton);

        // 버튼 추가
        backButton = new JButton("Back");
        // 버튼 서식
        backButton.setOpaque(false);
        backButton.setContentAreaFilled(false); // 배경
        backButton.setBorderPainted(false); // 배경
        backButton.setFocusPainted(false); // 테두리
        backButton.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 20)); // 폰트
        backButton.setBounds(0, 500, 100, 20); // set position and size

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginPage loginPage = new LoginPage();
                setVisible(false);
            }
        });
        getContentPane().add(backButton);

        setVisible(true);
    }
}
