package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import Coffee.CoffeeData;
import DataManage.DataWriter;
import DataManage.Search;
import User.Admin;
import User.User;

import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.GroupLayout.Alignment;

public class AdminFrame extends JFrame {

	private JPanel contentPane;
	private Admin adminUser;
	private String AdminUID;
	CardLayout layout;

	private class MenuHandler implements MenuListener{

		@Override
		public void menuSelected(MenuEvent e) {
			// TODO Auto-generated method stub
			JMenu menu = (JMenu) e.getSource();
			System.out.println(menu.getText());
		}

		@Override
		public void menuDeselected(MenuEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void menuCanceled(MenuEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	/**
	 * Create the frame.
	 */
	public AdminFrame(String AdminUID) {
		this.AdminUID = AdminUID;

		this.adminUser = new Admin(AdminUID);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 300);
		
        JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("選項");
		setJMenuBar(menuBar);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("補貨");
		JMenuItem mntmNewMenuItem2 = new JMenuItem("匯入咖啡");
		menu.add(mntmNewMenuItem);
		menu.add(mntmNewMenuItem2);
        mntmNewMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				layout.show(contentPane, "inventory");
				
				
            }
			
        });
        mntmNewMenuItem2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				layout.show(contentPane, "coffee");

            }

        });
		
		menuBar.add(menu);
		// menu.addMenuListener(new MenuHandler());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, "inventory");
		
		JLabel lblNewLabel = new JLabel("咖啡名：");
		panel.add(lblNewLabel);
		
		HashMap<String, Integer> NameToCode = new HashMap<>();
		JComboBox<String> comboBox = new JComboBox<>();
		ArrayList<Integer> ret = Search.GetIndex();
		ArrayList<CoffeeData> CoffeeDataList = CoffeeData.converter(ret);
		for(CoffeeData cd : CoffeeDataList){
			comboBox.addItem(cd.getName());
			NameToCode.put(cd.getName(), cd.getCode());
		}
		panel.add(comboBox);

		JLabel lblNewLabel2 = new JLabel("數量：");
		panel.add(lblNewLabel2);
		
		JComboBox<Integer> comboBox_1 = new JComboBox<>();
		for(int i = 1; i <= 10; i++){
			comboBox_1.addItem(i);
		}
		panel.add(comboBox_1);
		
		JButton btnNewButton = new JButton("新增");
		btnNewButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String selected = (String)comboBox.getSelectedItem();
				int id = NameToCode.get(selected);
				int addAmount = (Integer)comboBox_1.getSelectedItem();
				System.out.printf("%d, %s %d\n", id, selected, addAmount);
				if(adminUser.addInventory(id, addAmount)){
					JOptionPane.showMessageDialog(new JFrame(), "成功新增咖啡:%s %d包!".formatted(selected, addAmount));
				}
				else{
					JOptionPane.showMessageDialog(new JFrame(), "新增咖啡失敗!");
				}
			}
		});
		panel.add(btnNewButton);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, "coffee");
		
		JButton btnNewButton_1 = new JButton("搜尋檔案");
		btnNewButton_1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                int flag = chooser.showOpenDialog(null);
				if (flag == JFileChooser.APPROVE_OPTION) {
					String path = new String(chooser.getSelectedFile().toPath().toString());
					System.out.println("使用者選擇了檔案：" + path);
					DataWriter.coffeeInit(path);
					JOptionPane.showMessageDialog(new JFrame(), "成功新增咖啡資料!");
				}
			}
		});
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(162)
					.addComponent(btnNewButton_1)
					.addContainerGap(177, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(97)
					.addComponent(btnNewButton_1)
					.addContainerGap(112, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		layout = (CardLayout)contentPane.getLayout();
		setVisible(true);
	}

}
