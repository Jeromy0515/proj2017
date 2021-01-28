package frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CarInformPanel extends JPanel{
	private JTable table;
	private DefaultTableModel model;
	
	CarInformPanel(MenuFrame frame) {
		frame.setHVGap(20, 20);
		model = new DefaultTableModel(null,"차량번호,출발지,도착지,첫차시간,소요시간,운임횟수,운임금액".split(","));
		model = BaseFrame.setTable(model,"select * from tbl_bus","bNumber,bDeparture,bArrival,vTime,bElapse,vCount,bPrice".split(","));
		table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(700,500));
		add(scrollPane,BorderLayout.CENTER);
	}
	
}
