package frame;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.TargetDataLine;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class BaseFrame extends JFrame{
	
	static Connection connection;
	static PreparedStatement pst = null;
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
	
	
	
	
	
	
	public static <T extends JComponent> T createComponent(T comp, int width,int height) {
		comp.setPreferredSize(new Dimension(width, height));
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
	
	
	
	
	
	public static void warningMessage(String caption,String title) {
		JOptionPane.showMessageDialog(null, caption,title,JOptionPane.WARNING_MESSAGE);
	}
	
	
	
	
	
	
	public static JLabel createLabel(JLabel label,Font font) {
		label.setFont(font);
		return label;
	}
	
	
	
	
	
	
	public static JLabel createMainLabel(JLabel label, Font font) {
		label.setFont(font);
		label.setHorizontalAlignment(JLabel.LEFT);
		return label;
	}
	
	
	
	
	
	
	public static <T extends AbstractButton> T createButton(T button,ActionListener act) {
		button.addActionListener(act);
		return button;
	}
	
	
	
	
	
	
	public static JButton createButton(String text, ActionListener act) {
		JButton button = new JButton(text);
		button.addActionListener(act);
		return button;
	}
	
	
	
	
	
	
	public static JTable tableAlign(JTable table) {
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		for(int i=0;i<table.getColumnModel().getColumnCount();i++) {
			table.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}
		return table;
	}
	
	
	
	
	
	
	public static JTable setTableColumnWidth(JTable table, int...width) {
		for(int i=0;i<table.getColumnModel().getColumnCount();i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(width[i]);
		}
		return table;
	}
	
	
	
	
	
	
	public static ResultSet getSqlResults(String sql,Object...objects) {
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
	
	
	
	
	
	public static void pstClose() {
		try {
			if(pst != null)
				pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
/*	public <T> List<T> getSqlResults(String sql,getResultSet<T> consumer,Object...objects){
		ResultSet rs = null;
		List<T> result = new ArrayList<T>();
		try (PreparedStatement pst = connection.prepareStatement(sql)){
			for(int i=0;i<objects.length;i++)
				pst.setObject(i+1, objects[i]);
			rs = pst.executeQuery();
			while(rs.next()) {
				result.add(consumer.consume(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	*/
	
	public static void withoutSqlResult(String sql,Object...objects) {
		try {
			pst = connection.prepareStatement(sql);
			for(int i=0;i<objects.length;i++) {
				pst.setObject(i+1, objects[i]);
			}
			pst.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			pstClose();
		}
	}
	
	
	
	
	
	
	public static DefaultTableModel setTable(DefaultTableModel model,String sql,String...cols) {
		model.setNumRows(0);
		try(ResultSet rs = BaseFrame.getSqlResults(sql)){
			Object data[] = new Object[cols.length];
			while(rs.next()) {
				for(int i=0;i<data.length;i++) {
					data[i] = rs.getObject(cols[i]);
				}
				model.addRow(data);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			pstClose();
		}
		
		return model;
	}
	
	
	
	
	
	
	public static <T>JComboBox<T> createComboBox(JComboBox<T> box, String sql, String col){
		box = new JComboBox<>();
		ResultSet rs = null;
		try (PreparedStatement pst = connection.prepareStatement(sql)){
			rs = pst.executeQuery();
			while(rs.next()) {
				box.addItem((T) rs.getObject(col));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return box;
	}
	
	
	
}
