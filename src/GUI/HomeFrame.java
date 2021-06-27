package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import Coffee.CoffeeData;
import User.User;

public class HomeFrame {

    private JPanel contentPane = new JPanel(new BorderLayout());
    private JPanel searchPanel = new JPanel();
    private JFrame frame = new JFrame("Coffee");
    private JPanel cards = new JPanel(new CardLayout());
    CardLayout cl;
    // private JPanel loginPanel = new JPanel();
    JComboBox<String> comboBoxOrigin = new JComboBox<>();
    JComboBox<String> comboBoxVariety = new JComboBox<>();
    JComboBox<String> comboBoxProcessMethod = new JComboBox<>();
    JComboBox<String> comboBoxPurchased = new JComboBox<>();
    DefaultListModel<CoffeeData> listModel = new DefaultListModel<>();
    ArrayList<CoffeeData> displayData = null;
    JList<CoffeeData> list;
    String userID;
    User user;

    private class recommendHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<Integer> ret = user.getRecommand();
            displayData = CoffeeData.converter(ret);
            listModel.clear();

            for(CoffeeData o : displayData){
                listModel.addElement(o);
            }
            cards.setVisible(true);
            cl.show(cards, "content");
        }
    }

    private class searchHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String Origin = (String) comboBoxOrigin.getSelectedItem();
            String Variety = (String) comboBoxVariety.getSelectedItem();
            String ProcessMethod = (String) comboBoxProcessMethod.getSelectedItem();
            String Purchased = (String) comboBoxPurchased.getSelectedItem();

            Integer PurchasedInt = 0;
            if(Purchased.equals("是")){
                PurchasedInt = 1;
            }
            else if(Purchased.equals("否")){
                PurchasedInt = -1;
            }
            if(Origin.equals("請選擇") || Origin.equals("不限")) Origin = null;
            if(Variety.equals("請選擇") || Variety.equals("不限")) Variety = null;
            if(ProcessMethod.equals("請選擇") || ProcessMethod.equals("不限")) ProcessMethod = null;

            System.out.printf("%s, %s, %s, %d\n", Origin, Variety, ProcessMethod, PurchasedInt);
            
            
            ArrayList<Integer> ret = user.Search(Origin, Variety, ProcessMethod, PurchasedInt);
           displayData = CoffeeData.converter(ret);

            listModel.clear();

            for(CoffeeData o : displayData){
                listModel.addElement(o);
            }
            cards.setVisible(true);
            cl.show(cards, "content");
        }
    }

    public HomeFrame(){}
    public HomeFrame(String userID) {
        this.userID = new String(userID);
        this.user = new User(this.userID);
        this.user.InitPurchasedList();
        searchPanel = new JPanel();
        frame.setSize(800, 600);
        frame.setLocation(500, 50);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        {
            JLabel LabelOrigin = new JLabel("產地：");
            searchPanel.add(LabelOrigin);

            comboBoxOrigin.setToolTipText("產地");
            comboBoxOrigin.addItem("請選擇");
            comboBoxOrigin.addItem("非洲");
            comboBoxOrigin.addItem("亞洲");
            comboBoxOrigin.addItem("美洲");
            comboBoxOrigin.addItem("不限");
            searchPanel.add(comboBoxOrigin);

            JLabel LabelVariety = new JLabel("品種：");
            searchPanel.add(LabelVariety);

            comboBoxVariety.setToolTipText("品種");
            comboBoxVariety.addItem("請選擇");
            comboBoxVariety.addItem("Geisha");
            comboBoxVariety.addItem("SL28");
            comboBoxVariety.addItem("Heirloom");
            comboBoxVariety.addItem("Typica");
            comboBoxVariety.addItem("Bourbon");
            comboBoxVariety.addItem("其他");
            comboBoxVariety.addItem("不限");
            searchPanel.add(comboBoxVariety);

            JLabel LabelProcessMethod = new JLabel("處理法：");
            searchPanel.add(LabelProcessMethod);

            comboBoxProcessMethod.setToolTipText("處理法");
            comboBoxProcessMethod.addItem("請選擇");
            comboBoxProcessMethod.addItem("日曬法");
            comboBoxProcessMethod.addItem("水洗法");
            comboBoxProcessMethod.addItem("蜜處理");
            comboBoxProcessMethod.addItem("其他");
            comboBoxProcessMethod.addItem("不限");
            searchPanel.add(comboBoxProcessMethod);

            JLabel NewLabelPurchased = new JLabel("是否買過：");
            searchPanel.add(NewLabelPurchased);

            comboBoxPurchased.setToolTipText("是否買過");
            comboBoxPurchased.addItem("請選擇");
            comboBoxPurchased.addItem("是");
            comboBoxPurchased.addItem("否");
            comboBoxPurchased.addItem("不限");
            searchPanel.add(comboBoxPurchased);

            JButton ButtonSubmit = new JButton("搜尋");
            ButtonSubmit.addActionListener(new searchHandler());
            searchPanel.add(ButtonSubmit);

            JButton ButtonRecommend = new JButton("推薦");
            ButtonRecommend.addActionListener(new recommendHandler());
            searchPanel.add(ButtonRecommend);
        }
        // frame.getContentPane().add(searchPanel, BorderLayout.NORTH);
        // cards.add(searchPanel, "card2");

        // contentPane.add(comp)
        // frame.getContentPane().add(cards);
        // cl.show(cards, "card1");
        // cl.show(cards, "card2");
        list = new JList<>(listModel);
        // frame.add(new JScrollPane(list));
        list.setCellRenderer(new CoffeeDataRenderer());
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                JList<CoffeeData> theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2) {
                    System.out.println("yes");
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index >= 0) {
                        int o = theList.getModel().getElementAt(index).getCode();
                        System.out.println(o);
                        CoffeeData target = new CoffeeData();
                        System.out.println(displayData.get(0).getCode());
                        for(int i=0; i<displayData.size(); i++){
                            if(displayData.get(i).getCode() == o){
                                target = displayData.get(i);
                                break;
                            }
                        }
                        // CoffeeData info = new CoffeeData(target); // TODO: copy constructor

                        CoffeeFrame cf = new CoffeeFrame(target, user.Purchased(target.getCode()), user);
                    } 
                }
            }
        };
        list.addMouseListener(mouseListener);
        
        contentPane.add(new JScrollPane(list), BorderLayout.CENTER);
        cards.add(contentPane, "content");
        cl = (CardLayout)(cards.getLayout());
        // cl.show(cards, "content");
        // list.setVisible(false);
        ArrayList<Integer> ret = user.Search(null, null, null, 0);
        displayData = CoffeeData.converter(ret);
        listModel.clear();
        for(CoffeeData o : displayData){
            listModel.addElement(o);
        }
        cards.setVisible(true);
        frame.getContentPane().add(searchPanel, BorderLayout.NORTH);
        frame.getContentPane().add(cards, BorderLayout.CENTER);
        // frame.add(homePanel);
        // frame.pack();
        frame.setVisible(true);
    }
}
