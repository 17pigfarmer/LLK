package llk.viewer;

public class PathLength {
	private int ax;
	private int ay;
	private int bx;
	private int by;
	private int length;
	public int getAx() {
		return ax;
	}
	public void setAx(int ax) {
		this.ax = ax;
	}
	public int getAy() {
		return ay;
	}
	public void setAy(int ay) {
		this.ay = ay;
	}
	public int getBx() {
		return bx;
	}
	public void setBx(int bx) {
		this.bx = bx;
	}
	public int getBy() {
		return by;
	}
	public void setBy(int by) {
		this.by = by;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	
	public void showLength() {
		System.out.println(this.getLength());
	}
	public PathLength(int ax, int ay, int bx, int by, int length) {

		this.ax = ax;
		this.ay = ay;
		this.bx = bx;
		this.by = by;
		this.length = length;
	}
	
}
