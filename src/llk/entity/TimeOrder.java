package llk.entity;

public class TimeOrder {
	
	public TimeOrder(int id, String name, int time) {
		super();
		this.id = id;
		this.name = name;
		this.time = time;
	}
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	private String name;
	private int time;
}
