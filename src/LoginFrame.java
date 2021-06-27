import javax.swing.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class LoginFrame {
    JLabel userLabel;
    JTextField userText;
    JLabel passwordLabel;
    JPasswordField passwordText;
    JButton loginButton;
    JPanel panel;
    JFrame frame;
    JOptionPane success;
    PasswordChecker passwordChecker = new PasswordChecker();

    private class loginHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            // System.out.println("yes");
        	String user = userText.getText();
        	String rawPassword = new String(passwordText.getPassword());
            System.out.println(e.getActionCommand());
            System.out.printf("user name: %s\n", user);
            System.out.printf("rawpassword: %s size: %d\n", rawPassword, rawPassword.length());
            String hashedPassword = "";
            try {
                hashedPassword = Encryptor.encryption(rawPassword);
				// System.out.println(hashedPassword);
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			}
            
            if(DataWriter.loginChecker(user, hashedPassword)) {
            	System.out.println("yes");
            	JOptionPane.showMessageDialog(frame, "login success");
            	frame.setVisible(false);
                // change to home frame
            	// HomeFrame hf;
                new HomeFrame(user);
            }
            else {
            	System.out.println("login fail!");
            	JOptionPane.showMessageDialog(frame, "login failed");
            }
        }
    }
    public void placeComponents(JPanel panel) {
        panel.setLayout(null);

        userLabel = new JLabel("User:");
        userLabel.setBounds(10,20,80,25);
        panel.add(userLabel);

        userText = new JTextField(20);
        userText.setBounds(100,20,165,25);
        panel.add(userText);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10,50,80,25);
        panel.add(passwordLabel);

        passwordText = new JPasswordField(20);
        passwordText.setBounds(100,50,165,25);
        panel.add(passwordText);

        loginButton = new JButton("login");
        loginButton.setBounds(110, 86, 80, 25);
        loginButton.addActionListener(new loginHandler());
        panel.add(loginButton);
    }
    public LoginFrame(){
        frame = new JFrame("Coffee");
        frame.setSize(350, 200);
        frame.setResizable(false);
        frame.setLocation(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        frame.getContentPane().add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }
}
