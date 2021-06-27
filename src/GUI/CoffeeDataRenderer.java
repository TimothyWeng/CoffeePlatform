package GUI;

import javax.swing.*;
import java.awt.*;
import Coffee.CoffeeData;
/**
 * Define basic UI settings of Java Panel, Java Button, Java List
 */
public class CoffeeDataRenderer extends JPanel implements ListCellRenderer<CoffeeData> {
    private Color LIGHT_GRAY = new Color(237, 237, 237);

    private JLabel lbIcon = new JLabel();
    private JLabel lbName = new JLabel();
    private JLabel lbPrice = new JLabel();
    private JLabel lbOrigin = new JLabel();
    private JLabel lbMethod = new JLabel();
    private JLabel lbVariety = new JLabel();
    private JLabel lbRate = new JLabel();
    JPanel panelRight;
    JPanel panelRightCenter;
    JPanel panelTop;

    /**
     * default constructor of CoffeeDataRenderer
     */
    public CoffeeDataRenderer() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(0, 0));
        // setBorder(new EmptyBorder(0, 0, 2, 0));
        panelRight = new JPanel(new BorderLayout(0, 0));
        panelRightCenter = new JPanel(new GridLayout(3, 2));
        panelTop = new JPanel(new BorderLayout());
        // panelRight.setBorder(new EmptyBorder(1, 2, 3, 2));

        panelRightCenter.add(new JLabel("產區", JLabel.CENTER));
        panelRightCenter.add(lbOrigin);
        panelRightCenter.add(new JLabel("處理法", JLabel.CENTER));
        panelRightCenter.add(lbMethod);
        panelRightCenter.add(new JLabel("品種", JLabel.CENTER));
        panelRightCenter.add(lbVariety);

        panelTop.add(lbName, BorderLayout.WEST);
        panelTop.add(lbRate, BorderLayout.EAST);
        panelRight.add(panelTop, BorderLayout.NORTH);
        panelRight.add(panelRightCenter, BorderLayout.CENTER);
        panelRight.add(lbPrice, BorderLayout.EAST);
        add(panelRight, BorderLayout.CENTER);
        add(lbIcon, BorderLayout.WEST);
    }

    /**
     * override all settings in Java List CoffeeData
     * 
     * @param list
     * @param coffeeData
     * @param index
     * @param isSelected
     * @param cellHasFocus
     * @return Component getListCellRendererComponent
     */
    @Override
    public Component getListCellRendererComponent(JList<? extends CoffeeData> list, CoffeeData coffeeData, int index,
            boolean isSelected, boolean cellHasFocus) {
        ImageIcon icon = new ImageIcon("src/image/" + Integer.toString(coffeeData.getCode()) + "-2.png");
        lbIcon.setIcon(icon);
        lbIcon.setVerticalAlignment(JLabel.TOP);
        lbName.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        
        lbName.setText(coffeeData.getName());
        lbName.setHorizontalAlignment(JLabel.LEFT);
        lbName.setVerticalAlignment(JLabel.BOTTOM);
        
        lbPrice.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
        lbPrice.setText("$" + coffeeData.getPrice());
        lbPrice.setHorizontalAlignment(JLabel.RIGHT);
        lbPrice.setVerticalAlignment(JLabel.BOTTOM);
        lbOrigin.setText(coffeeData.getProductionArea());
        lbMethod.setText(coffeeData.getProcessMethod());
        lbVariety.setText(coffeeData.getVariety());
        lbRate.setText("Rating:%.2f".formatted(coffeeData.getRate()));
        lbRate.setVerticalAlignment(JLabel.BOTTOM);
        
        lbName.setOpaque(true);
        lbOrigin.setOpaque(true);
        lbMethod.setOpaque(true);
        lbVariety.setOpaque(true);
        lbIcon.setOpaque(true);
        lbPrice.setOpaque(true);
        lbRate.setOpaque(true);

        // when select item
        if (isSelected) {
            lbName.setBackground(list.getSelectionBackground());
            panelRightCenter.setBackground(list.getSelectionBackground());
            panelTop.setBackground(list.getSelectionBackground());
            lbOrigin.setBackground(list.getSelectionBackground());
            lbMethod.setBackground(list.getSelectionBackground());
            lbIcon.setBackground(list.getSelectionBackground());
            lbVariety.setBackground(list.getSelectionBackground());
            lbPrice.setBackground(list.getSelectionBackground());
            lbRate.setBackground(list.getSelectionBackground());

            setBackground(list.getSelectionBackground());
        } else { // when don't select
            lbName.setBackground(LIGHT_GRAY);
            panelRightCenter.setBackground(LIGHT_GRAY);
            panelTop.setBackground(LIGHT_GRAY);
            lbOrigin.setBackground(LIGHT_GRAY);
            lbMethod.setBackground(LIGHT_GRAY);
            lbVariety.setBackground(LIGHT_GRAY);
            lbIcon.setBackground(LIGHT_GRAY);
            lbPrice.setBackground(LIGHT_GRAY);
            panelRight.setBackground(LIGHT_GRAY);
            lbPrice.setBackground(LIGHT_GRAY);
            lbRate.setBackground(LIGHT_GRAY);
            // setBackground(Color.BLACK);
        }
        return this;
    }
}
