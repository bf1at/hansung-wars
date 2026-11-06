package hansungwar;

public class Unit {
	int playerId;	// 어느 플레이어의 유닛인지 식별
	
	int hp = 100;
	int moveSpeed = 1;
	int attackPower = 10;
	double attackSpeed = 1.0;
	
	int x;
	int y;
	int width = 40;
	int height = 40;
	
	public Unit(int playerId){
		this.playerId = playerId;
	}
	
	public void move() {
	    if (playerId == 1) {
	        x += moveSpeed;
	    }
	}
}
