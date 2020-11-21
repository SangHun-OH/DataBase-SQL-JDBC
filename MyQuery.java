package SqlDB;

import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

public class MyQuery extends JFrame {

	Vector<Object[]> sqlData;
	JTable Table;
	JPanel countPanel;
	Connection conn = null;

	String[] select = { "전체", "부서별" };
	String[] departSelect = { "Research", "Administration", "Headquarters" };
	String[] attribute = { "Name", "Ssn", "Bdate", "Address", "Sex", "Salary", "Supervisor", "Dname" };
	String[] tableBox = { "NAME", "SSN", "BDATE", "ADDRESS", "SEX", "SALARY", "SUPERVISOR", "DEPARTMENT" };

	JLabel sRange = new JLabel("   검색범위");
	JLabel sSelect = new JLabel("검색항목");
	JLabel LB0 = new JLabel("                                     ");

	JLabel newSalary = new JLabel("새로운 Salary");
	JLabel deleteLB = new JLabel("선택한 데이터 삭제");
	JLabel LB5 = new JLabel("                                ");
	int count = 0;
	JLabel countLB = new JLabel("   인원수 : ", count);
	JLabel selEmLB = new JLabel("   선택한 직원 : ");
	JLabel LB8 = new JLabel("                                                  ");

	JComboBox selCBB = new JComboBox(select);
	JComboBox departCBB = new JComboBox(departSelect);
	JCheckBox[] attributes = { new JCheckBox("NAME", true), new JCheckBox("SSN", true), new JCheckBox("BDATE", true),
			new JCheckBox("ADDRESS", true), new JCheckBox("SEX", true), new JCheckBox("SALARY", true),
			new JCheckBox("SUPERVISOR", true), new JCheckBox("DEPARTMENT", true) };

	JButton search = new JButton("검색");
	JTextField chaSalary = new JTextField(20);
	JButton delete = new JButton("DELETE");
	JButton update = new JButton("UPDATE");


	JPanel tablePN;
	JFrame FR = this;
	JLabel COUNT = new JLabel();
	JPanel fPanel;
	JPanel f2Panel;
	JPanel f3Panel;
	JPanel sPanel;
	JPanel tPanel;
	JPanel foPanel;
	JPanel nPanel;
	JPanel down;
	FlowLayout fl = new FlowLayout();

	public MyQuery() {
		setTitle("Information Retrieval System");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

// 윗 패널 -----------------------------------------------------------------##
		fPanel = new JPanel(fl);
		fPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		f2Panel = new JPanel(fl);
		f3Panel = new JPanel(fl);
		sPanel = new JPanel(fl);

		fPanel.add(sRange);
		fPanel.add(selCBB);

		f2Panel.add(departCBB);
		f2Panel.setVisible(false);

		f3Panel.add(LB0);

		selCBB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox scb = (JComboBox) e.getSource();
				int i = scb.getSelectedIndex();
				// System.out.print(cb.getSelectedItem());
				if (scb.getSelectedItem() == "전체") {
					f2Panel.setVisible(false);
					f3Panel.setVisible(true);
				} else {
					f2Panel.setVisible(true);
					f3Panel.setVisible(false);
				}
			}
		});
		sPanel.add(sSelect);
		for (int i = 0; i < 8; i++) {
			sPanel.add(attributes[i]);
		}
		sPanel.add(search);

		fPanel.add(f2Panel);
		fPanel.add(f3Panel);
		fPanel.add(sPanel);

// 아래 패널 -----------------------------------------------------------------##

		tPanel = new JPanel(fl);
		tPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		foPanel = new JPanel(fl);

	
		tPanel.add(newSalary);
		tPanel.add(delete);
		tPanel.add(chaSalary);
		tPanel.add(update);


		foPanel.add(deleteLB);
		foPanel.add(delete);

		nPanel = new JPanel(fl);
		nPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		countPanel = new JPanel(fl);
		countPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		down = new JPanel(fl);
		down.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		down.add(tPanel);
		down.add(foPanel);
		down.add(nPanel);

