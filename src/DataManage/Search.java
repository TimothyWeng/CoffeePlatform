package DataManage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Search {
    public static ArrayList<String> varieties = new ArrayList<String>();
    public static ArrayList<String> processes = new ArrayList<String>();

    public static void InitSearch() {
        varieties.add("Geisha");
        varieties.add("SL28");
        varieties.add("Heirloom");
        varieties.add("Typica");
        varieties.add("Bourbon");
        processes.add("日曬法");
        processes.add("水洗法");
        processes.add("蜜處理");
    }

    public static ArrayList<Integer> SearchByAttr(String targetOrigin, String targetVariety, String targetProcess, ArrayList<Integer> List) {
        if (targetOrigin != null) {
            List = SearchByOrigin(targetOrigin, List);
        }
        // for (Integer i : List)
        //     System.out.println(i);
        // System.out.println();
        if (targetVariety != null) {
            List = SearchByVariety(targetVariety, List);
        }
        // for (Integer i : List)
        //     System.out.println(i);
        // System.out.println();
        if (targetProcess != null) {
            List = SearchByProcess(targetProcess, List);
        }
        return List;
    }

    public static ArrayList<Integer> SearchByOrigin(String targetOrigin, ArrayList<Integer> List) {
        ArrayList<Integer> success = null;
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
        }
        catch (Exception e) {
            System.out.println("SearchByOrigin connection error");
            System.out.println(e.getMessage());
            return success;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        String Origin = new String();
        try {
            for (int i = 0; i < List.size(); i++) {
                String sql = "Select 產地 from coffee Where 編號 = ?";
                ps = con.prepareStatement(sql);
                ps.setInt(1, List.get(i));
                rs = ps.executeQuery();
                Origin = rs.getString(1);
                if (!targetOrigin.equals(Origin)) {
                    List.remove(i);
                    i--;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in SearchByOrigin");
            System.out.println(e.toString());
            return success;
        } finally {
            try {
                rs.close();
                ps.close();
                con.close();
                rs = null;
                ps = null;
            } catch (SQLException e) {
                System.out.println("SearchByOrigin-closing error");
                System.out.println(e.toString());
                return success;
            }
        }
        return List;
    }

    public static ArrayList<Integer> SearchByVariety(String targetVariety, ArrayList<Integer> List) {
        ArrayList<Integer> success = null;
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
        }
        catch (Exception e) {
            System.out.println("SearchByVariety connection error");
            System.out.println(e.getMessage());
            return success;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        String Variety = new String();
        try {
            for (int i = 0; i < List.size(); i++) {
                String sql = "Select 品種 from coffee Where 編號 = ?";
                ps = con.prepareStatement(sql);
                ps.setInt(1, List.get(i));
                rs = ps.executeQuery();
                Variety = rs.getString(1);
                if (targetVariety.equals("其他")) {
                    for (int j = 0; j < varieties.size(); j++) {
                        if (varieties.get(j).equals(Variety)) {
                            List.remove(i);
                            i--;
                            break;
                        }
                    }
                }
                else {
                    if (!targetVariety.equals(Variety)) {
                        List.remove(i);
                        i--;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in SearchByVariety");
            System.out.println(e.toString());
            return success;
        } finally {
            try {
                rs.close();
                ps.close();
                con.close();
                rs = null;
                ps = null;
            } catch (SQLException e) {
                System.out.println("SearchByVariety-closing error");
                System.out.println(e.toString());
                return success;
            }
        }
        return List;
    }

    public static ArrayList<Integer> SearchByProcess(String targetProcess, ArrayList<Integer> List) {
        ArrayList<Integer> success = null;
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
        }
        catch (Exception e) {
            System.out.println("SearchByProcess connection error");
            System.out.println(e.getMessage());
            return success;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        String Process = new String();
        try {
            for (int i = 0; i < List.size(); i++) {
                String sql = "Select 處理法 from coffee Where 編號 = ?";
                ps = con.prepareStatement(sql);
                ps.setInt(1, List.get(i));
                rs = ps.executeQuery();
                Process = rs.getString(1);
                if (targetProcess.equals("其他")) {
                    for (int j = 0; j < processes.size(); j++) {
                        if (processes.get(j).equals(Process)) {
                            List.remove(i);
                            i--;
                            break;
                        }
                    }
                }
                else {
                    if (!targetProcess.equals(Process)) {
                        List.remove(i);
                        i--;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in SearchByProcess");
            System.out.println(e.toString());
            return success;
        } finally {
            try {
                rs.close();
                ps.close();
                con.close();
                rs = null;
                ps = null;
            } catch (SQLException e) {
                System.out.println("SearchByProcess-closing error");
                System.out.println(e.toString());
                return success;
            }
        }
        return List;
    }

    public static ArrayList<Integer> GetIndex() {
        ArrayList<Integer> success = null;
        ArrayList<Integer> ret = new ArrayList<Integer>();
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:sqlite:database/UsersAndCoffee.db");
        }
        catch (Exception e) {
            System.out.println("getIndex connection error");
            System.out.println(e.getMessage());
            return success;
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "Select 編號 from coffee";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ret.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            System.out.println("Error in getIndex");
            System.out.println(e.toString());
            return success;
        } finally {
            try {
                rs.close();
                ps.close();
                con.close();
                rs = null;
                ps = null;
            } catch (SQLException e) {
                System.out.println("getIndex-closing error");
                System.out.println(e.toString());
                return success;
            }
        }
        return ret;
    }
}
