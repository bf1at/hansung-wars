package hansungwar;

public class Unit {
	double hp = 100.0;
	double moveSpeed = 1.0;
	double attackPower = 10.0;
	double attackSpeed = 1.0;
	double attackRange = 40.0;
	
	double x;
	double y;
	double width = 40;
	double height = 40;
	
	// 중심 좌표
	public double center() {
		return x + width/2;
	}
	
	public void move(int direction) {
		x += moveSpeed * direction;
	}
	
	public void attack(Unit target) {
		
	}
	
	
}
