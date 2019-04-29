package llk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import llk.entity.TimeOrder;

public class HandleDB {
	
	private Connection conn = null;
	private PreparedStatement stat = null;
	private ResultSet rs = null;
	
	public void insertInfo(String name,int time) {
		conn = DBUtil.getConn();
		
		String sql = "select count(*) from timeorder where [time]<="+time;
		try {
			stat = conn.prepareStatement(sql);
			rs = stat.executeQuery();
			if(rs.next()) {
				int n = rs.getInt(1);
				if(n<=4) {
					sql = "insert into timeorder values('"+name+"',"+time+")";
					stat = conn.prepareStatement(sql);
					stat.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public ArrayList selectInfo() {
		
		ArrayList<TimeOrder> al = new ArrayList<TimeOrder>();
		
		String sql = "select top 5 * from timeorder order by [time] asc";
		
		conn = DBUtil.getConn();
		try {
			stat = conn.prepareStatement(sql);
			rs = stat.executeQuery();
			while(rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2);
				int time = rs.getInt(3);
				
				TimeOrder to = new TimeOrder(id,name,time);
				al.add(to);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return al;
	}

	public static void main(String[] args) {
		HandleDB hdb = new HandleDB();
		hdb.insertInfo("a4", 1998);

	}

}
