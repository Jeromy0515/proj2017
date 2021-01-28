package frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class SeatSelectPanel extends JPanel{
	private JTextField busNumField,busNumField2,dDayField,dDayField2,hoChaField,seatNumField,idField;
	private JPanel centerPanel;
	private DefaultTableModel model;
	private JTable table;
	private JScrollPane scrollPane;
	public SeatSelectPanel(MenuFrame frame) {
		frame.setHVGap(0, 0);
		setLayout(new BorderLayout());
		Object setIndex[][] = {{"1","","2","","ㅡ","3","","4",""},{"5","","6","","ㅡ","7","","8",""},
				{"9","","10","","ㅡ","11","","12",""},{"13","","14","","ㅡ","15","","16",""},
				{"17","","18","","ㅡ","19","","20",""}};
		model = new DefaultTableModel(setIndex,"좌측창가,예약,좌측통로,예약,통로,우측창가,예약,우측통로,예약".split(","));
		table = new JTable(model);
		scrollPane = new JScrollPane(table);
		
		busNumField = new JTextField(10);
		dDayField = new JTextField(10);
		hoChaField = new JTextField(10);

		centerPanel = new JPanel();
		centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		centerPanel.add(BaseFrame.createLabel(new JLabel("차량번호"), null));
		centerPanel.add(busNumField);
		centerPanel.add(BaseFrame.createLabel(new JLabel("출발일자"), null));
		centerPanel.add(dDayField);
		centerPanel.add(BaseFrame.createLabel(new JLabel("호차번호"),null));
		centerPanel.add(hoChaField);
		centerPanel.add(BaseFrame.createButton("좌석조회", e->selectSeat()));
		
		busNumField2 = new JTextField(10);
		dDayField2 = new JTextField(10);
		seatNumField = new JTextField(10);
		idField = new JTextField(10);
				
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		southPanel.add(BaseFrame.createLabel(new JLabel("출발일자"), null));
		southPanel.add(dDayField2);
		southPanel.add(BaseFrame.createLabel(new JLabel("차량번호"), null));
		southPanel.add(busNumField2);
		southPanel.add(BaseFrame.createLabel(new JLabel("좌석번호"), null));
		southPanel.add(seatNumField);
		southPanel.add(BaseFrame.createLabel(new JLabel("회원ID"), null));
		southPanel.add(idField);
		southPanel.add(BaseFrame.createButton("예약", e->reserveSeat()));
		
		add(BaseFrame.createMainLabel(new JLabel("좌석표 선택"), new Font("굴림",1,20)),BorderLayout.NORTH);
		add(centerPanel,BorderLayout.CENTER);
		add(southPanel,BorderLayout.SOUTH);
		
	}
	
	
	
	
	
	
	
	
	
	private void setUnabledField() {
		dDayField2.setText(dDayField.getText());
		busNumField2.setText(busNumField.getText());
		
		dDayField2.setEnabled(false);
		busNumField2.setEnabled(false);
	}
	
	
	
	
	
	
	
	
	
	
	private void checkReservedSeat() {
		ArrayList<Integer> getReservedSeat = new ArrayList<Integer>();
		try (ResultSet rs = BaseFrame.getSqlResults("select bSeat from tbl_ticket where bNumber=? and bNumber2=? and bDate=?",busNumField.getText(),hoChaField.getText(),dDayField.getText() )){
			while(rs.next()) {
				getReservedSeat.add(rs.getInt("bSeat"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			BaseFrame.pstClose();
		}
		while(getReservedSeat.size() > 0) {
			for(int i=0;i<table.getRowCount();i++) {
				for(int j=0;j<table.getColumnCount();j+=2) {
					try {
						if(Integer.parseInt(table.getModel().getValueAt(i, j).toString()) == getReservedSeat.get(0)) {
						table.getModel().setValueAt("O", i, j+1);
						getReservedSeat.remove(0);
						}
					} catch (Exception e) { // ㅡ(통로)를 만났을때 j에 1을 뺴주고 2를 더해서 제자리를 찾게함
						j--;
					}

				}
			}
		}
	}
	
	
	
	
	
	
	
	
	private void setTableVisible(){
		scrollPane.setPreferredSize(new Dimension(600,300));
		BaseFrame.setTableColumnWidth(table,new int[]{80,5,80,5,5,80,5,80,5});
		BaseFrame.tableAlign(table);
		setUnabledField();
		checkReservedSeat();
		centerPanel.add(scrollPane);
		revalidate();
//		repaint();

	}
	
	
	
	
	
	
	
	private void selectSeat() {
		setTableVisible();
	}
	
	
	
	
	
	
	
	
	
	
	private ArrayList<Integer> changeSeatFieldTextToInteger() {
		String getSeatFieldText[] = seatNumField.getText().split(", |,| ");
		int seatNum[] = new int[getSeatFieldText.length];
		boolean overlapCheck = false;
		ArrayList<Integer> overlapNumList = new ArrayList<Integer>();
		for(int i=0;i<getSeatFieldText.length;i++) {
			seatNum[i] = Integer.parseInt(getSeatFieldText[i]);
				for(int j=0;j<i;j++) {
					if(seatNum[i] == seatNum[j]){
						overlapNumList.add(seatNum[i]);
					}
				}
		}
		return overlapNumList;
	}
	
	
	
	
	
	
	private void ser() {
		String getSeatFieldText[] = seatNumField.getText().split(", |,| ");
		int num[] = new int[getSeatFieldText.length];
		boolean check[] = new boolean[getSeatFieldText.length];
		for(int i=0;i<num.length;i++) {
			num[i] = Integer.parseInt(getSeatFieldText[i]);
		}
		
	}
	
	
	
	
	
	
	
	
	
	private void reserveSeat() {
		if(seatNumField.getText().equals("")) {
			BaseFrame.informMessage("좌석번호를 입력해주세요", "메시지");
		}else{
			try {
				BaseFrame.withoutSqlResult("select ", busNumField2.getText(),dDayField2.getText(),seatNumField.getText().split(", |,| "));
				
				
			}catch (Exception e) {
				BaseFrame.informMessage("올바르지 못한 좌석정보입니다.", "메시지");
			}
		}
		
	}
	
	
	
	
	
	public JTextField getBusNumField() {
		return busNumField;
	}
	
	
	
	
	
	
	
	
	
	public JTextField getdDayField() {
		return dDayField;
	}
	
	
	
	
	
	
	
	public JTextField getHoChaField() {
		return hoChaField;
	}
	
	
}
