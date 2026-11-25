package hansungwar;

public class Unit {
	enum State {
		IDLE,
	    MOVE,
	    ATTACK,
	    DIE
	}
	
	State state;
	
	double hp = 100.0;
	double moveSpeed = 1.0;
	double attackPower = 20.0;
	double attackSpeed = 1.0;
	long lastAttackTimeMs = 0; // 밀리초 단위
	double attackRange = 40.0;
	
	double x;
	double y;
	double width = 40;
	double height = 40;
	
	public Unit() {
		this.state = State.MOVE;
	}
	
	// 중심 좌표
	public double center() {
		return x + width/2;
	}
	
	public void move(int direction) {
		if(this.state != State.MOVE)
			this.state = State.MOVE;
		x += moveSpeed * direction;
	}
	
	public boolean tryAttack(long now) {
	    long intervalMs = (long) (attackSpeed * 1000);

	    if (now - lastAttackTimeMs < intervalMs) {
	        return false;
	    }

	    state = State.ATTACK;
	    lastAttackTimeMs = now;
	    return true;
	}
	
	public void takeDamage(double damage) {
		this.hp -= damage;
		if(hp <= 0)
			die();
	}
	
	public void die() {
		if(this.state != State.DIE)
			this.state = State.DIE;
	}
}
