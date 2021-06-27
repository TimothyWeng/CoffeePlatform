package src;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Define custom Java List Object
 */
public class JListCustomRenderer extends JFrame{
    private ArrayList<CoffeeData> displayData = new ArrayList<CoffeeData>();
    /**
     * constructor of Java Panel
     * @param PD
     * @return JPanel panel
     */
    public JPanel createPanel(ArrayList<CoffeeData> PD){
        if(PD != null){
            displayData = PD;
        }
        JPanel frame = new JPanel(new BorderLayout());
        JPanel cardHolder = new JPanel(new CardLayout());
        JPanel panel = new JPanel(new BorderLayout());
        // JButton btnBack = new JButton("回列表");
        // btnBack.setBackground(Color.WHITE);
        // btnBack.setForeground(new Color(21, 188, 163));
        // btnBack.setFont(new Font(Font.DIALOG,Font.BOLD,18));
        // btnBack.setVisible(false);
        cardHolder.add(panel,"List");
        CardLayout cl = (CardLayout)(cardHolder.getLayout());
        cl.show(cardHolder, "List");
        JList jlist = createList();
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                JList<String> theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2) {
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index >= 0) {
                        Object o = theList.getModel().getElementAt(index);
                        CoffeeData target = new CoffeeData();
                        for(int i=0; i<displayData.size(); i++){
                            if(displayData.get(i).getTitle().equals(o.toString())){
                                target = displayData.get(i);
                                break;
                            }
                        }
                        tripInfo info = new tripInfo(target);
                        JPanel panel2 = info.getPanel();
                        cardHolder.add(panel2,"Info");
                        cl.show(cardHolder,"Info");
                        btnBack.setVisible(true);
                    }
                }
            }
        };
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cl.show(cardHolder,"List");
                btnBack.setVisible(false);
            }
        });
        jlist.addMouseListener(mouseListener);
        panel.setBorder(new EmptyBorder(5,5,5,5));
        panel.setBackground(new Color(21,188,163));
        panel.add(new JScrollPane(jlist),BorderLayout.CENTER);
        frame.add(cardHolder);
        frame.add(btnBack, BorderLayout.SOUTH);
        return frame;
    }

    /**
     * create Java List
     * @return JList list
     */
    public JList<CoffeeData> createList(){
        DefaultListModel<CoffeeData> model = new DefaultListModel<CoffeeData>();
        for(CoffeeData val : displayData)
            model.addElement(val);
        JList<CoffeeData> list = new JList<CoffeeData>(model);
        list.setCellRenderer(new ProductRenderer());
        return list;
    }
}
