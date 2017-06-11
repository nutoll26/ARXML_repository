package kr.ac.kookmin.cs.GUI;

import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.xmldb.api.base.XMLDBException;

import kr.ac.kookmin.cs.Xquery.NetworkingExist;


public class WindowGUI extends JFrame{
	private Frame mainFrame;
	private Label headerLabel;
	private Label statusLabel;
	private Panel controlPanel, buttonPanel;
	private final List[] ecuList = new List[5];
	private JList ecuCategoryList;
	
	private int optionNum = 0;
	
	public WindowGUI() {
		prepareGUI();
	}
	
	private void prepareGUI() {
		// Frame 에 대한 셋팅
		mainFrame = new Frame("ECU Extract");
		mainFrame.setSize(600, 600);
		mainFrame.setLayout(new GridLayout(4, 1));
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});

		// 상단에 있는 라벨
		headerLabel = new Label();
		headerLabel.setAlignment(Label.CENTER);
		headerLabel.setText("Control Test : List");

		// 하단 상태값 라벨
		statusLabel = new Label();
		statusLabel.setText("Status Lable");
		statusLabel.setAlignment(Label.CENTER);
		statusLabel.setSize(350, 100);

		controlPanel = new Panel();
		controlPanel.setLayout(new FlowLayout());

		buttonPanel = new Panel();
		buttonPanel.setLayout(new FlowLayout());

		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel);
		mainFrame.add(buttonPanel);
		mainFrame.add(statusLabel);
		mainFrame.setVisible(true);
	}

	public void showList(NetworkingExist exist) {
		final String []ecuCategoryStr = {"Body", "PT", "Chassis", "OccptPedSfty", "MmedTelmHmi"};
		ecuCategoryList = new JList(ecuCategoryStr);
		ecuCategoryList.setVisibleRowCount(3);

		ecuList[0] = new List(5, true);
		ecuList[0].add("LockgCen");
		ecuList[0].add("IntrLi");
		ecuList[0].add("MirrAdjmt");
		ecuList[0].add("MirrTintg");
		ecuList[0].add("SeatAdjmt");
		ecuList[0].add("WiprWshr");
		ecuList[0].add("EntryRemKeyls");
		ecuList[0].add("KeyPad");
		ecuList[0].add("Antithft");
		ecuList[0].add("ExtrLi");
		ecuList[0].add("TerminalClmpCtrl");
		ecuList[0].add("HornCtrl");
		ecuList[0].add("RoofCnvtbCtrl");
		ecuList[0].add("DefrstCtrl");
		ecuList[0].add("Imob");
		ecuList[0].add("Pase");
		ecuList[0].add("SnsrBody");
		ecuList[0].add("SeatClima");

		ecuList[1] = new List(5, true);
		ecuList[1].add("PtCoorr");
		ecuList[1].add("Trsm");
		ecuList[1].add("CmbEng");
		ecuList[1].add("VehMtnForPt");
		ecuList[1].add("PtMisc");

		ecuList[2] = new List(5, true);
		ecuList[2].add("CrsCtrlAndAcc");
		ecuList[2].add("Esc");
		ecuList[2].add("Ssm");
		ecuList[2].add("Epb");
		ecuList[2].add("Vlc");
		ecuList[2].add("Rsc");
		ecuList[2].add("Steer");
		ecuList[2].add("SteerVehStabyCtrl");
		ecuList[2].add("SteerDrvrAsscSys");
		ecuList[2].add("Susp");
		ecuList[2].add("DtTqDistbn");
		ecuList[2].add("TirePMon");
		ecuList[2].add("ChassisSnsr");
		ecuList[2].add("SurrndgsSnsr");

		ecuList[3] = new List(5, true);
		ecuList[3].add("SnsrPoolOccptPedSfty");
		ecuList[3].add("ActrPoolOccptPedSfty1");
		ecuList[3].add("ActrPoolOccptPedSfty2");
		ecuList[3].add("ActrPoolOccptPedSfty3");
		ecuList[3].add("SeatBltRmn");
		ecuList[3].add("PedCrashDetn");
		ecuList[3].add("SysPedProtnActvn");
		ecuList[3].add("OccptDetn");
		ecuList[3].add("VehCrashDetn");
		ecuList[3].add("OccptRestrntSysActvn");

		ecuList[4] = new List(5, true);
		ecuList[4].add("BtnPan");
		ecuList[4].add("InterdomCtrlr");

		Button showButton = new Button("Arxml");
		Button ExtractButton = new Button("Extact To Excel");

		JListHandler handler = new JListHandler();
		ecuCategoryList.addListSelectionListener(handler);
		
		showButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(ecuCategoryList.getSelectedIndex());
			}
		});

		ExtractButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ecuName = ecuList[optionNum].getSelectedItem();
				System.out.println(ecuList[optionNum].getSelectedItem());
				try {
					exist.SendQueryToExistDB(ecuName);
				} catch (XMLDBException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		ecuCategoryList.setSelectedIndex(0);
		controlPanel.add(ecuCategoryList);
		controlPanel.add(ecuList[0]);
		buttonPanel.add(showButton);
		buttonPanel.add(ExtractButton);

		mainFrame.setVisible(true);
	}

	private class JListHandler implements ListSelectionListener {
		// 리스트의 항목이 선택이 되면
		public void valueChanged(ListSelectionEvent event) {
			controlPanel.remove(ecuList[optionNum]);
			controlPanel.add(ecuList[ecuCategoryList.getSelectedIndex()]);
			optionNum = ecuCategoryList.getSelectedIndex();
			
			mainFrame.setVisible(true);
		}
	}
}
