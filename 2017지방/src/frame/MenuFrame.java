package frame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuFrame extends BaseFrame{
	
	private CardLayout card = new CardLayout();
	private JPanel centerPanel = new JPanel();
	
	 MenuFrame() {
		 
		 super("고속버스예매 프로그램",800,700);
		 
		 JPanel labelPanel = createComponent(new JPanel(), 800, 50);
		 labelPanel.add(createLabel(new JLabel("고속버스예매 프로그램"), new Font("굴림",1,30)));
		 JPanel northPanel = createComponent(new JPanel(), 800, 100);
		 
		 northPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 10, 0, Color.BLACK));
		 northPanel.setLayout(new FlowLayout());
		 northPanel.add(labelPanel);
		 northPanel.add(createButton(new JButton("배차차량조회"), e->changePanel("CarInform")));
		 northPanel.add(createButton(new JButton("예약신청"), e -> changePanel("Reserve")));
		 northPanel.add(createButton(new JButton("차량좌석조회"), e->changePanel("SeatSelect")));
		 northPanel.add(createButton(new JButton("승차권발매"), e->changePanel("Buy")));
		 northPanel.add(createButton(new JButton("예약조회"), null));
		 
		 centerPanel.setLayout(card);
		 centerPanel.add("Blank",new JPanel()); // blank Panel
		 
		 CarInformPanel carInformPanel = new CarInformPanel(this); //배차차량조회
		 TicketBuyPanel ticketBuyPanel = new TicketBuyPanel(); //승차권발매
		 SeatSelectPanel seatSelectPanel = new SeatSelectPanel(this); //차량좌석조회
		 ReservePanel reservePanel = new ReservePanel(this,seatSelectPanel); //예약신청
		 
		 centerPanel.add("CarInform", carInformPanel);
		 centerPanel.add("Reserve", reservePanel);
		 centerPanel.add("SeatSelect", seatSelectPanel);
		 centerPanel.add("Buy",ticketBuyPanel);
//		 centerPanel.add("CarSelect",new CarSelectPanel(this));
//		 centerPanel.add("Reserve",new ReservePanel(this));
//		 centerPanel.add("SeatSelect",new SeatSelectPanel(this));
		 
		 add(northPanel,BorderLayout.NORTH);
		 add(centerPanel,BorderLayout.CENTER);
	 }
	 
	 public void changePanel(String text) {
		 card.show(centerPanel, text);
	 }
//	 public void changePanel() {
//		 card.next(centerPanel);		 
//	 }
	 
	 public CardLayout getCardLayout() {
		 return card;
	 }
	
	 public JPanel getPanel() {
		 return centerPanel;
	 }
	 
	 public void setHVGap(int H,int V) {
		 card.setHgap(H);
		 card.setVgap(V);
	 }
	 
}
