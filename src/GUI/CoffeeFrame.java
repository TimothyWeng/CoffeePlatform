package GUI;
import javax.swing.border.EmptyBorder;
import javax.swing.*;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.ArrayList;
import User.User;
import Coffee.CoffeeData;


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
            int point = user.getPoint();
            ArrayList<Integer> totalcost = new ArrayList<Integer>();
            rateComboBox.setEnabled(true);
            int tmp = 0;
            if (point > 0) {
                tmp = JOptionPane.showConfirmDialog(frame, String.format("您共有價值 %d 元的點數，請問要抵用嗎？", point), "", JOptionPane.YES_NO_OPTION);
                if (tmp == JOptionPane.OK_OPTION) {
                    totalcost = user.buy(items, point);
                    if (totalcost.get(0) < 0) {
                        user.setPoint((-1)*totalcost.get(0));
                        totalcost.set(0, 0);
                    }
                }
                else
                    totalcost = user.buy(items, 0);
            }
            else
                totalcost = user.buy(items, 0);
            if (totalcost.get(0) != 0)
                JOptionPane.showMessageDialog(frame, String.format("總共金額是 %d 元，真是太棒了！\n價值 %d 元的點數已經匯入您的帳戶！\n", totalcost.get(0), totalcost.get(0)/10));
            else 
                JOptionPane.showMessageDialog(frame, String.format("總共金額是 %d 元，真是太棒了！\n", totalcost.get(0)));
            if (totalcost.size() > 1 && totalcost.get(1) == 1) 
                JOptionPane.showMessageDialog(frame, "恭喜成為會員，以後訂單都享 9 折優惠！\n");

            target.setInventory(target.getInventory()-amount);
            discription.setText(("品名：%s\r\n產地：%s\r\n區域：%s\r\n處理法：%s\r\n產季：%d年\r\n品種：%s\r\n風味：\r\n%s\r\n"+
            "剩餘包數：%d\r\n評分/人數：%.2f/%d").formatted(target.getName()
            , target.getProductionArea(), target.getRegion(), target.getProcessMethod(), target.getProductionSeason(), target.getVariety()
            , target.getFlavor(), target.getInventory(), target.getRate(), target.getRatedPeople()));

            //buyComboBox.removeAllItems();
            System.out.println(target.getInventory());
            for(int i = buyComboBox.getItemCount()-1; i > Math.min(10, target.getInventory()); i--){ 
                System.out.println(i);   
                buyComboBox.removeItem(Integer.toString(i));
            }
            buyComboBox.setBounds(330, 145+30, 100, 20);
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
            JOptionPane.showMessageDialog(frame, String.format("你評了 %d 分，真是太棒了\n", ratingValue));
            target.setRate((double)(target.getRate()*target.getRatedPeople()+ratingValue) / (double)(target.getRatedPeople()+1));
            target.setRatedPeople(target.getRatedPeople()+1);
            discription.setText(("品名：%s\r\n產地：%s\r\n區域：%s\r\n處理法：%s\r\n產季：%d年\r\n品種：%s\r\n風味：\r\n%s\r\n"+
            "剩餘包數：%d\r\n評分/人數：%.2f/%d").formatted(target.getName()
            , target.getProductionArea(), target.getRegion(), target.getProcessMethod(), target.getProductionSeason(), target.getVariety()
            , target.getFlavor(), target.getInventory(), target.getRate(), target.getRatedPeople()));

            rateButton.setEnabled(false);
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
		frame.setBounds(100, 100, 520, 320);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
        ImageIcon icon = new ImageIcon("src/image/" + Integer.toString(target.getCode()) + "-1.png");
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
        "剩餘包數：%d\r\n評分/人數：%.2f/%d").formatted(target.getName()
        , target.getProductionArea(), target.getRegion(), target.getProcessMethod(), target.getProductionSeason(), target.getVariety()
        , target.getFlavor(), target.getInventory(), target.getRate(), target.getRatedPeople()));
		discription.setOpaque(false);
		discription.setEditable(false);
		discription.setBounds(280, 10, 220, 170);
		contentPane.add(discription);
        frame.setVisible(true);
    }
    
}
