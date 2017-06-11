package kr.ac.kookmin.cs.main;

import kr.ac.kookmin.cs.gui.WindowGUI;
import kr.ac.kookmin.cs.xquery.NetworkingExist;

import org.xmldb.api.base.XMLDBException;

public class ArxmlManager {

	public static void main(String args[]) throws XMLDBException {
		WindowGUI awtControlDemo = new WindowGUI();
		NetworkingExist exist = new NetworkingExist();
		
		awtControlDemo.showList(exist);
	}
}
