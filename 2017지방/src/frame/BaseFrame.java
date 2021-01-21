package frame;

import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class BaseFrame extends JFrame{
	
	static Connection connection;
	PreparedStatement pst;
	static {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/sw3_Setting?serverTimezone=UTC","user","1234");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public BaseFrame(String title,int width,int height) {
		setSize(width,height);
		setTitle(title);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public BaseFrame() {
	}
	
	public static <T extends JComponent> T createComponent(T comp,int x,int y,int width,int height){
		comp.setBounds(x, y, width, height);
		return comp;
	}
	
	public static int ok_cancelDialog(String caption,String title) {
		return JOptionPane.showConfirmDialog(null, caption,title,JOptionPane.OK_CANCEL_OPTION);
	}
	
	public static void informMessage(String caption,String title) {
		JOptionPane.showMessageDialog(null, caption,title,JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void errorMessage(String caption,String title) {
		JOptionPane.showMessageDialog(null, caption,title,JOptionPane.ERROR_MESSAGE);
	}
	
	public static JButton createButton(String text,ActionListener act) {
		JButton button = new JButton(text);
		button.addActionListener(act);
		return button;
	}
	
	
	public ResultSet getSqlResults(String sql,Object...objects) {
		ResultSet rs = null;
		try {
			pst = connection.prepareStatement(sql);
			for(int i=0;i<objects.length;i++) {
				pst.setObject(i+1, objects[i]);
			}
			rs = pst.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	
	public static JComboBox<String> createCoboBox(JComboBox<String> box, String sql, String col){
		box = new JComboBox<>();
		ResultSet rs = null;
		try (PreparedStatement pst = connection.prepareStatement(sql)){
			rs = pst.executeQuery();
			while(rs.next()) {
				box.addItem(rs.getString(col));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return box;
	}
}
