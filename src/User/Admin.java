package User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import DataManage.DataWriter;
public class Admin extends User {
    private int[] recommandParameter = new int []{10, 8, 6};

    public Admin() {super();}
    public Admin(String UID) {super(UID);}

    public boolean addCoffee(String name, String continent, String region, String process, int year, String species,
    String flavor, int price, int remain) {
        boolean success = false;
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
            PreparedStatement ps = con.prepareStatement("Select 編號 from coffee");
            ResultSet rs = ps.executeQuery();
            int maxId = 0;
            while (rs.next()) {
                int id = rs.getInt(1);
                if (maxId <= id) {
                    maxId = id + 1;
                }
            }
            rs.close();
            ps.close();
            con.close();

            success = DataWriter.coffeeInsert(maxId, name, continent, region, process, year, species, flavor, price, remain);
    
        } catch (SQLException e) {
            //TODO: handle exception
            System.out.println(e.toString());
        }
        return success;
    }
    
    public void setParameter(int a, int b, int c) {
        recommandParameter[0] = a;
        recommandParameter[1] = b;
        recommandParameter[2] = c;
    }

    /**
     * 把現有的庫存補貨
     * @param id
     * 要捕貨的指定編號
     * @param addNum
     * 捕獲量
     * @return
     * 成功與否
     */
    public boolean addInventory(int id, int addNum) {
        boolean success = false;
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
            PreparedStatement ps = con.prepareStatement("Select 剩餘包數 from coffee WHERE 編號 = ?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            int oldInventory = rs.getInt(1);
            oldInventory += addNum;
            
            rs.close();
            ps.close();
            con.close();
            
            success = DataWriter.updateInventory(id, oldInventory);
    
        } catch (SQLException e) {
            //TODO: handle exception
            System.out.println(e.toString());
        }
        return success;
    }

    /**
     * 更新指定編號的售價
     */
    public boolean setPrice(int id, int newPrice) {
        return DataWriter.updatePrice(id, newPrice);
    }

    public int[] getParameter() {
        int[] ret = new int []{recommandParameter[0], recommandParameter[1], recommandParameter[2]};
        // for (int i = 0; i < 3; i++) {
        //     System.out.println(i);
        // }
        return ret;
    }

    // public static void main(String[] args) {
    //     Admin u = new Admin("jjk846505");
    //     u.addCoffee("123", "46", "46", "46", 2021, "46", "46", 300, 10);
    // }
}
