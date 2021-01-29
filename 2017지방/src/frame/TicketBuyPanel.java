package frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		
		scrollPane.setPreferredSize(new Dimension(600,205));
		BaseFrame.tableAlign(table);
		BaseFrame.setTableColumnWidth(table, new int[] {200,200,200});
		
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
		if(dDayField.getText().equals("") || carNumField.getText().equals("") || busNumField.getText().equals("") || idField.getText().equals("")) {
			BaseFrame.warningMessage("발권할 승차권의 데이터를 입력해주시기 바랍니다.", "웹 페이지 메시지");
		}else {
			
			try (ResultSet rs = BaseFrame.getSqlResults("select t.bPrice, b.bDeparture, b.bArrival,t.bDate from tbl_ticket as t left join tbl_bus as b on b.bNumber = t.bNumber left join tbl_customer as c on c.cID = t.cID where t.bDate=? and t.bNumber=? and t.bNumber2=? and t.cID = ?;", dDayField.getText(),carNumField.getText(),busNumField.getText(),idField.getText())){
				
				if(rs.next()) {
					
					if(busNumField.equals("1호차")) {
						table.setValueAt(rs.getString("bDeparture"), 1, 0);
						table.setValueAt(rs.getString("bArrival"), 1, 2);
					}else {
						table.setValueAt(rs.getString("bArrival"), 1, 0);
						table.setValueAt(rs.getString("bDeparture"), 1, 2);
					}
					int price = rs.getInt("bPrice");
					table.setValueAt(price, 3, 0);
					if(idField.getText().equals("비회원")) {
						table.setValueAt(0, 3, 1);
					}else {
						table.setValueAt(price/10, 3, 1);
					}
					table.setValueAt(price/10, 3, 1);
					table.setValueAt(price-price*(1/10), 3, 2);
					table.setValueAt(rs.getString("bDate"), 6, 0);
					table.setValueAt(dDayField.getText(), 6, 2);
				}else {
					BaseFrame.warningMessage("조회한 정보가 존재하지 않습니다.", "웹 페이지 메시지");
					return;
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				BaseFrame.pstClose();
			}
			BaseFrame.warningMessage("승차권이 정상적으로 발권되었습니다.", "웹 페이지 메시지");
			add(scrollPane,BorderLayout.CENTER);
			revalidate();
		}

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
