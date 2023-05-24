package org.newdawn.spaceinvaders.frame;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.newdawn.spaceinvaders.user.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginPage extends JFrame{
    private JLabel idLabel;
    private JLabel nameLabel;
    private JTextField idField;
    private JPasswordField pwField;
    private JLabel message;
    private JButton loginButton;
    private JButton registerButton;
    private String id = null;
    private String pw = null;
    private Player player;

    static String userName = null;

    public LoginPage() {
        // Window Setting
        super("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // JFrame 닫히면 프로그램 종료
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null); // 창을 화면 중앙에 배치

        // get hold the content of the frame and set up the resolution of the game
        setContentPane(new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                Image backgroundImage = new ImageIcon("src/main/resources/background/loginBackground.png").getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        });
        getContentPane().setLayout(null);

//        JLabel titleLabel = new JLabel("Login");
////        titleLabel.setForeground(Color.BLACK); // 글자색
////        titleLabel.setOpaque(true);
////        titleLabel.setBackground(Color.);
//        titleLabel.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 35));
//        titleLabel.setHorizontalAlignment(JLabel.CENTER);
//        titleLabel.setBounds(300, 170, 200, 55);
//        getContentPane().add(titleLabel);

        // 아이디 입력 필드
        idLabel = new JLabel("ID");
        idLabel.setHorizontalAlignment(JLabel.CENTER);
        idLabel.setBounds(280, 240, 60, 30);
        idField = new JTextField(10);
        idField.setBounds(340, 240, 160, 30);
        getContentPane().add(idLabel);
        getContentPane().add(idField);

        // 비밀번호 입력 필드
        nameLabel = new JLabel("Name");
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        nameLabel.setBounds(280, 280, 60, 30);
        pwField = new JPasswordField(10);
        pwField.setBounds(340, 280, 160, 30);
        getContentPane().add(nameLabel);
        getContentPane().add(pwField);

        message = new JLabel("");
        message.setHorizontalAlignment(JLabel.CENTER);
        message.setBounds(300, 415, 200, 30);
        getContentPane().add(message);

        // 로그인 버튼
        loginButton = new JButton("Login");
        // 버튼 서식
        loginButton.setOpaque(false);
//        loginButton.setContentAreaFilled(false); // 배경
        loginButton.setBackground(Color.WHITE); // 배경색
//        loginButton.setBorderPainted(false); // 외곽선
//        loginButton.setForeground(Color.WHITE); // 글자색
//        loginButton.setFocusPainted(false); // 테두리
        loginButton.setFont(new Font("Arial", Font.PLAIN, 15)); // 폰트
        loginButton.setBounds(300, 330, 200, 35);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 로그인 버튼을 클릭한 경우
                if (e.getSource() == loginButton) {
                    id = idField.getText();
                    pw = new String(pwField.getPassword());

                    if (id.isEmpty() || pw.isEmpty()) {
                        message.setText("ID 또는 PW를 입력하세요");
                    } else {
                        getDataByEmail();
                    }
                }
            }
        });
        getContentPane().add(loginButton);

        // 회원가입 버튼
        registerButton = new JButton("Register");
        // 버튼 서식
        registerButton.setOpaque(false);
//        registerButton.setContentAreaFilled(false); // 배경
        registerButton.setBackground(Color.WHITE); // 배경색
//        registerButton.setBorderPainted(false); // 외곽선
//        registerButton.setForeground(Color.WHITE); // 글자색
//        registerButton.setFocusPainted(false); // 테두리
        registerButton.setFont(new Font("Arial", Font.PLAIN, 15)); // 폰트
        registerButton.setBounds(300, 370, 200, 35);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterPage();
                dispose();
            }
        });
        getContentPane().add(registerButton);

        setVisible(true);
    }

    private void getDataByEmail(){

        UserRecord userRecord = null;
        try {
            userRecord = FirebaseAuth.getInstance().getUserByEmail(idField.getText());
            String email = userRecord.getEmail();
            String uid = userRecord.getUid();
            userName = userRecord.getDisplayName();

            if (userName.equals(String.valueOf(pwField.getPassword()))){
                JOptionPane.showMessageDialog(null, "Hello" + " " + email);
                player = new Player();
                MainFrame mainFrame = new MainFrame(player);
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.");
            }

            recoverUserData(uid);
        } catch (FirebaseAuthException ex) {
            Logger.getLogger(RegisterPage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUserEmail() throws FirebaseAuthException {
        UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(idField.getText());
        return userRecord.getEmail();
    }

    private void recoverUserData(String uid){
        UserRecord userRecord = null;
        try {
            userRecord = FirebaseAuth.getInstance().getUser(uid);

            Logger.getLogger(LoginPage.class.getName()).log(Level.INFO, "유저 데이터를 성공적으로 Fetch:");
            Logger.getLogger(LoginPage.class.getName()).log(Level.INFO, userRecord.getUid());
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }
    }

    public static String getUserName() {
        return userName;
    }
}