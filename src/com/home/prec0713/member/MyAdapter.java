package com.home.prec0713.member;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*
java GUI분야에서의 Adapter란?

 개발자가 인터페이스 구현 시, 해당 인터페이스가 보유한 메서드가 너무 많은 경우
 사용하지도 않는 메서드를 오버라이드해야한다는 원칙을 고수하면 개발의 효율성이 떨어짐
 이 문제를 해결하기 위해 개발자 대신 sun사에서 Adapter라는 클래스를 지원해주는데,
 어댑터는 우리가 구현해야할 인터페이스를 대신 구현해놓았으므로
 개발자는 어댑터의 메서드 중 필요한 것만 골라서 오버라이드하면 됨
*/

public class MyAdapter extends WindowAdapter{
	public void windowClosing(WindowEvent e) {
		//1) 열려있는 Connection 닫기
		
		//2) 프로세스 종료
		
	}
}
