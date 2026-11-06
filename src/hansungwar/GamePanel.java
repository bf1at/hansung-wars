package hansungwar;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class GamePanel extends JPanel {
	
	// [test] 테스트를 위한 유닛 객체들
	Unit unit1;
	Unit unit2;

    public GamePanel() {
    	
    	// [test] 테스트 적
    	unit2 = new Unit(2);
    	unit2.x = 300;
    	unit2.y = 425;
    	
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
            unit1 = new Unit(1);
            unit1.x = 50;
            unit1.y = 425;
            repaint();
        });
        
        JButton btnNewButton_1_1 = new JButton("2");
        btnNewButton_1_1.setBounds(148, 27, 82, 75);
        panel.add(btnNewButton_1_1);
        
        JButton btnNewButton_1_2 = new JButton("3");
        btnNewButton_1_2.setBounds(266, 27, 82, 75);
        panel.add(btnNewButton_1_2);
        
        JButton btnNewButton_1_3 = new JButton("4");
        btnNewButton_1_3.setBounds(390, 27, 82, 75);
        panel.add(btnNewButton_1_3);
        
        JButton btnNewButton_1_4 = new JButton("5");
        btnNewButton_1_4.setBounds(510, 27, 82, 75);
        panel.add(btnNewButton_1_4);
        
        // 10밀리초마다 이동 함수 호출
        Timer timer = new Timer(10, e -> {
            if (unit1 != null) {
                unit1.move();
                repaint();
            }
        });
        timer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (unit1 != null) {
            g.setColor(Color.BLUE);
            g.fillRect(unit1.x, unit1.y, unit1.width, unit1.height);
        }
        
        if (unit2 != null) {
        	g.setColor(Color.RED);
            g.fillRect(unit2.x, unit2.y, unit2.width, unit2.height);
        }
    }
    
}