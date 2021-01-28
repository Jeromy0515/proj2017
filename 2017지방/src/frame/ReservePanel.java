package frame;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.ResultSet;
import java.util.Calendar;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class ReservePanel extends JPanel{
	JComboBox<String> busNumBox,busHoChaBox,yearBox,monthBox,dayBox;
	SeatSelectPanel seatSelectPanel;
	MenuFrame frame;
	ReservePanel(MenuFrame f,SeatSelectPanel p) {
		setLayout(new BorderLayout());
		frame = f;
		frame.setHVGap(0, 0);
		
		seatSelectPanel = p;
		
		JPanel flowPanel = new JPanel();
		flowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		busNumBox = BaseFrame.createComboBox(busNumBox, "SELECT bNumber FROM tbl_bus","bNumber");
		busHoChaBox = BaseFrame.createComboBox(busHoChaBox, "SELECT bNumber2 FROM tbl_ticket GROUP BY bNumber2","bNumber2");		
		yearBox = BaseFrame.createComboBox(yearBox, "select date_format(now(),'%Y') AS year from tbl_ticket group by year", "year");
		monthBox = new JComboBox<>();
		setMonthBox();
		dayBox = new JComboBox<>();
		dayBox.insertItemAt("==일 선택==", 0);
		dayBox.setSelectedIndex(0);
		monthBox.addActionListener(e->setDayBox());
		
		flowPanel.add(BaseFrame.createLabel(new JLabel("차량정보:"), null));
		
		flowPanel.add(busNumBox);
		flowPanel.add(busHoChaBox);
		flowPanel.add(yearBox);
		flowPanel.add(monthBox);
		flowPanel.add(dayBox);
		flowPanel.add(BaseFrame.createButton("좌석조회", e->seatSelect()));
		flowPanel.add(BaseFrame.createButton("메인으로", e-> frame.changePanel("Blank")));
		
		add(BaseFrame.createMainLabel(new JLabel("배차차량조회 및 예약하기"), new Font("굴림",1,20)),BorderLayout.NORTH);
		add(flowPanel,BorderLayout.CENTER);
	}
	
	private void seatSelect() {
		if(monthBox.getSelectedIndex() == 0 || dayBox.getSelectedIndex() == 0) {
			BaseFrame.informMessage("월 또는 일을 선택하여 주십시오.", "메시지");
			return;
		}
		BaseFrame.warningMessage("예약이 시작됩니다.", "웹 페이지 메시지");
		seatSelectPanel.getBusNumField().setText((String)busNumBox.getSelectedItem());
		seatSelectPanel.getdDayField().setText((String)yearBox.getSelectedItem()+"-"+monthBox.getSelectedItem()+"-"+dayBox.getSelectedItem());
		seatSelectPanel.getHoChaField().setText((String)busHoChaBox.getSelectedItem());
		frame.changePanel("SeatSelect");
	}
	
	private void setDayBox() {
		dayBox.removeAllItems();
		dayBox.insertItemAt("==일 선택==", 0);
		dayBox.setSelectedIndex(0);
//		for(int i=1;i<=3;i++) {
			if(monthBox.getSelectedIndex() == 0)
				return;
			try (ResultSet rs = BaseFrame.getSqlResults("select date_format(bdate, '%d') as day from tbl_ticket where date_format(bdate,'%d') between date_format(last_day(now() - interval 1 month)+ interval 1 day ,'%d') and date_format(last_day(now() + interval ? month), '%d') group by day order by day asc;", monthBox.getSelectedIndex()-1)){
				while(rs.next()) {
					dayBox.addItem(rs.getString("day"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
//		}
	}
	
	private void setMonthBox() {
		for(int i=0;i<3;i++) {
		try (ResultSet rs = BaseFrame.getSqlResults("select date_format(DATE_ADD(NOW(), INTERVAL ? MONTH),'%m') AS month;", i)){
				while(rs.next()) {
					monthBox.addItem(rs.getString("month"));
				}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			BaseFrame.pstClose();
		}
		}
		monthBox.insertItemAt("==월 선택==", 0);
		monthBox.setSelectedIndex(0);
	}
	
	
}
