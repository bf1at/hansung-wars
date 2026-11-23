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
        
        // 10밀리초마다 호출
        Timer timer = new Timer(10, e -> {
        	int length1 = player1Units.size();
        	int length2 = player2Units.size();
        	
        	// 플레이어1 유닛 입장에서 적이 공격 사거리 내에 있는지 검사 후 있으면 공격, 없으면 이동
        	for(int i=0; i<length1; i++) {
        		Unit unit = player1Units.get(i);
        		if(unit.state == Unit.State.DIE)
        			continue;
        		double unitCenter = unit.center();
        		int targetIndex = -1;	// 공격 타겟으로 정한 적의 인덱스, -1이면 공격하지 않고 넘어간다
        		double targetCenter = Double.POSITIVE_INFINITY;
        		
        		for(int j=0; j<length2; j++) {
        			Unit enemy = player2Units.get(j);
        			double enemyCenter = enemy.center();
        			if(enemyCenter >= unitCenter && enemyCenter <= unitCenter + unit.attackRange && enemy.state != Unit.State.DIE) {
        				if(targetCenter > enemyCenter) {
        					targetIndex = j;
        					targetCenter = enemyCenter;
        				}
        			}
        		}
        		
        		if(targetIndex == -1) {
        			// 우측으로 이동
        			player1Units.get(i).move(1);
        		}
        		else {
        			Unit target = player2Units.get(targetIndex);
        			unit.attack(target);
        			if (target.state == Unit.State.DIE) {
        	            destroy(target, 2000); // [임시] 2초 뒤 유닛 제거
        	        }
        		}
        		repaint();
        	}
        	
        	// 플레이어2 유닛 입장에서 적이 공격 사거리 내에 있는지 검사 후 있으면 공격, 없으면 이동
        	for (int i = 0; i < length2; i++) {
        	    Unit unit = player2Units.get(i);
        	    if(unit.state == Unit.State.DIE)
        			continue;
        	    double unitCenter = unit.center();
        	    int targetIndex = -1;
        	    double targetCenter = Double.NEGATIVE_INFINITY;

        	    for (int j = 0; j < length1; j++) {
        	        Unit enemy = player1Units.get(j);
        	        double enemyCenter = enemy.center();
        	        if (enemyCenter >= unitCenter-unit.attackRange && enemyCenter <= unitCenter && enemy.state != Unit.State.DIE) {
        	        	if(enemyCenter > targetCenter) {
        	        		targetIndex = j;
        	        		targetCenter = enemyCenter;
        	        	}
        	        }
        	    }

        	    if (targetIndex == -1) {
        	    	// 좌측으로 이동
        	        unit.move(-1);
        	    } 
        	    else {
        	    	Unit target = player1Units.get(targetIndex);
        	        unit.attack(target);
        	        if (target.state == Unit.State.DIE) {
        	            destroy(target, 2000); // [임시] 2초 뒤 유닛 제거
        	        }
        	    }
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
    		g.fillRect((int)unit.x, (int)unit.y, (int)unit.width, (int)unit.height);
    	}
        
    	int length2 = player2Units.size();
    	for(int i=0; i<length2; i++) {
    		Unit unit = player2Units.get(i);
    		g.setColor(Color.RED);
    		g.fillRect((int)unit.x, (int)unit.y, (int)unit.width, (int)unit.height);
    	}
    }
    
    // [임시] 밀리초 뒤에 유닛 목록에서 제거하는 함수
    private void destroy(Unit target, int time) {
    	Timer removeTimer = new Timer(time, e -> {
            player1Units.remove(target);
            player2Units.remove(target);
            repaint();
        });
        removeTimer.setRepeats(false);
        removeTimer.start();
    }
}