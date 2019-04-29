package llk.viewer;


public class PathNode  {
	
	public final int Y = 100;
	public final int X = 101;
	public final int ONE = 102;
	public final int TWO = 103;
	public final int THREE = 104;
	public final int FOUR = 105;
	
	
	private int x;
	private int y;
	private int forward;
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getForward() {
		return forward;
	}
	public void setForward(int forward) {
		this.forward = forward;
	}
	
	
	public void setMap(int[][] map) {
		map[x][y]=forward;
	}
	
	public void showyourself() {
		String str;
		switch(this.getForward()) {
		case X:
			str = "X";
			break;
		case Y:
			str = "Y";
			break;
		case ONE:
			str = "ONE";
			break;
		case TWO:
			str = "TWO";
			break;
		case THREE:
			str = "THREE";
			break;
		case FOUR:
			str = "FOUR";
			break;
		default:
			str = "";
		}
		
		
		System.out.println("("+this.x+","+this.y+","+str+")");
	}
	
	
	public PathNode(int x,int y,int forward)
	{
		this.x=x;
		this.y=y;
		this.forward=forward;
	}
}
