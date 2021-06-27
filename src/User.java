import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;

public class User {
    private String UID;
    private ArrayList<Integer> purchasedList;
    private ArrayList<Integer> unpurchasedList;

    public User() {
        purchasedList = new ArrayList<Integer>();
        unpurchasedList = new ArrayList<Integer>();
        Search.InitSearch();
    }

    public User(String UID) {
        this.UID = new String(UID);
        purchasedList = new ArrayList<Integer>();
        unpurchasedList = new ArrayList<Integer>();
        Search.InitSearch();
    }

    public boolean Purchased(int code){
        boolean ret = purchasedList.contains(code);
        System.out.println(ret? "Purchased":"notPurchased");
        return ret;
    }

    public void InitPurchasedList() {
        purchasedList = new ArrayList<Integer>();

        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
        }
        catch (Exception e) {
            System.out.println("getPurchasedIndex connection error");
            System.out.println(e.getMessage());
            return;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = String.format("Select 編號 from %s", UID);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                purchasedList.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Error in getPurchasedIndex");
            System.out.println(e.toString());
            return;
        } finally {
            try {
                rs.close();
                ps.close();
                rs = null;
                ps = null;
            } catch (SQLException e) {
                System.out.println("getPurchasedIndex-closing error");
                System.out.println(e.toString());
                return;
            }
        }
        
