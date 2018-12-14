package upb.demo;

import java.util.Random;

public class VelDemo {

	private float trgtSpeed;

	private SpeedMode curMode = SpeedMode.QUICK_TARGET;

	private float topSpeedLim;
	private Random rand;

	public VelDemo(float trgtSpeed, float overshootMult) {
		super();
		rand = new Random();
		this.trgtSpeed = trgtSpeed;
		this.topSpeedLim = overshootMult * trgtSpeed;
	}

	public float getAccMult(float curSpeed) {
		float accPerc = 0;
		switch (curMode) {
		case QUICK_TARGET:
			if (curSpeed <= topSpeedLim) {
				accPerc = 1;
			} else {
				curMode = SpeedMode.QUICK_DEACC;
				accPerc = 0;
			}
			break;
		case QUICK_DEACC:
			if (curSpeed > trgtSpeed) {
				accPerc = -0.45f;
			} else {
				curMode = SpeedMode.STABLE_VEL;
				accPerc = 0;
			}
		case STABLE_VEL:
			if (curSpeed > trgtSpeed) {
				// accPerc = -0.15f;
				accPerc = -1 * rand.nextFloat();
			} else {
				// accPerc = 0.15f;
				accPerc = 1 * rand.nextFloat();
			}

		default:
			break;
		}
		return accPerc;
	}

	public void reset() {
		this.curMode = SpeedMode.QUICK_TARGET;
	}

	// Demonstration
	public static void main(String[] args) throws InterruptedException {
		float curSpeed = 0;
		float maxAcc = 30f;
		VelDemo velDem = new VelDemo(300f, 1.1f);
		while (true) {
			float accMult = velDem.getAccMult(curSpeed);
			curSpeed += accMult * maxAcc;
			System.out.println("Current speed is: " + curSpeed);
			Thread.sleep(17);
		}
	}

}

enum SpeedMode {
	QUICK_TARGET, QUICK_DEACC, STABLE_VEL;
}
