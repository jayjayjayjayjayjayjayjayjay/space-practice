package org.newdawn.spaceinvaders.dataBase;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.*;
import org.newdawn.spaceinvaders.frame.MainFrame;
import org.newdawn.spaceinvaders.user.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class Rank extends JFrame {

    private JLabel titleLabel;
    private JButton BackButton;
    private JLabel scoreLabel;
    private Player player;
    private DB db;
//    private HashMap<String, Integer> userData = new HashMap<>();

    public Rank(Player player) throws FirebaseAuthException {

        super("Rank");
        this.player = player;
        db = new DB();
        loadRank();
//        db.storeScore(50);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // JFrame 닫히면 프로그램 종료
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(null); // 창을 화면 중앙에 배치

        // get hold the content of the frame and set up the resolution of the game
        setContentPane(new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                Image backgroundImage = new ImageIcon("src/main/resources/background/Background.png").getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        });

        setIgnoreRepaint(false);
        getContentPane().setLayout(null);

        titleLabel = new JLabel("Rank");
        titleLabel.setForeground(Color.WHITE); // 기본 글씨 색을 검은색으로 설정합니다.
        titleLabel.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 35));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBounds(300, 100, 200, 55);
        getContentPane().add(titleLabel);

        // 버튼 추가
        BackButton = new JButton("Back");
        // 버튼 서식
        BackButton.setOpaque(false);
        BackButton.setContentAreaFilled(false); // 배경
        BackButton.setBorderPainted(false); // 배경
        BackButton.setForeground(Color.WHITE); // 글자색
        BackButton.setFocusPainted(false); // 테두리
        BackButton.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 20)); // 폰트
        BackButton.setBounds(0, 0, 100, 50);

        BackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame mainFrame = new MainFrame(player);
                setVisible(false);
            }
        });
        getContentPane().add(BackButton);

        // scoreLabel 초기화
        scoreLabel = new JLabel();
        scoreLabel.setForeground(Color.BLACK); // 기본 글씨 색을 검은색으로 설정합니다.
        scoreLabel.setBounds(200, 200, 400, 50);
        getContentPane().add(scoreLabel);

        // finally make the window visible
//         pack();
//         setResizable(false);
        setVisible(true);



    }

    public void loadRank() {
        // Rank 불러오기
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        // 상위 10명의 사용자 가져오기
        Query topScoresQuery = myRef.orderByChild("score").limitToLast(10);
        topScoresQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String userId = childSnapshot.getKey();
                    long score = childSnapshot.child("score").getValue(Long.class);
                    System.out.println(userId + ": " + score);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 처리할 오류 처리
                System.out.println("failed");
            }
        });
    }

    public void displayScore(int timer, int alienKill) {
        // 점수를 GUI에 표시하는 코드를 여기에 작성합니다.
        scoreLabel.setText("Time: " + timer + ", Alien Kills: " + alienKill);
    }
}
