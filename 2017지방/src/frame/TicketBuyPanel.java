package frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class TicketBuyPanel extends JPanel{
	 JTextField dDayField, carNumField, busNumField, idField;
	private DefaultTableModel model;
	private JTable table;
	private JScrollPane scrollPane; 
	
	TicketBuyPanel() {
		setLayout(new FlowLayout());
		Object setIndex[][] = {{"","",""},{"","",""},{"운행요금","할인요금","영수액"},{"","",""},
				{"","",""},{"발권날짜","","출발일자"},{"","",""}};
		
		model  = new DefaultTableModel(setIndex,"출발지,→→→,도착지".split(","));
		table = new JTable(model);
		table.setRowHeight(26);
		scrollPane = new JScrollPane(table);
		
		dDayField = new JTextField(10);
		carNumField = new JTextField(10);
		busNumField = new JTextField(10);
		idField = new JTextField(10);
		
		JPanel flowPanel = new JPanel();
		flowPanel.setLayout(new FlowLayout());
		flowPanel.add(BaseFrame.createLabel(new JLabel("출발일자"),null));
		flowPanel.add(dDayField);
		flowPanel.add(BaseFrame.createLabel(new JLabel("차량번호"), null));
		flowPanel.add(carNumField);
		flowPanel.add(BaseFrame.createLabel(new JLabel("버스번호"), null));
		flowPanel.add(busNumField);
		flowPanel.add(BaseFrame.createLabel(new JLabel("회원ID"), null));
		flowPanel.add(idField);
		flowPanel.add(BaseFrame.createButton("조회", e->buyTicket()));
		
		add(flowPanel,BorderLayout.NORTH);
		
	}
	
	
	
	private void setTableVisible() {
		scrollPane.setPreferredSize(new Dimension(600,205));
		BaseFrame.tableAlign(table);
		BaseFrame.setTableColumnWidth(table, new int[] {200,200,200});
		JPanel centerPanel = new JPanel();
		centerPanel.add(scrollPane);
		add(scrollPane,BorderLayout.CENTER);
		revalidate();
	}
	
	
	
	
	
	private void buyTicket() {
		setTableVisible();
	}
	
	
	
	
	
	private JTextField getdDayField() {
		return dDayField;
	}
	
	
	
	
	private JTextField getCarNumField() {
		return carNumField;
	}
	
	
	
	private JTextField getBusNumField() {
		return busNumField;
	}
	
	
}
