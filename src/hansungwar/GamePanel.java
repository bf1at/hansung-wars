package hansungwar;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.ArrayList;

public class GamePanel extends JPanel {
	
	// 유닛을 관리할 배열
	private final ArrayList<Unit> player1Units = new ArrayList<>();
	private final ArrayList<Unit> player2Units = new ArrayList<>();
	

    public GamePanel() {
    	
        setLayout(null);
        setPreferredSize(new Dimension(900, 600));
        
        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel.setBounds(0, 465, 900, 135);
        add(panel);
        panel.setLayout(null);
        
        JButton btnNewButton_1 = new JButton("1");
        btnNewButton_1.setBounds(33, 27, 82, 75);
        panel.add(btnNewButton_1);
        btnNewButton_1.addActionListener(e -> {
        	Unit unit = new Unit();
            unit.x = 50;
            unit.y = 425;
            player1Units.add(unit);
            repaint();
        });
        
        JButton btnNewButton_2 = new JButton("2");
        btnNewButton_2.setBounds(148, 27, 82, 75);
        panel.add(btnNewButton_2);
        btnNewButton_2.addActionListener(e -> {
        	Unit unit = new Unit();
            unit.x = 810;
            unit.y = 425;
            player2Units.add(unit);
            repaint();
        });
        
        JButton btnNewButton_3 = new JButton("3");
        btnNewButton_3.setBounds(266, 27, 82, 75);
        panel.add(btnNewButton_3);
        
        JButton btnNewButton_4 = new JButton("4");
        btnNewButton_4.setBounds(390, 27, 82, 75);
        panel.add(btnNewButton_4);
        
        JButton btnNewButton_5 = new JButton("5");
        btnNewButton_5.setBounds(510, 27, 82, 75);
        panel.add(btnNewButton_5);
        
        // 10밀리초마다 이동 함수 호출
        Timer timer = new Timer(10, e -> {
        	int length1 = player1Units.size();
        	for(int i=0; i<length1; i++) {
        		player1Units.get(i).move(1);
        		repaint();
        	}
        	
        	int length2 = player2Units.size();
        	for(int i=0; i<length2; i++) {
        		player2Units.get(i).move(-1);
        		repaint();
        	}
        });
        timer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int length1 = player1Units.size();
    	for(int i=0; i<length1; i++) {
    		Unit unit = player1Units.get(i);
    		g.setColor(Color.BLUE);
    		g.fillRect(unit.x, unit.y, unit.width, unit.height);
    	}
        
    	int length2 = player2Units.size();
    	for(int i=0; i<length2; i++) {
    		Unit unit = player2Units.get(i);
    		g.setColor(Color.RED);
    		g.fillRect(unit.x, unit.y, unit.width, unit.height);
    	}
    }
    
}