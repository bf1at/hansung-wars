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
            // 1. 아무도 움직이기 전에 양쪽 모두 이동 여부를 결정
            ArrayList<Unit> movers1 = findMovers(player1Units, player2Units, 1);
            ArrayList<Unit> movers2 = findMovers(player2Units, player1Units, -1);

            // 2. 이동하기로 한 유닛을 실제로 이동
            for (Unit unit : movers1) {
                unit.move(1);
            }
            for (Unit unit : movers2) {
                unit.move(-1);
            }

            // 3. 이동 후 위치에서 양쪽 공격을 판정
            long now = System.currentTimeMillis();
            double[] damageToP2 = collectDamage(player1Units, player2Units, 1, now);
            double[] damageToP1 = collectDamage(player2Units, player1Units, -1, now);

            // 4. 양쪽 공격 판정이 끝났으니 체력에서 피해를 빼기
            applyDamage(player1Units, damageToP1);
            applyDamage(player2Units, damageToP2);

            repaint();
        });
        timer.start();
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
    
    // 화면에 요소 그리기
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int length1 = player1Units.size();
    	for(int i=0; i<length1; i++) {
    		Unit unit = player1Units.get(i);
    		g.setColor(Color.BLUE);
    		g.fillRect((int)unit.x, (int)unit.y, (int)unit.width, (int)unit.height);
    		drawHealthBar(g, unit);
    	}
        
    	int length2 = player2Units.size();
    	for(int i=0; i<length2; i++) {
    		Unit unit = player2Units.get(i);
    		g.setColor(Color.RED);
    		g.fillRect((int)unit.x, (int)unit.y, (int)unit.width, (int)unit.height);
    		drawHealthBar(g, unit);
    	}
    }
    
    private int findTargetIndex(Unit unit, ArrayList<Unit> enemies, int direction) {
        int targetIndex = -1;
        double closestDistance = Double.POSITIVE_INFINITY;

        for (int i = 0; i < enemies.size(); i++) {
            Unit enemy = enemies.get(i);

            if (enemy.state == Unit.State.DIE) {
                continue;
            }

            double distance = (enemy.center() - unit.center()) * direction;

            if (distance >= 0 && distance <= unit.attackRange
                    && distance < closestDistance) {
                closestDistance = distance;
                targetIndex = i;
            }
        }

        return targetIndex;
    }
    
    private ArrayList<Unit> findMovers(ArrayList<Unit> allies, ArrayList<Unit> enemies, int direction) {
    	ArrayList<Unit> movers = new ArrayList<>();

    	for (Unit unit : allies) {
    		if (unit.state == Unit.State.DIE) {
    			continue;
    		}

    		if (findTargetIndex(unit, enemies, direction) == -1) {
    			movers.add(unit);
    		}
    	}
	
    	return movers;
    }
    
    private double[] collectDamage(ArrayList<Unit> allies, ArrayList<Unit> enemies, int direction, long now) {
    	double[] damage = new double[enemies.size()];

    	for (Unit unit : allies) {
    		if (unit.state == Unit.State.DIE) {
    			continue;
    		}

    		int targetIndex = findTargetIndex(unit, enemies, direction);

    		if (targetIndex != -1 && unit.tryAttack(now)) {
    			damage[targetIndex] += unit.attackPower;
    		}
    	}

    	return damage;
    }
    
    private void applyDamage(ArrayList<Unit> units, double[] damage) {
        for (int i = 0; i < units.size(); i++) {
            if (damage[i] == 0) {
                continue;
            }

            Unit unit = units.get(i);
            unit.takeDamage(damage[i]);

            if (unit.state == Unit.State.DIE) {
                destroy(unit, 2000);
            }
        }
    }
    
    private void drawHealthBar(Graphics g, Unit unit) {;
        int width = 35;
        int x = (int) (unit.center() - width / 2.0);
        int y = (int) unit.y - 10;
        int remainingWidth = (int) (width * Math.max(0, unit.hp) / 100.0);

        g.setColor(Color.GRAY);
        g.fillRect(x, y, width, 4);

        g.setColor(Color.GREEN);
        g.fillRect(x, y, remainingWidth, 4);
    }
}