package main;

public class Shot extends Thread {
	private int x, y, v0, d = -1,
				xt, yt = 0;
	private double cosAngle,
				   sinAngle,
				   t;
	private GUI gui;
	
	public Shot(GUI g) {
		gui = g;
	}
	
	public Shot() {
		
	}
		
	public void makeShot(int v0, int angle, int xt, int yt) {
		refresh();
		this.xt = xt;
		this.yt = yt;
		this.v0 = v0;
		cosAngle = Math.round(Math.cos(Math.toRadians(angle)) * 100) / 100.;
		sinAngle = Math.round(Math.sin(Math.toRadians(angle)) * 100) / 100.;
		start();
	}
	
	public void run() {
		try {
			while(calcNext()) {
				gui.setPos(x, y);
				checkDistance();
				Thread.sleep(50);
			}
			System.out.println(d);
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally { refresh(); }
	}
	
	private boolean calcNext() {
		x = (int) ((x + v0 * t * cosAngle));
		y = (int) ((y + v0 * t * sinAngle - 5 * (t*t)));
		t += .05;
		return (x < -10 || y < -10 || x > 450 || y > 1000) ? false : true;
	}
	
	private void checkDistance() {
		int tmp = (int) Math.sqrt(((x - xt) * (x - xt)) + ((y - yt) * (y - yt)));
		d = (d < 0) ? tmp : (d > tmp) ? tmp : d;
	}
	
	private void refresh() {
		t = sinAngle = cosAngle = x = y = v0 = 0;
		d = - 1;
	}
}