        unpurchasedList = Search.GetIndex();
        for (Integer i : purchasedList) {
            if (unpurchasedList.contains(i)) {
                unpurchasedList.remove(i);
            }
        }
        return;
    }

    
    /** 
     * @param targetOrigin 要搜尋的產地
     * @param targetVariety 要搜尋的品種
     * @param targetProcess 要搜尋的處理法
     * @param purchased 有沒有買過 (1: 購買過, 0: 不限制, -1: 沒有購買過)
     * @return ArrayList<Integer> (所以符合條件的咖啡編號)
     */
    public ArrayList<Integer> Search(String targetOrigin, String targetVariety, String targetProcess, int purchased) {
        if (purchased == 1) {
            return Search.SearchByAttr(targetOrigin, targetVariety, targetProcess, new ArrayList<Integer>(purchasedList));
        }
        if (purchased == -1) {
            return Search.SearchByAttr(targetOrigin, targetVariety, targetProcess, new ArrayList<Integer>(unpurchasedList));
        }
        return Search.SearchByAttr(targetOrigin, targetVariety, targetProcess, Search.GetIndex());
    } 

    
    /** 
     * @param items 要購買的咖啡編號
     * @return int 價格
     */
    public ArrayList<Integer> buy(ArrayList<Integer> items, int discount) {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        ret.add(0);
        int totalCost = 0;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;

        for (int i = 0; i < items.size(); i++) {
            try {
                con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
            }
            catch (Exception e) {
                System.out.println("buy - connection error");
                System.out.println(e.getMessage());
            }

            try {
                sql = "Select 售價, 剩餘包數, 品名 from coffee WHERE 編號 = ?";
                ps = con.prepareStatement(sql);
                ps.setInt(1, items.get(i));
                rs = ps.executeQuery();
                int price = rs.getInt(1);
                int remain = rs.getInt(2);
                String name = rs.getString(3);

                rs.close();
                ps.close();
                con.close();

                if (remain > 0) {
                    totalCost += price;

                    // 寫資料進該UID購買歷史的table
                    if (DataWriter.insertPurchaseHistory(this.UID, items.get(i))) {
                        this.purchasedList.add(items.get(i));
                        this.unpurchasedList.remove((Integer)items.get(i));
                    }

                    DataWriter.reduceStorage(items.get(i));
                }
                else {
                    System.out.println(name + "沒有庫存");
                }
            } catch (Exception e) {
                //TODO: handle exception
                System.out.println(e.toString());
            }
        }
        totalCost -= discount;
        int member = 0;

        try {
            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
        }
        catch (Exception e) {
            System.out.println("buy - connection error");
            System.out.println(e.getMessage());
        }

        try {
            sql = String.format("Select 點數, 會員 from users WHERE 帳號 = ?");
            ps = con.prepareStatement(sql);
            ps.setString(1, UID);
            rs = ps.executeQuery();
            int point = rs.getInt(1);
            member = rs.getInt(2);
            if (member == 1)
                totalCost *= 0.9;
            //System.out.println(point);

            sql = String.format("UPDATE users SET 點數 = %d WHERE 帳號 = ?", point + totalCost/10);
            ps = con.prepareStatement(sql);
            ps.setString(1, UID);
            ps.execute();
            //System.out.println(sql);
            
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println(e.toString());
        } 
        if (member == 0 && totalCost >= 1000) {
            try {
                con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
            }
            catch (Exception e) {
                System.out.println("buy - connection error");
                System.out.println(e.getMessage());
            }

            try {
                sql = "UPDATE users SET 會員 = 1 WHERE 帳號 = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, UID);
                ps.execute();
                //System.out.println(sql);
                
                rs.close();
                ps.close();
                con.close();
                System.out.println("恭喜成為會員");
                ret.add(1);
            } catch (Exception e) {
                //TODO: handle exception
                System.out.println(e.toString());
            } 
        }
        ret.set(0, totalCost);
        return ret;
    }

    
    /** 
     * @return ArrayList<Integer> 推薦咖啡編號列表
     */
    public ArrayList<Integer> getRecommand() {
        String targetOrigin, Origin;
        String targetProcess, Process;
        String targetVariety, Variety;
        int[][] similarScore = new int[unpurchasedList.size()][2];
        Admin admin = new Admin();

        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
        }
        catch (Exception e) {
            System.out.println("getPurchasedIndex connection error");
            System.out.println(e.getMessage());
            return null;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        try {
            for (int i = 0; i < purchasedList.size(); i++) {
                sql = String.format("Select 產地 from coffee where 編號 = ?");
                ps = con.prepareStatement(sql);
                ps.setInt(1, purchasedList.get(i));
                rs = ps.executeQuery();
                targetOrigin = rs.getString(1);
                for (int j = 0; j < unpurchasedList.size(); j++) {
                    sql = String.format("Select 產地 from coffee where 編號 = ?");
                    ps = con.prepareStatement(sql);
                    ps.setInt(1, unpurchasedList.get(j));
                    rs = ps.executeQuery();
                    Origin = rs.getString(1);
                    similarScore[j][0] = unpurchasedList.get(j);
                    similarScore[j][1] = 0;
                    if (targetOrigin.equals(Origin))
                        similarScore[j][1] += admin.getParameter()[0];
                        
                    rs.close();
                    ps.close();
                }

                sql = String.format("Select 處理法 from coffee where 編號 = ?");
                ps = con.prepareStatement(sql);
                ps.setInt(1, purchasedList.get(i));
                rs = ps.executeQuery();
                targetProcess = rs.getString(1);
                for (int j = 0; j < unpurchasedList.size(); j++) {
                    sql = String.format("Select 處理法 from coffee where 編號 = ?");
                    ps = con.prepareStatement(sql);
                    ps.setInt(1, unpurchasedList.get(j));
                    rs = ps.executeQuery();
                    Process = rs.getString(1);
                    if (targetProcess.equals(Process))
                        similarScore[j][1] += admin.getParameter()[1];
                    
                    rs.close();
                    ps.close();
                }

                sql = String.format("Select 品種 from coffee where 編號 = ?");
                ps = con.prepareStatement(sql);
                ps.setInt(1, purchasedList.get(i));
                rs = ps.executeQuery();
                targetVariety = rs.getString(1);
                for (int j = 0; j < unpurchasedList.size(); j++) {
                    sql = String.format("Select 品種 from coffee where 編號 = ?");
                    ps = con.prepareStatement(sql);
                    ps.setInt(1, unpurchasedList.get(j));
                    rs = ps.executeQuery();
                    Variety = rs.getString(1);
                    if (targetVariety.equals(Variety))
                        similarScore[j][1] += admin.getParameter()[2];
                    
                    rs.close();
                    ps.close();
                }
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Error in getPurchasedIndex");
            System.out.println(e.toString());
            return null;
        }
        ArrayList<Integer> ret = new ArrayList<Integer>();
        Sort2DArrayBasedOnColumnNumber(similarScore, 2);
        // System.out.printf("\n%d\n", unpurchasedList.size());
        for (int i = 0; i < unpurchasedList.size(); i++) {
            ret.add(similarScore[i][0]);
        }
        return ret;
    }

    
    /** 
     * @param array 比對大小的陣列
     * @param columnNumber 要拿來比較的column index
     */
    private void Sort2DArrayBasedOnColumnNumber (int[][] array, final int columnNumber){
        Arrays.sort(array, new Comparator<int[]>() {
            @Override
            public int compare(int[] first, int[] second) {
               if(first[columnNumber-1] > second[columnNumber-1]) return 1;
               else return -1;
            }
        });
    }
	
	
    /** 
     * @param id 咖啡編號
     * @param score 評的分數
     * @return boolean 是否成功
     */
    public boolean rateCoffee(int id, int score) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		double originalScore = 0.0;
		int numOfPeople = 0;
		double newScore = 0.0;
		boolean success = false;
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db"); // connecting to our database
			System.out.println("Connected!");
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println(e + "");
		}
		
		try {
			// Use sqlite statement to get satisfied data
			String sql = String.format("SELECT 評分,評分人數 FROM coffee WHERE 編號 = %d", id);
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {
				originalScore = rs.getDouble("評分");
				numOfPeople = rs.getInt("評分人數");
			}
			
			newScore = (originalScore * numOfPeople + score) / (numOfPeople + 1);			
		} catch (SQLException e) {
			System.out.println(e.toString());
			return success;
		}
		
		try {	
			String sql = String.format("UPDATE coffee SET 評分 = %f, 評分人數 = %d WHERE 編號 = %d", newScore, numOfPeople + 1, id);
			ps = con.prepareStatement(sql);
			ps.execute();
			success = true;
		} catch (SQLException e) {
			System.out.println(e.toString());
			return false;
		} finally {
			try {
				rs.close();
				ps.close();
				con.close();
			} catch (SQLException e) {
				System.out.println(e.toString());
				return false;
			}
		}
		return success;
	}

    public int getPoint() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
        }
        catch (Exception e) {
            System.out.println("buy - connection error");
            System.out.println(e.getMessage());
        }

        int point = 0;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        try {
            sql = "Select 點數 from users WHERE 帳號 = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, UID);
            rs = ps.executeQuery();
            point = rs.getInt(1);

            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println(e.toString());
        } 
        return point;
    }
}