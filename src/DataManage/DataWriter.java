package DataManage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.File;
import java.io.FileReader;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.json.JSONObject;
import java.util.ArrayList;
import java.security.NoSuchAlgorithmException;
import Coffee.CoffeeData;
public class DataWriter {
    public static void createDatabase() {
        Connection con = null;
        try {
            File dir = new File("../database");
            if (!dir.exists()) {
                dir.mkdir();
            }

            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
            Statement stat = con.createStatement();
            stat.setQueryTimeout(30);
            stat.executeUpdate("create table if not exists users ("
                            + "帳號 text not null, "
                            + "密碼 text not null, "
                            + "email text, "
                            + "會員 integer not null, "
                            + "身分 integer not null, "
                            + "點數 integer not null)");
            
            stat.executeUpdate("create table if not exists coffee ("
                            + "編號 integer not null, "
                            + "品名 text not null, "
                            + "產地 text, "
                            + "區域 text, "
                            + "處理法 text not null, "
                            + "產季 integer, "
                            + "品種 text, "
                            + "風味敘述 text, "
                            + "售價 integer, "
                            + "剩餘包數 integer, "
                            + "評分 double, "
                            + "評分人數 integer)");

        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("Database creation fault");
            System.out.println(e.getMessage());
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                //TODO: handle exception
                System.out.println("createDatabase - closing part");
                System.out.println(e.toString());
            }
        }
    }

    public static boolean reduceStorage(int id) {
        boolean success = false;

        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
        }
        catch (Exception e) {
            System.out.println("reduceStorage connection error");
            System.out.println(e.getMessage());
            return success;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        int storage = 0;
        try {
            String sql = "Select 剩餘包數 from coffee Where 編號 = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            storage = rs.getInt(1);

            rs.close();
            ps.close();
            con.close();
            
        } catch (SQLException e) {
            System.out.println("Error in get 剩餘包數");
            System.out.println(e.toString());
            try {
                con.close();
            } catch (SQLException sqe) {
                System.out.println("con closing error");
                System.out.println(sqe.toString());
            }
            return success;
        } 

        if (storage > 0) {
            storage--;
            try {
                con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
            }
            catch (Exception e) {
                System.out.println("reduceStorage connection error");
                System.out.println(e.getMessage());
                return success;
            }

            try {
                String sql = "Update coffee set 剩餘包數 = ? WHERE 編號 = ?";
                ps = con.prepareStatement(sql);
                ps.setInt(1, storage);
                ps.setInt(2, id);
                ps.execute();
                success = true;

                ps.close();
                con.close();
            } catch (SQLException e) {
                //TODO: handle exception
                System.out.println("Error in updating 剩餘包數");
                System.out.println(e.toString());
                try {
                    con.close();
                } catch (SQLException sqe) {
                    System.out.println("con closing error");
                    System.out.println(sqe.toString());
                }
                return success;
            } 
            return success;
        }
        else {
            try {
                con.close();
            } catch (SQLException sqe) {
                System.out.println("con closing error");
                System.out.println(sqe.toString());
            }
            return success;
        }
    }

    public static CoffeeData getCoffeeInfo(int id) {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");

            PreparedStatement ps = null;
            ResultSet rs = null;
            String sql = "Select * from coffee WHERE 編號 = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            CoffeeData ret = new CoffeeData(id, rs.getString(2), rs.getInt(6), rs.getString(7), rs.getString(3), rs.getString(4), rs.getString(5),
            rs.getString(8), rs.getInt(9), rs.getInt(10), rs.getDouble(11), rs.getInt(12));

            rs.close();
            ps.close();
            con.close();
            return ret;

        }
        catch (SQLException e) {
            System.out.println("getInfo error");
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static boolean register(String UID, String password, String email, int subscribed, int identity) {
        boolean success = true;
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
        }
        catch (Exception e) {
            System.out.println("register connection error");
            System.out.println(e.getMessage());
            return false;
        }


        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "Select * from users WHERE 帳號 = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, UID);
            rs = ps.executeQuery();
            if (rs.next()) {
                // 有同樣username的用戶
                success = false;
                rs.close();
                ps.close();
                con.close();
                return success;
            }
            else {
                ps.close();
                sql = "Insert into users(帳號, 密碼, email, 會員, 點數, 身分) Values(?,?,?,?,?,?)";
                ps = con.prepareStatement(sql);
                ps.setString(1, UID);

                String hashedpassword = new String();
                try {
                    hashedpassword = Encryptor.encryption(password);
                    // System.out.println(hashedPassword);
                } catch (NoSuchAlgorithmException e1) {
                    e1.printStackTrace();
                }

                ps.setString(2, hashedpassword);
                ps.setString(3, email);
                ps.setInt(4, subscribed);
                ps.setInt(5, 0);
                ps.setInt(6, identity);

                ps.execute();
                ps.close();
                
                Statement stat = con.createStatement();
                stat.setQueryTimeout(30);
                stat.executeUpdate(String.format("create table if not exists %s(編號 integer not null)", UID));
                con.close();
            }

        } catch (SQLException e) {
            //TODO: handle exception
            System.out.println("register-insertion error");
            System.out.println(e.toString());
            try {
                con.close();
            } catch (SQLException sqe) {
                System.out.println("con closing error");
                System.out.println(sqe.toString());
            }
            return false;
        }
        return success;
    }

    public static boolean register(String UID, String password, int subscribed, int identity) {
        return register(UID, password, null, subscribed, identity);
    }

    public static boolean insertPurchaseHistory(String UID, int item) {
        boolean modified = false;
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
            String sql = String.format("Select 編號 from %s", UID);
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            boolean bought = false;
            while (rs.next()) {
                if (rs.getInt(1) == item) {
                    bought = true;
                    break;
                }
            }
            rs.close();
            ps.close();

            // 如果沒買過，insert編號
            if (!bought) {
                sql = String.format("Insert into %s(編號) Values(?)", UID);
                ps = con.prepareStatement(sql);
                ps.setInt(1, item);
                ps.execute();
                modified = true;
            }

            con.close();
        }
        catch (SQLException e) {
            System.out.println("insertPurchaseHistory fault");
            System.out.println(e.toString());
            return modified;
        }

        return modified;
    }

    public static boolean coffeeInsert(int id, String name, String continent, String region, String process, int year, String species,
    String flavor, int price, int remain) {
        boolean success = false;
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
        }
        catch (Exception e) {
            System.out.println("register connection error");
            System.out.println(e.getMessage());
            return success;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "Select * from coffee WHERE 編號 = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                // 有同樣username的用戶
                success = false;
                rs.close();
                ps.close();
                con.close();
            } 
            else {
                rs.close();
                ps.close();
                sql = "Insert into coffee(編號, 品名, 產地, 區域, 處理法, 產季, 品種, 風味敘述, 售價, 剩餘包數, 評分, 評分人數) Values(?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = con.prepareStatement(sql);
                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setString(3, continent);
                ps.setString(4, region);
                ps.setString(5, process);
                ps.setInt(6, year);
                ps.setString(7, species);
                ps.setString(8, flavor);
                ps.setInt(9, price);
                ps.setInt(10, remain);
                ps.setDouble(11, 0.0);
                ps.setInt(12, 0);
                if (ps.execute()) 
                    success = true;

                ps.close();
                con.close();
            }
        } catch (SQLException e) {
            //TODO: handle exception
            System.out.println("Error in coffee insertion");
            System.out.println(e.toString());
            try {
                con.close();
            } catch (SQLException sqe) {
                System.out.println("con closing error");
                System.out.println(sqe.toString());
            }
            return success;
        } 
        return success;
    }

    public static boolean loginChecker(String UID, String password) {
        boolean success = false;
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
            String sql = "Select 帳號, 密碼 from users WHERE 帳號 = ? and 密碼 = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, UID);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                success = true;
            }

            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            //TODO: handle exception
            System.out.println("loginChecker fault");
            System.out.println(e.toString());
        }

        return success;
    }

    public static void coffeeInit() {
        try (FileReader reader = new FileReader("..coffeeData/coffee.json")) {
            JSONArray List = (JSONArray) (new JSONTokener(reader).nextValue());
            for (Object o1 : List) {
                JSONObject t1 = (JSONObject) o1;
                int id = Integer.parseInt(t1.getString("編號"));
                String name = t1.getString("品名");
                String continent = t1.getString("產地");
                String region = t1.getString("區域");
                String process = t1.getString("處理法");
                int year = Integer.parseInt(t1.getString("產季"));
                String species = t1.getString("品種");
                String flavor = t1.getString("風味敘述");
                int price = Integer.parseInt(t1.getString("售價"));
                int remain = Integer.parseInt(t1.getString("剩餘包數"));

                coffeeInsert(id, name, continent, region, process, year, species, flavor, price, remain);


            }
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("coffeeInit fault");
            System.out.println(e.toString());
        }
    }

    public static void coffeeInit(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JSONArray List = (JSONArray) (new JSONTokener(reader).nextValue());
            for (Object o1 : List) {
                JSONObject t1 = (JSONObject) o1;
                int id = Integer.parseInt(t1.getString("編號"));
                String name = t1.getString("品名");
                String continent = t1.getString("產地");
                String region = t1.getString("區域");
                String process = t1.getString("處理法");
                int year = Integer.parseInt(t1.getString("產季"));
                String species = t1.getString("品種");
                String flavor = t1.getString("風味敘述");
                int price = Integer.parseInt(t1.getString("售價"));
                int remain = Integer.parseInt(t1.getString("剩餘包數"));

                coffeeInsert(id, name, continent, region, process, year, species, flavor, price, remain);


            }
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("coffeeInit fault");
            System.out.println(e.toString());
        }
    }
    public static boolean updateInventory(int id, int num) {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
            PreparedStatement ps = null;
            String sql = "UPDATE coffee set 剩餘包數 = ? WHERE 編號 = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, num);
            ps.setInt(2, id);
            ps.execute();

            ps.close();
            con.close();
        }
        catch (SQLException e) {
            System.out.println("updateInventory error");
            System.out.println(e.getMessage());
        }
        return true;
    }

    public static boolean updatePrice(int id, int newPrice) {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
            PreparedStatement ps = null;
            String sql = "UPDATE coffee set 售價 = ? WHERE 編號 = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, newPrice);
            ps.setInt(2, id);

            ps.execute();
            ps.close();
            con.close();
        }
        catch (SQLException e) {
            System.out.println("updateInventory error");
            System.out.println(e.getMessage());
        }
        return true;
    }
    
    public static void main(String[] args) {
        createDatabase();
        coffeeInit();
        register("jjk846505", "123456", "frankyang6668@gmail.com", 0, 1);
        register("barista", "777777", 0, 0);
        register("ilovekinu", "666666", "aaa@gmail.com", 0, 0);
        register("q", "q", "hi@gmail.com", 0, 0);
        // coffeeInsert(1009, "Geisha, Finca Takesi", "美洲", "Yanacachi, Bolivia", "水洗法", 2020, "Geisha", "茉莉、佛手柑、荔枝、蜂蜜、複雜", 700, 20);
        // coffeeInsert(1010, "Geisha, Finca Takesi", "非洲", "Yanacachi, Bolivia", "水洗法", 2020, "Geisha", "茉莉、佛手柑、荔枝、蜂蜜、複雜", 700, 20);
        // coffeeInsert(1011, "Geisha, Finca Takesi", "美洲", "Yanacachi, Bolivia", "水洗法", 2020, "Geisha", "茉莉、佛手柑、荔枝、蜂蜜、複雜", 700, 20);
        // reduceStorage(1010);
        //System.out.println(loginChecker("ilovekinu", "666666"));
        // User testUser = new User("barista");
        // System.out.println(testUser.UID);
        // testUser.UID = new String("barista");
        // System.out.println(testUser.UID);
        // testUser.InitPurchasedList();
        // ArrayList<Integer> items = new ArrayList<Integer>();
        // items.add(1001);
        // testUser.buy(items);
        // items = testUser.Search(null, "其他", null, 0);
        /*
        for (Integer i : items)
            System.out.println(i);
        */
        
        // for (Integer i : testUser.purchasedList)
        //     System.out.println(i);
        // System.out.println("-----");
        // for (Integer i : testUser.unpurchasedList)
        //     System.out.println(i);
        
        // ArrayList<Integer> items = new ArrayList<Integer>();
        // items.add(1009);
        // items.add(1010);
        // testUser.buy(items);

        // System.out.println("=====");
        // for (Integer i : testUser.purchasedList)
        //     System.out.println(i);
        // System.out.println("-----");
        // for (Integer i : testUser.unpurchasedList)
        //     System.out.println(i);

        // System.out.println("=====");
        // items = testUser.getRecommand();
        // for (Integer i : items)
        //     System.out.println(i);
            
        // System.out.println("=====");
        // for (Integer i : testUser.Search("非洲", null, "水洗法", 0))
        //     System.out.println(i);
    }
}
