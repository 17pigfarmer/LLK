package llk.viewer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import llk.dao.HandleDB;
import llk.viewer.TimeOrderDialog;

import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JComboBox;

public class MainFrame extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JPanel playPanel = new JPanel();
	private int lv ;
	private int size ;
	public LinkedList<PathNode> pathList = new LinkedList<PathNode>();
	public int state = 0;
	public ImgButton btn1;
	public ImgButton btn2;

	public final int Y = 100;
	public final int X = 101;
	public final int ONE = 102;
	public final int TWO = 103;
	public final int THREE = 104;
	public final int FOUR = 105;
	public final int IS_PATH = 50;
	
	HandleDB hdb = new HandleDB();

	public JButton btnStart;
	public JButton btnOrder;
	public JLabel lblLeftTime;
	Timer timer = null;
	public int time  = 0;
	
	public boolean isRun=false;

	public int [][] map ;
	
	public ImgButton[][] btnMap;
	private JComboBox cbLv;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnStart) {
			if (isRun) {
				int n = JOptionPane.showConfirmDialog(this, "您真的要重新开始游戏吗？");
				if (n == 0) {
					init();
					isRun = true;
					startThread();
					lblLeftTime.setText(time+"");
				}
			}else {
				init();
				startThread();
				isRun = true;
				btnStart.setText("重新开始");
				lblLeftTime.setText(time+"");
			}
		} else if(e.getSource() == btnOrder){
			new TimeOrderDialog(this, true);
		}else {
			if (state == 0) {
				btn1 = (ImgButton) e.getSource();
				HandleClick();
				state = 1;
			} else if (state == 1) {
				btn2 = (ImgButton) e.getSource();
				HandleClick();
				state = 0;
			}
		}

	}

	
	
	public void startThread() {
		if(timer!=null) {
			timer.cancel();
			time =0;
		}
		timer = new Timer();
		timer.scheduleAtFixedRate(new MyTask(), 1000, 1000);;
		
	}
	
	public class MyTask extends TimerTask{

		@Override
		public void run() {
			time++;
			lblLeftTime.setText(time+"");

		}
			
	}

		

	public void HandleClick() {
		if (state == 0) {
				
		} else if (state == 1) {
			if (getPath(btn1, btn2)) {
				btn1.setEnabled(false);
				btn2.setEnabled(false);
				while(!pathList.isEmpty()) {
					pathList.getFirst().setMap(map);
					pathList.removeFirst().showyourself();
				}
				showMap();//显示路径
				
				Thread thread = new Thread() {
					public void run() {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						};
						clearPath();//清除路径
						map[btn1.getx()][btn1.gety()] = 0;
						map[btn2.getx()][btn2.gety()] = 0;
						showMap();
						System.out.println("寻路成功");
						if(isWin()) {
							isRun = false;
							timer.cancel();
							
							String name = JOptionPane.showInputDialog(this, "请输入姓名");

							if (name == null || "".equals(name.trim())) {
								name = "匿名";
							}
							hdb.insertInfo(name, time);
							time = 0;
							lblLeftTime.setText(time+"");
						}
					}
				};
				thread.start();


				
				/*clearPath();
				map[btn1.getx()][btn1.gety()] = 0;
				map[btn2.getx()][btn2.gety()] = 0;
				showMap();*/
				
				
			}
		}
	}

	public boolean isWin() {
		for (int i = 0; i < size + 2; i++) {
			for (int j = 0; j < size + 2; j++) {
				if (map[i][j] != 0)
					return false;
			}
		}
		return true;
	}
	
	public boolean isDead() {
		for (int i = 1; i < size + 1; i++)
			for (int j = 1; j < size + 1; j++)
				for (int m = 1; m < size + 1; m++)
					for (int n = 1; n < size + 1; n++) {
						if (map[i][j] == map[m][n] && map[i][j] != 0 && getPath(btnMap[i][j], btnMap[m][n]))
							return false;

					}

		return true;
	}

	public void resetMap() {
		Random ran = new Random();
		int len = size * size;
		int[] tmpArr = new int[len];
		int tmp1, rannum, tmp2, k = 0;
		for (int i = 1; i < size + 1; i++)
			for (int j = 1; j < size + 1; j++) {
				tmpArr[k] = map[i][j];
				k++;
			}

		while (len != 0) {
			rannum = ran.nextInt(len);
			tmp2 = tmpArr[len - 1];
			tmpArr[len - 1] = tmpArr[rannum];
			tmpArr[rannum] = tmp2;
			len--;
		}
		k = 0;
		for (int i = 1; i < size + 1; i++)
			for (int j = 1; j < size + 1; j++) {
				map[i][j] = tmpArr[k];
				k++;
			}

	}

	public void initMap() {
		
		map=new int[size+2][size+2];
		Random ran = new Random();
		int len = size * size;
		int[] tmpArr = new int[len];
		int tmp1, rannum, tmp2, k = 0;
		for (int i = 0; i < len - 1; i = i + 2) {
			tmp1 = ran.nextInt(lv) + 1;
			tmpArr[i] = tmp1;
			tmpArr[i + 1] = tmp1;
		}
		while (len != 0) {
			rannum = ran.nextInt(len);
			tmp2 = tmpArr[len - 1];
			tmpArr[len - 1] = tmpArr[rannum];
			tmpArr[rannum] = tmp2;
			len--;
		}
		for (int i = 1; i < size + 1; i++)
			for (int j = 1; j < size + 1; j++) {
				map[i][j] = tmpArr[k];
				k++;
			}

	}

	
	public void clearPath() {
		for (int i = 0; i < size + 2; i++) {
			for (int j = 0; j < size + 2; j++) {
				if(map[i][j]>IS_PATH)
					map[i][j] = 0;
		}
	}
	}
	
	
	public void showMap() {
		for (int i = 0; i < size + 2; i++) {
			for (int j = 0; j < size + 2; j++) {
				btnMap[i][j].getImg(map[i][j],size);
			}
		}
	}

	public void initGrade() {
		int n = cbLv.getSelectedIndex();
		if (n == 0 || n == 1) {
			lv = 6;
			size = 6;
		}
		else if (n == 2) {
			lv = 10;
			size = 8;
		}
	}
	
	public void init() {
		playPanel.removeAll();
		initGrade();
		initMap();


		playPanel.setLayout(new GridLayout(size + 2, size + 2,0,0));

		btnMap = new ImgButton[size + 2][size + 2];

		for (int i = 0; i < size + 2; i++) {
			for (int j = 0; j < size + 2; j++) {
				ImgButton btn = new ImgButton(i, j);
				if(i==0||j==0||i==size+1||j==size+1) {
					btn.setEnabled(false);
				}
				btnMap[i][j] = btn;
				playPanel.add(btn);
				btn.addActionListener(this);

			}
		}
		showMap();
	}


	public boolean getPath(ImgButton a, ImgButton b) {
		int ax = a.getx();
		int ay = a.gety();
		int bx = b.getx();
		int by = b.gety();
		if (map[ax][ay] == map[bx][by] && map[ax][ay] != 0) {
			if (ax == bx) {
				if (reach_y(ax, ay, by, true)) {
					pathList.clear();
					getPathy(ax, ay, bx, by);
					return true;
				}
			}
			if (ay == by) {
				if (reach_x(ay, ax, bx, true)) {
					pathList.clear();
					getPathx(ax, ay, bx, by);
					return true;
				}
			}
			int[] a_x = new int[size + 2];
			int[] b_x = new int[size + 2];
			int[] a_y = new int[size + 2];
			int[] b_y = new int[size + 2];
			for (int i = 0; i < size + 2; i++) {
				if (reach_x(ay, ax, i, false))
					a_x[i] = 1;
				if (reach_x(by, bx, i, false))
					b_x[i] = 1;
				if (reach_y(ax, ay, i, false))
					a_y[i] = 1;
				if (reach_y(bx, by, i, false))
					b_y[i] = 1;
			}

			if (reach_x(ay, bx, ax, true) && b_y[ay] == 1) {
				pathList.clear();
				getPathx(ax, ay, bx, ay);
				getPathy(bx, by, bx, ay);
				int forward = bx > ax ? (by > ay ? ONE : TWO) : (by > ay ? FOUR : THREE);
				pathList.add(new PathNode(bx, ay, forward));
				return true;
			}
			if (reach_y(ax, by, ay, true) && b_x[ax] == 1) {
				pathList.clear();
				getPathx(bx, by, ax, by);
				getPathy(ax, by, ax, ay);
				int forward = bx > ax ? (by > ay ? THREE : FOUR) : (by > ay ? TWO : ONE);
				pathList.add(new PathNode(ax, by, forward));
				return true;
			}
			LinkedList<PathLength> pathLengthList = new LinkedList<PathLength>();
			for (int i = 0; i < size + 2; i++) {
				if (b_x[i] == 1 && a_x[i] == 1 && reach_y(i, ay, by, true)) {
					pathLengthList.add(new PathLength(i, ay, i, by, Math.abs(ax - i) + Math.abs(bx - i)));
				}
				if (b_y[i] == 1 && a_y[i] == 1 && reach_x(i, ax, bx, true)) {
					pathLengthList.add(new PathLength(ax, i, bx, i, Math.abs(ay - i) + Math.abs(by - i)));
				}
			}
			if(pathLengthList.isEmpty())
				return false;
			pathLengthList.sort(new Comparator<PathLength>() {

				@Override
				public int compare(PathLength o1, PathLength o2) {
					if (o1.getLength() == o2.getLength())
						return 0;
					else if (o1.getLength() < o2.getLength())
						return -1;
					else 
						return 1;
				}
			});
			PathLength shortPath = pathLengthList.getFirst();
			int x1=shortPath.getAx();
			int x2=shortPath.getBx();
			int y1=shortPath.getAy();
			int y2=shortPath.getBy();
			
			if(x1==x2) {
				pathList.clear();
				getPathy(x1,y1,x2,y2);
				getPathx(x1,ay,ax,ay);
				getPathx(x1,by,bx,by);
				int forwardA = ax > x1 ? (ay > by ? THREE :FOUR) : (ay > by ? TWO : ONE);
				int forwardB = bx > x1 ? (by > ay ? THREE :FOUR) : (by > ay ? TWO : ONE);
				pathList.add(new PathNode(x1, ay, forwardA));
				pathList.add(new PathNode(x1, by, forwardB));
				return true;
			}
			if(y1==y2) {
				pathList.clear();
				getPathx(x1,y1,x2,y2);
				getPathy(ax,y1,ax,ay);
				getPathy(bx,y1,bx,by);
				int forwardA = ay > y1 ? (ax > bx ? ONE : FOUR) : (ax > bx ? TWO : THREE);
				int forwardB = by > y1 ? (bx > ax ? ONE : FOUR) : (bx > ax ? TWO : THREE);
				pathList.add(new PathNode(ax, y1, forwardA));
				pathList.add(new PathNode(bx, y1, forwardB));
				return true;
			}
					
		} else {
			return false;
		}
		return false;

	}

	public void getPathx(int ax, int ay, int bx, int by) {
		for (int i = Integer.min(ax, bx); i < Integer.max(ax, bx); i++)
			if (Integer.min(ax, bx) != i)
				pathList.add(new PathNode(i, ay, X));
	}

	public void getPathy(int ax, int ay, int bx, int by) {
		for (int i = Integer.min(ay, by); i < Integer.max(ay, by); i++)
			if (Integer.min(ay, by) != i)
				pathList.add(new PathNode(ax, i, Y));
	}

	public boolean reach_x(int y, int x1, int x2, boolean b) {
		// b false 右短点为0 true 右短点都可
		if (map[x2][y] == 0 || b) {
			if (Math.abs(x1 - x2) == 1)
				return true;
			int xmin = Integer.min(x1, x2);
			int xmax = Integer.max(x1, x2);
			for (int i = xmin + 1; i < xmax; i++) {
				if (map[i][y] == 0) {
					if (i == xmax - 1)
						return true;
				} else {
					break;
				}
			}
		}
		return false;
	}

	public boolean reach_y(int x, int y1, int y2, boolean b) {
		if (map[x][y2] == 0 || b) {
			if (Math.abs(y1 - y2) == 1)
				return true;
			int ymin = Integer.min(y1, y2);
			int ymax = Integer.max(y1, y2);
			for (int i = ymin + 1; i < ymax; i++) {
				if (map[x][i] == 0) {
					if (i == ymax - 1)
						return true;
				} else {
					break;
				}
			}

		}
		return false;
	}

	public MainFrame() {
		

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 881, 695);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		playPanel.setBounds(21, 25, 600, 600);
		contentPane.add(playPanel);
		playPanel.setBackground(Color.BLACK);

		btnOrder = new JButton("排行榜");
		btnOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnOrder.setBounds(708, 330, 93, 23);
		contentPane.add(btnOrder);
		btnOrder.addActionListener(this);

		JButton btnMusic = new JButton("音乐开关");
		btnMusic.setBounds(708, 388, 93, 23);
		contentPane.add(btnMusic);

		btnStart = new JButton("\u5F00\u59CB\u6E38\u620F");
		btnStart.setBounds(708, 497, 93, 23);
		contentPane.add(btnStart);
		
		JLabel label = new JLabel("\u5DF2\u7528\u65F6\u95F4");
		label.setForeground(Color.RED);
		label.setBounds(662, 275, 54, 15);
		contentPane.add(label);
		
		lblLeftTime = new JLabel("0");
		lblLeftTime.setForeground(Color.BLUE);
		lblLeftTime.setBounds(760, 275, 54, 15);
		contentPane.add(lblLeftTime);
		
		String[] strGrade = { "选择难度", "6+6x6", "10+8x8" };
		
		cbLv = new JComboBox(strGrade);
		cbLv.setBounds(708, 452, 93, 21);
		contentPane.add(cbLv);
		btnStart.addActionListener(this);
	}
}
