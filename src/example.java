import javax.swing.*;

public class example {

    public static void main(String[] args) {
        //使用 invokeLater 確保 UI 在排程執行緒內
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // LoginFrame f;
                new LoginFrame();
            }
        });
    }
}
