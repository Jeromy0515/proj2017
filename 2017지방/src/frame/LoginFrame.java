 package frame;

import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.ResultSet;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class LoginFrame extends BaseFrame{
	private JTextField idField;
	private JPasswordField pwField;
	public static boolean division = true;
	LoginFrame() {
		super("로그인",300,180);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		idField = new JTextField(10);
		pwField = new JPasswordField(10);
		
		JRadioButton memberBtn = createButton(new JRadioButton("회원 로그인",true), e->radioBtnAct("", true));
		JRadioButton nonMemberBtn = createButton(new JRadioButton("비회원 로그인"), e->radioBtnAct("비회원", false));
		ButtonGroup group = new ButtonGroup();
		
		group.add(memberBtn);
		group.add(nonMemberBtn);
		JPanel northPanel = createComponent(new JPanel(), 300, 25);
		northPanel.setLayout(new FlowLayout());
		northPanel.add(memberBtn);
		northPanel.add(nonMemberBtn);
		
		JPanel centerPanel = createComponent(new JPanel(), 300, 10);
		centerPanel.add(createLabel(new JLabel("로그인"), new Font("굴림",1,20)));

		JPanel southPanel = new JPanel();
		southPanel.setLayout(new FlowLayout());
		southPanel.add(createLabel(new JLabel("회원 번호:"), null));
		southPanel.add(idField);
		southPanel.add(createButton(new JButton("확인"), e->loginAct()));
		southPanel.add(createLabel(new JLabel("비밀 번호:"), null));
		southPanel.add(pwField);
		southPanel.add(createButton(new JButton("취소"), e->System.exit(0)));
		
		add(northPanel);
		add(centerPanel);
		add(southPanel);
	}
	
		private void loginAct() {
			if(division) {
				try (ResultSet rs = getSqlResults("SELECT * FROM tbl_customer WHERE cID=? AND cPW=?", idField.getText(),pwField.getText())){
					if(rs.next()) {
						informMessage("로그인 완료", "메시지");
						selectLogin();
					}else {
						informMessage("없는 회원 입니다.다시 확인하여 주십시오.", "메시지");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else {
				informMessage("비회원으로 로그인 합니다.", "메시지");
				selectLogin();
			}

		}
		
		private void selectLogin() {
			setVisible(false);
			new MenuFrame().setVisible(true);
		}
		
		private void radioBtnAct(String text,boolean bool) {
			division = bool;
			
			idField.setText(text);
			pwField.setText(text);
			
			idField.setEnabled(bool);
			pwField.setEnabled(bool);
		}
		
		public static void main(String[] args) {
			new LoginFrame().setVisible(true);
		}
	
}