// search Query ---------------------------------------------------------########
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nPanel.removeAll();
				nPanel.add(selEmLB);

				String stmt = "";

				if (selCBB.getSelectedItem().toString() == "부서별") {
					if (departCBB.getSelectedItem().toString() == "Research") {
						stmt = "Select Concat(E.Fname,' ', E.minit,' ', E.Lname,' ')as Name, E.Ssn, E.Bdate, E.Address, E.Sex, E.Salary,"
								+ " concat(S.Fname, ' ', S.minit, ' ',S.Lname,' ')as Supervisor, dname\r\n"
								+ "from employee as E left outer join employee as S on E.super_ssn=S.ssn, department\r\n"
								+ "where E.Dno = dnumber" + " AND Dname = \"Research\"";
					} else if (departCBB.getSelectedItem().toString() == "Administration") {
						stmt = "Select Concat(E.Fname,' ', E.minit,' ', E.Lname,' ')as Name, E.Ssn, E.Bdate, E.Address, E.Sex, E.Salary,"
								+ " concat(S.Fname, ' ', S.minit, ' ',S.Lname,' ')as Supervisor, dname\r\n"
								+ "from employee as E left outer join employee as S on E.super_ssn=S.ssn, department\r\n"
								+ "where E.Dno = dnumber" + " AND Dname = \"Administration\"";
					} else if (departCBB.getSelectedItem().toString() == "Headquarters") {
						stmt = "Select Concat(E.Fname,' ', E.minit,' ', E.Lname,' ')as Name, E.Ssn, E.Bdate, E.Address, E.Sex, E.Salary,"
								+ " concat(S.Fname, ' ', S.minit, ' ',S.Lname,' ')as Supervisor, dname\r\n"
								+ "from employee as E left outer join employee as S on E.super_ssn=S.ssn, department\r\n"
								+ "where E.Dno = dnumber" + " AND Dname = \"Headquarters\"";
					}
				} else {
					stmt = "Select Concat(E.Fname,' ', E.minit,' ', E.Lname,' ')as Name, E.Ssn, E.Bdate, E.Address, E.Sex, E.Salary,"
							+ " concat(S.Fname, ' ', S.minit, ' ',S.Lname,' ')as Supervisor, dname\r\n"
							+ "from employee as E left outer join employee as S on E.super_ssn=S.ssn, department\r\n"
							+ "where E.Dno = dnumber";
				}

				String name, ssn, bdate, address, sex, Supervisor, dname;
				double salary;

				Connection conn = null;
				try {
					try {
						Class.forName("com.mysql.cj.jdbc.Driver");
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
					}

					String url = "jdbc:mysql://localhost:3306/COMPANY?serverTimezone=UTC";
					String user = "root";
					String pwd = "1234";

					conn = DriverManager.getConnection(url, user, pwd);

					PreparedStatement p = conn.prepareStatement(stmt);
					ResultSet r = p.executeQuery();
					sqlData = new Vector<Object[]>();
					while (r.next() == true) {
						name = r.getString("Name");
						ssn = r.getString("E.Ssn");
						bdate = r.getString("E.Bdate");
						address = r.getString("E.Address");
						sex = r.getString("E.Sex");
						salary = r.getDouble("E.Salary");
						Supervisor = r.getString("Supervisor");
						dname = r.getString("Dname");

						sqlData.add(new Object[] { name, ssn, bdate, address, sex, salary, Supervisor, dname, false });
					}
					resultTable();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FR.remove(tablePN);
				FR.revalidate();
				nPanel.removeAll();
				countPanel.removeAll();
				nPanel.add(selEmLB);
				Vector<Object[]> tempData = new Vector<Object[]>();
				String stmt = "";

				for (int i = 0; i < Table.getRowCount(); i++) {
					if (Table.getValueAt(i, 8).equals(true)) {
						JLabel NAME = new JLabel((String) Table.getValueAt(i, 0));
						nPanel.add(NAME);
					} else {
						tempData.add(sqlData.get(i));
					}
				}
				sqlData = tempData;
				Connection conn = null;
				try {
					for (int i = 0; i < Table.getRowCount(); i++) {
						if (Table.getValueAt(i, 8).equals(true)) {
							try {
								Class.forName("com.mysql.cj.jdbc.Driver");
							} catch (ClassNotFoundException e1) {
								e1.printStackTrace();
							}

							String url = "jdbc:mysql://localhost:3306/COMPANY?serverTimezone=UTC";
							String user = "root";
							String pwd = "1234";

							conn = DriverManager.getConnection(url, user, pwd);

							stmt = "Delete From Employee Where Ssn = " + '"' + (String) Table.getValueAt(i, 1) + '"';

							PreparedStatement p = conn.prepareStatement(stmt);
							int r = p.executeUpdate();
							System.out.println("변경된 row : " + r);
						}
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				FR.remove(tablePN);
				FR.revalidate();
				resultTable();
			}
		});

		update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FR.remove(tablePN);
				FR.revalidate();
				nPanel.removeAll();
				countPanel.removeAll();
				nPanel.add(selEmLB);
				Vector<Object[]> tempData = new Vector<Object[]>();
				ArrayList<String> tempSsn = new ArrayList<String>();

				String stmt = "";
				double setSal = Double.parseDouble(chaSalary.getText());
				String setSal2 = chaSalary.getText();
				
				for (int i = 0; i < Table.getRowCount(); i++) {
					if (Table.getValueAt(i, 8).equals(true)) {
						JLabel NAME = new JLabel((String) Table.getValueAt(i, 0));
						nPanel.add(NAME);
						tempSsn.add((String) Table.getValueAt(i, 1));
						Object[] tempValue = sqlData.get(i);
						tempValue[5] = setSal;
						tempData.add(tempValue);
					} else {
						tempData.add(sqlData.get(i));
					}
				}
				sqlData = tempData;
				Connection conn = null;
				try {
					for (int i = 0; i < Table.getRowCount(); i++) {
						if (Table.getValueAt(i, 8).equals(true)) {
							try {
								Class.forName("com.mysql.cj.jdbc.Driver");
							} catch (ClassNotFoundException e1) {
								e1.printStackTrace();
							}
							String url = "jdbc:mysql://localhost:3306/COMPANY?serverTimezone=UTC";
							String user = "root";
							String pwd = "1234";

							conn = DriverManager.getConnection(url, user, pwd);

							stmt = "Update Employee Set Salary = " + setSal2 + " Where Ssn = " + tempSsn.get(0);

							PreparedStatement p = conn.prepareStatement(stmt);
							int r = p.executeUpdate();
							System.out.println("변경된 row : " + r);
						}
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				FR.remove(tablePN);
				FR.revalidate();
				resultTable();
			}
		});

		FR.add("North", fPanel);
		FR.add("South", down);
		FR.add("West", countPanel);
		FR.setSize(1300, 400);
		FR.setVisible(true);
	}

	public void resultTable() {
		DefaultTableModel tableModel = new DefaultTableModel(tableBox, 0);
		tableModel.addColumn("선택");

		for (int i = 0; i < sqlData.size(); i++) {
			tableModel.addRow(sqlData.get(i));
		}

		Table = new JTable(tableModel) {
			@Override
			public Class getColumnClass(int column) {
				switch (column) {
				case 0:
					return String.class;
				case 1:
					return String.class;
				case 2:
					return String.class;
				case 3:
					return String.class;
				case 4:
					return String.class;
				case 5:
					return String.class;
				case 6:
					return String.class;
				case 7:
					return String.class;
				default:
					return Boolean.class;
				}
			}
		};
		for (int i = 0; i < attributes.length; i++) {
			if (attributes[i].isSelected() == false) {
				Table.getColumn(tableBox[i]).setWidth(0);
				Table.getColumn(tableBox[i]).setMinWidth(0);
				Table.getColumn(tableBox[i]).setMaxWidth(0);
			}
		}

		COUNT.setText(Integer.toString(Table.getRowCount()));
		countPanel.add(countLB);
		countPanel.add(COUNT);

		tablePN = new JPanel(fl);
		tablePN.add(Table);
		JScrollPane resultPane = new JScrollPane(Table);
		resultPane.setPreferredSize(new Dimension(1100, 250));

		tablePN.add(resultPane);
		FR.add("Center",tablePN);
		FR.add("West", countPanel);

		FR.setSize(1300, 400);
		FR.setVisible(true);
		FR.revalidate();
	}
}