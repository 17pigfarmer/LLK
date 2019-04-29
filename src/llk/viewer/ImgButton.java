package llk.viewer;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ImgButton extends JButton {
	private int x;
	private int y;
	public int getx() {
		return x;
	}
	public void setx(int x) {
		this.x = x;
	}
	public int gety() {
		return y;
	}
	public void sety(int y) {
		this.y = y;
	}
	
	public void getImg(int num,int size) {
		if(num != 0) {
			String filename = this.getClass().getResource("/img/"+num+".jpg").getFile();
			ImageIcon icon = new ImageIcon(filename);
			icon.setImage(icon.getImage().getScaledInstance(500/(size), 500/(size), 1));
			this.setIcon(icon);
			this.setDisabledIcon(this.getIcon());
		}else if(num == 0) {
			this.setIcon(null);
			
		}
	}
	
	
	
	public ImgButton(int x,int y) {
		this.x = x;
		this.setBackground(new Color(0, 0, 0));
		this.setBorder(null);
		this.y = y;
	}
}
