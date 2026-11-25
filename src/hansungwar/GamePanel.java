package hansungwar;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.ArrayList;

public class GamePanel extends JPanel {
	
	// 유닛을 관리할 배열
	private final ArrayList<Unit> player1Units = new ArrayList<>();
	private final ArrayList<Unit> player2Units = new ArrayList<>();

    // UI 버튼 배치 (기존 UI 버전 설정)
    private static final int BUTTON_SIZE = 90;
    private static final int BUTTON_Y = 20;
	

    public GamePanel() {
    	
        setLayout(null);
        setPreferredSize(new Dimension(900, 600));
        
        // 하단 UI 패널
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(new LineBorder(Color.BLACK));
        panel.setBounds(0, 465, 900, 135);
        add(panel);

        // 1. 지갑 강화 버튼
        JButton btnWallet = createImageButton(
                "MoneyUP",
                33, BUTTON_Y-10, BUTTON_SIZE+20
        );
        panel.add(btnWallet);

        // 2. 상상부기 버튼
        JButton btnSangsang = createImageButton(
                "Sangsangbugi",
                248, BUTTON_Y, BUTTON_SIZE
        );
        panel.add(btnSangsang);

        // 3. 한성냥이 버튼
        JButton btnCat = createImageButton(
                "Hansungnyangi",
                363, BUTTON_Y, BUTTON_SIZE
        );
        panel.add(btnCat);

        // 4. 상찌 버튼
        JButton btnFrog = createImageButton(
                "Sangjji",
                478, BUTTON_Y, BUTTON_SIZE
        );
        panel.add(btnFrog);

        // 5. 꼬꼬&꾸꾸 버튼
        JButton btnChicken = createImageButton(
                "Kokko_Kkukkuk",
                593, BUTTON_Y, BUTTON_SIZE
        );
        panel.add(btnChicken);

        // -------------------------
        // 버튼 클릭 테스트
        // -------------------------

        // 지갑 버튼: 임시로 파란 유닛 생성
        btnWallet.addActionListener(e -> {
            Unit unit = new Unit();
            unit.x = 50;
            unit.y = 425;
            player1Units.add(unit);
            repaint();
        });

        // 상상부기 버튼: 임시로 빨간 유닛 생성
        btnSangsang.addActionListener(e -> {
            Unit unit = new Unit();
            unit.x = 810;
            unit.y = 425;
            player2Units.add(unit);
            repaint();
        });

        // 나머지는 UI 테스트용 파란 유닛 생성
        btnCat.addActionListener(e -> spawnTestUnit());
        btnFrog.addActionListener(e -> spawnTestUnit());
        btnChicken.addActionListener(e -> spawnTestUnit());

        // 비활성화 이미지 확인용
        // 원하는 버튼에 아래 코드를 적용해서 테스트
        // btnFrog.setEnabled(false);

        // 최신 게임 로직: 이동, 공격, 체력 처리는 아래 코드 그대로 사용
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
    
    // 테스트용 유닛 생성
    private void spawnTestUnit() {

        Unit unit = new Unit();
        unit.x = 50;
        unit.y = 425;

        player1Units.add(unit);
        repaint();
    }

    // 이미지 버튼 생성
    private JButton createImageButton(
            String name, int x, int y, int size) {

        String normal = name + "_Enabled.png";
        String disabled = name + "_Disabled.png";

        // 모든 버튼은 공통 파일명 규칙 사용
        String pressed = name + "_Pressed.png";

        ImageIcon normalIcon = loadIcon(normal, size);
        ImageIcon pressedIcon = loadIcon(pressed, size);
        ImageIcon disabledIcon = loadIcon(disabled, size);

        JButton button = new JButton(normalIcon);

        button.setBounds(x, y, size, size);

        button.setPressedIcon(pressedIcon);
        button.setDisabledIcon(disabledIcon);

        // 기본 Swing 버튼 테두리 제거
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        return button;
    }

    // PNG 불러오기 + 크기 조절
    private ImageIcon loadIcon(String fileName, int size) {

        String path = "/hansungwar/images/" + fileName;

        URL url = getClass().getResource(path);

        if (url == null) {
            throw new IllegalArgumentException(
                    "이미지 파일을 찾을 수 없습니다: " + path
            );
        }

        ImageIcon original = new ImageIcon(url);

        Image scaled = original.getImage()
                .getScaledInstance(
                        size,
                        size,
                        Image.SCALE_SMOOTH
                );

        return new ImageIcon(scaled);
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