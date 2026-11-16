package hansungwar;

public class Unit {
	int hp = 100;
	int moveSpeed = 1;
	int attackPower = 10;
	double attackSpeed = 1.0;
	
	int x;
	int y;
	int width = 40;
	int height = 40;
	
	public void move(int direction) {
		x += moveSpeed * direction;
	}
}
