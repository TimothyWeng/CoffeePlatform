import javax.swing.*;
import GUI.LoginFrame;
import DataManage.DataWriter;
public class example {

    public static void main(String[] args) {
        DataWriter.createDatabase();
        DataWriter.register("admin", "admin", "b08902038@ntu.edu.tw", 0, 1);
        //使用 invokeLater 確保 UI 在排程執行緒內
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // LoginFrame f;
                new LoginFrame();
            }
        });
    }
}
