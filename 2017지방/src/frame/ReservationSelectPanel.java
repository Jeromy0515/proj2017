package frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


public class ReservationSelectPanel extends JPanel{
	
	private JTextField idField;
	private DefaultTableModel model;
	private JTable table;
	private JScrollPane scrollPane;
	JPanel flowPanel;
	
	ReservationSelectPanel() {
		
		setLayout(new BorderLayout());
		
		idField = new JTextField(10);
		
		model = new DefaultTableModel(null, "성명,예약일,차량번호,좌석번호,운임금액,발권상태".split(","));
		table = new JTable(model);
		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(600,300));
		JLabel mainLabel = BaseFrame.createMainLabel(new JLabel("고객티켓예약조회"), new Font("굴림",1,20));
		mainLabel.setHorizontalAlignment(JLabel.LEFT);
		
		flowPanel = new JPanel();
		flowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		flowPanel.add(BaseFrame.createLabel(new JLabel("고객ID"), null));
		flowPanel.add(idField);
		flowPanel.add(BaseFrame.createButton("조회", e->showTicket()));
		
		add(mainLabel,BorderLayout.NORTH);
		add(flowPanel,BorderLayout.CENTER);
		
	}
	
	
	
	
	
	
	
	private void showTicket() {
		
		try (ResultSet rs = BaseFrame.getSqlResults("select c.cName, t.bDate, t.bNumber, t.bSeat, t.bPrice, t.bState from tbl_ticket as t left join tbl_customer as c on c.cID = t.cID where t.cID=?;", idField.getText())){
			while(rs.next()) {
				model.addRow(new Object[] {rs.getString("cName"),rs.getString("bDate"),rs.getString("bNumber"),rs.getString("bSeat"),rs.getString("bPrice"),rs.getString("bState")});
			}
			model.getValueAt(0, 0);	
			
			flowPanel.add(scrollPane);
			revalidate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}finally {
			BaseFrame.pstClose();
		}
		
	}
	
	
}
