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
import javax.swing.table.DefaultTableModel;

public class SeatSelectPanel extends JPanel{
	private JTextField carNumField,carNumField2,dDayField,dDayField2,hoChaField,seatNumField,idField;
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
		
		table.setRowHeight(30);
		scrollPane.setPreferredSize(new Dimension(600,300));
		BaseFrame.setTableColumnWidth(table,new int[]{80,5,80,5,5,80,5,80,5});
		BaseFrame.tableAlign(table);
		
		
		carNumField = new JTextField(10);
		dDayField = new JTextField(10);
		hoChaField = new JTextField(10);

		centerPanel = new JPanel();
		centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		centerPanel.add(BaseFrame.createLabel(new JLabel("차량번호"), null));
		centerPanel.add(carNumField);
		centerPanel.add(BaseFrame.createLabel(new JLabel("출발일자"), null));
		centerPanel.add(dDayField);
		centerPanel.add(BaseFrame.createLabel(new JLabel("호차번호"),null));
		centerPanel.add(hoChaField);
		centerPanel.add(BaseFrame.createButton("좌석조회", e->showSeat()));
		
		carNumField2 = new JTextField(10);
		dDayField2 = new JTextField(10);
		seatNumField = new JTextField(10);
		idField = new JTextField(10);
				
		idField.setText(LoginFrame.id);
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		southPanel.add(BaseFrame.createLabel(new JLabel("출발일자"), null));
		southPanel.add(dDayField2);
		southPanel.add(BaseFrame.createLabel(new JLabel("차량번호"), null));
		southPanel.add(carNumField2);
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
		carNumField2.setText(carNumField.getText());
		
		dDayField2.setEnabled(false);
		carNumField2.setEnabled(false);
	}
	
	
	
	
	
	
	
	
	
	
	private void checkReservedSeat() {
		ArrayList<Integer> getReservedSeat = new ArrayList<Integer>();
		try (ResultSet rs = BaseFrame.getSqlResults("select bSeat from tbl_ticket where bNumber=? and bNumber2=? and bDate=?",carNumField.getText(),hoChaField.getText(),dDayField.getText() )){
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
		setUnabledField();
		checkReservedSeat();
		centerPanel.add(scrollPane);
		revalidate();
//		repaint();

	}
	
	
	
	
	
	
	
	private void showSeat() {
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
		
		String reservedSeat = "";
		String overlapSeat = "";
		String getSeatNums = "";
		int nextCnt = 0;
		int overlapCnt = 0;
		if(seatNumField.getText().equals("")) {
			BaseFrame.informMessage("좌석번호를 입력해주세요", "메시지");
			return;
		}else{
			try {
				for(String seatNums:seatNumField.getText().split(", |,| ")) {
					for(String arr:getSeatNums.split("")) {
						if(arr.equals(seatNums)) {
							overlapCnt++;
							if(overlapCnt > 1) {
								overlapSeat += ", "+seatNums;
							}else {
								overlapSeat += seatNums;
							}
							break;
						}
						getSeatNums += seatNums;
					}
					Integer.parseInt(seatNums);// 좌석정보에 숫자만 입력했는지 체크 숫자외 다른 문자 입력시 catch에서 잡아냄
					try (ResultSet rs = BaseFrame.getSqlResults("select * from tbl_ticket where bDate=? and bNumber=? and bSeat=?",dDayField2.getText(), carNumField2.getText(),seatNums)){
						if(rs.next()) {
							nextCnt++;
							if(nextCnt>1) {
								reservedSeat += ", "+seatNums;
							}else {
								reservedSeat += seatNums;
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}finally {
						BaseFrame.pstClose();
					}
				}
				
				if(overlapCnt > 0) {
					BaseFrame.warningMessage("좌석번호 "+overlapSeat+"이(가) 중복입력되었습니다.","메시지");
				}
				
				
				if(nextCnt > 0) {
					BaseFrame.warningMessage("좌석번호 "+reservedSeat+"은 이미 예약되어 있는 좌석입니다.", "메시지");
					return;
				}
				
			}catch (Exception e) {
				BaseFrame.informMessage("올바르지 못한 좌석정보입니다.", "메시지");
				return;
			}
		}
		
		if(BaseFrame.Yes_No_Dialog("차량번호["+carNumField2.getText()+"]\n예약일자["+dDayField2.getText()+"]\n좌석번호["+seatNumField.getText()+"]\n고객번호["+idField.getText()+"]\n예약하시겠습니까?", "웹 페이지 메시지") == 0) {
			try {
				for(String seatNums:seatNumField.getText().split(", |,| ")) {
					BaseFrame.withoutSqlResult("insert into tbl_ticket values(?,?,?,?,?,(select bPrice from tbl_bus where bNumber=?),?)" ,dDayField.getText(),carNumField.getText(),hoChaField.getText(),seatNums,idField.getText(),carNumField.getText(),"X");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			BaseFrame.warningMessage("차량번호["+carNumField2.getText()+"]\n예약일자["+dDayField2.getText()+"]\n좌석번호["+seatNumField.getText()+"]\n고객번호["+idField.getText()+"]\n예약되었습니다.", "웹 페이지 메시지");
		}
		
		
		
		
		
		
		
		
		
	}
	
	
	
	
	
	public JTextField getCarNumField() {
		return carNumField;
	}
	
	
	
	
	
	
	
	
	
	public JTextField getdDayField() {
		return dDayField;
	}
	
	
	
	
	
	
	
	public JTextField getHoChaField() {
		return hoChaField;
	}
	
	
}
