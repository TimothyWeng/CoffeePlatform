import javax.swing.border.EmptyBorder;
import javax.swing.*;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.ArrayList;

public class CoffeeFrame {
	private JPanel contentPane;
    private JFrame frame = new JFrame();
    CoffeeData target;
    boolean purchased = false;
    User user;
    JLabel lblNewLabel = new JLabel("New label");
    JSeparator separator = new JSeparator();
    JComboBox<String> buyComboBox = new JComboBox<>();
    JButton buyButton = new JButton("購買");
    JComboBox<String> rateComboBox = new JComboBox<>();
    JButton rateButton = new JButton("送出評分");
    JTextArea discription = new JTextArea();

    private class buyComboBoxHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            buyButton.setEnabled(!buyComboBox.getSelectedItem().equals("購買數量"));
        }
    }
    private class buyButtonHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            Integer amount = Integer.parseInt((String) buyComboBox.getSelectedItem());
            ArrayList<Integer> items = new ArrayList<>();
            System.out.printf("coffee code: %d is purchased with amount= %d\n", target.getCode(), amount);
            for(int i = 0; i < amount; i++){
                items.add(target.getCode());
            }
            user.buy(items);
            rateComboBox.setEnabled(true);
            JOptionPane.showMessageDialog(frame, String.format("coffee code: %d is purchased with amount= %d\n", target.getCode(), amount));
        }
    }
    private class rateComboBoxHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("ratingComboBoxHandler");
            rateButton.setEnabled(!rateComboBox.getSelectedItem().equals("請評分"));
        }
    }
    private class rateButtonHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            Integer ratingValue = Integer.parseInt((String) rateComboBox.getSelectedItem());
            System.out.printf("coffee code:%d is rated with %d\n", target.getCode(), ratingValue);
            user.rateCoffee(target.getCode(), ratingValue);
            JOptionPane.showMessageDialog(frame, String.format("coffee code:%d is rated with %d\n", target.getCode(), ratingValue));
        }
    }


    public CoffeeFrame(){}
    public CoffeeFrame(CoffeeData target, boolean purchased, User user){
        this.target = target;
        this.user = user;
        this.purchased = purchased;
		frame.setResizable(false);
		frame.setTitle(target.getName());
		frame.setBounds(new Rectangle(0, 0, 600, 600));
		frame.setSize(new Dimension(600, 700));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(100, 100, 500, 320);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
        ImageIcon icon = new ImageIcon("../image/" + Integer.toString(target.getCode()) + "-1.png");
		lblNewLabel.setIcon(icon);
		lblNewLabel.setBounds(10, 11, 256, 256);
		contentPane.add(lblNewLabel);
		
		separator.setOrientation(SwingConstants.VERTICAL);
		contentPane.add(separator);
		
        buyComboBox.addItem("購買數量");
        for(int i = 1; i <= Math.min(10, target.getInventory()); i++){    
            buyComboBox.addItem(Integer.toString(i));
        }
		buyComboBox.setBounds(330, 145+30, 100, 20);
        buyComboBox.addActionListener(new buyComboBoxHandler());
		contentPane.add(buyComboBox);
        
		buyButton.setBounds(330, 170+30, 100, 20);
        buyButton.setEnabled(false);
        buyButton.setToolTipText("請輸入購買數量");
        buyButton.addActionListener(new buyButtonHandler());
		contentPane.add(buyButton);
		
        rateComboBox.addItem("請評分");
        for(int i = 1; i <= 10; i++){
            rateComboBox.addItem(Integer.toString(i));
        }
		rateComboBox.setBounds(330, 195+30, 100, 20);
        rateComboBox.setEnabled(purchased);
        rateComboBox.setToolTipText("請先購買");
        rateComboBox.addActionListener(new rateComboBoxHandler());
		contentPane.add(rateComboBox);

        rateButton.setBounds(330, 220+30, 100, 20);
        rateButton.setEnabled(false);
        rateButton.addActionListener(new rateButtonHandler());
        contentPane.add(rateButton);
		
		discription.setText(("品名：%s\r\n產地：%s\r\n區域：%s\r\n處理法：%s\r\n產季：%d年\r\n品種：%s\r\n風味：\r\n%s\r\n"+
        "剩餘包數：%d\r\n評分/人數：%f/%d").formatted(target.getName()
        , target.getProductionArea(), target.getRegion(), target.getProcessMethod(), target.getProductionSeason(), target.getVariety()
        , target.getFlavor(), target.getInventory(), target.getRate(), target.getRatedPeople()));
		discription.setOpaque(false);
		discription.setEditable(false);
		discription.setBounds(280, 10, 200, 170);
		contentPane.add(discription);
        frame.setVisible(true);
    }
    
}
