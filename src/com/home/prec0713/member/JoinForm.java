package com.home.prec0713.member;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField; 

/*
JDBC - 자바 언어로 DB를 제어하는 기술을 가리킴
Java Database Connectivity
java.sql 패키지에서 지원함
*/

class JoinForm extends JFrame implements ActionListener{
	JTextField t_id;
	JTextField t_name;
	JTextField t_phone;
	JButton bt_connect; //DB접속
	JButton bt_regist; //등록
	
	//2-1) mySQL 접속주소(+포트번호) 
	//?characterEncoding=utf8 -> 한글 등 외국어 정상출력되도록 함 (안깨지게)
	String urlMySql="jdbc:mysql://localhost:3306/javase?characterEncoding=utf8"; 
	//*Connection 객체란? 
	//2-1) 접속을 성공하면, 그 접속 정보를 보유한 객체
	Connection con=null;
	
	//cf.오라클 접속주소_프로토콜(+포트번호)
	//OracleService"XE" 오라클인스턴스명임 - 오라클은 pc 1대에 복수로 설치가능하며, 이 때 인스턴스명을 바꿔서 설치해야함
	String urlOracle="jdbc:oracle:thin:@localhost:1521:XE";
	
	
	public JoinForm() {
		t_id=new JTextField();
		t_name=new JTextField();
		t_phone=new JTextField();
		bt_connect=new JButton("접속");
		bt_regist=new JButton("가입");
		
		Dimension d=new Dimension(280, 40);
		t_id.setPreferredSize(d);
		t_name.setPreferredSize(d);
		t_phone.setPreferredSize(d);
		
		setLayout(new FlowLayout());
		
		add(t_id);
		add(t_name);
		add(t_phone);
		add(bt_connect);
		add(bt_regist);

		setVisible(true);
		setSize(300, 400);
		
		//setDefaultCloseOperation(EXIT_ON_CLOSE); //프로세스만 없앰
		
		
		//버튼들과 리스너연결
		bt_connect.addActionListener(this);
		bt_regist.addActionListener(this);
		
		bt_regist.setEnabled(false); //비활성화
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//1) 열려있는 Connection 닫기
				if(con!=null) {
					try {
						con.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				
				//2) 프로세스 종료
				System.exit(0); //윈도우창 닫기
			}
		});
	}
	
	//★DB와 Stream객체는 사용 후 반드시 닫아줘야함 : 계속 메모리에 남아있기 때문
	//: 본 코드 내에서는 아래의 Connection, PreparedStatement 객체가 해당됨
	
	//★★★MySQL DB에 접속을 시도하는 메서드★★★
	public void connect() {
		try {
			 //1) MySQL용 드라이버를 로드함 "드라이버경로"
			Class.forName("com.mysql.jdbc.Driver");  //!!★중요★!!
			System.out.println("드라이버 로드 성공함");
			
			//2) 접속을 시도함 : (cmd명령어) mysql -h localhost -u root -p
			//*Connection 객체란? 
			//접속을 성공하면, 그 접속 정보를 보유한 객체
			//Connection con=null; -> 등록 메서드를 분리하면서 멤버변수로 이전함 
			con= DriverManager.getConnection(urlMySql, "root", "1234"); //!!★중요★!! (접속주소, ID, PW)
			
			//cf.오라클 접속 시의 Connection 객체
			//con=DriverManager.getConnection(urlOracle, "java", "1234");
			
			//getConnection()이 항상 접속 성공을 보장하는 것은 아니기때문에 접속 성공&실패 여부를 확인할 수 있도록함
			if(con==null) {
				System.out.println("접속실패");
			}else{
				System.out.println("접속성공");
				//접속버튼 비활성화+가입버튼 활성화
				bt_connect.setEnabled(false);
				bt_regist.setEnabled(true);
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("드라이버가 존재하지 않음");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//★★★MySQL DB에 레코드를 등록하는 메서드★★★
	public void regist() {
		String id=t_id.getText();
		String name=t_name.getText();
		String phone=t_phone.getText();
		
		String sql="insert into member(id, name, phone) values('"+id+"', '"+name+"', '"+phone+"')";
		//!!★중요★!! PreparedStatement : 쿼리문 수행을 담당하는 jdbc 객체, 쿼리문 1개당 1:1로 대응함
		//즉, 쿼리문을 200개 실행하면 200개가 메모리에 생성되어 쌓여있기 때문에, 사용 후 반드시 제거해줘야함
		PreparedStatement pstmt=null; 
		try {
			//3) 쿼리문을 수행함
			pstmt=con.prepareStatement(sql);
			//4) 준비된 쿼리문을 실행함
			//*executeUpdate() : DML -> INSERT, UPDATE, DELETE
			//또한 반환값으로 성공&실패여부를 확인할 수 있음, 즉 쿼리문으로 인해 영향을 받은 레코드의 수가 반환됨
			//따라서 성공 시 1(insert문 성공으로 영향받는 레코드수는 항상 1임), 실패 시 0이 반환됨
			
			//DML을 수행할 수 있는데, 이 때 수행결과로 이 쿼리 실행에 영향을 받은 레코드 수를 반환함
			//따라서 개발자는 그 결과가 0이면, DML수행이 안되었다는 것을 알 수 있음
			int result=pstmt.executeUpdate(); 
			
			if(result>0){
				//js) alert기능과 유사함 (부모컨테이너, "메세지내용")
				JOptionPane.showMessageDialog(this, "가입 성공"); //js) alert기능과 유사함 
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	//cf.오라클에 insert하는 메서드
	public void registOracle() {
		PreparedStatement pstmt=null;
		String sql="insert into member(member_idx, id, name, phone)";
		sql=sql+" values(seq_member.nextval, 'batman', '브루스', '345')";
		
		try {
			pstmt=con.prepareStatement(sql); //쿼리문 준비
			int result=pstmt.executeUpdate(); //insert 실행
			
			if(result>0) {
				JOptionPane.showMessageDialog(this, "오라클 등록 성공");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		Object obj=e.getSource();
		
		if(obj==bt_connect) { //접속 버튼을 누르면
			connect();
		}else if(obj==bt_regist){ //가입 버튼을 누르면
			regist();
			registOracle();
		}
		
	}
	
	public static void main(String[] args) {
		new JoinForm();
		
	}
}
