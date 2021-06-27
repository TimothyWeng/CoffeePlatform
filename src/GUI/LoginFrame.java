package GUI;

import javax.swing.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import DataManage.DataWriter;
import DataManage.Encryptor;
import User.User;
public class LoginFrame {
    JLabel userLabel;
    JTextField userText;
    JLabel passwordLabel;
    JPasswordField passwordText;
    JButton loginButton;
    JButton registerButton;
    JPanel panel;
    JFrame frame;
    JOptionPane success;

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

                User checkAdmin = new User(user);
                if(checkAdmin.IsAdmin()){
                    System.out.println("Admin");
                    new AdminFrame(user);
                }
                else{
                    System.out.println("not Admin");
                    // change to home frame
                    // HomeFrame hf;
                    new HomeFrame(user);
                }
            }
            else {
            	System.out.println("login fail!");
            	JOptionPane.showMessageDialog(frame, "login failed");
            }
        }
    }
    
    private class registerHandler implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            registerFrame f = new registerFrame();
            
        }
    }
    public void placeComponents(JPanel panel) {
        panel.setLayout(null);

        userLabel = new JLabel("帳號：");
        userLabel.setBounds(10,20,80,25);
        panel.add(userLabel);

        userText = new JTextField(20);
        userText.setBounds(100,20,165,25);
        panel.add(userText);

        passwordLabel = new JLabel("密碼：");
        passwordLabel.setBounds(10,50,80,25);
        panel.add(passwordLabel);

        passwordText = new JPasswordField(20);
        passwordText.setBounds(100,50,165,25);
        panel.add(passwordText);

        loginButton = new JButton("登入");
        loginButton.setBounds(110, 90, 80, 25);
        loginButton.addActionListener(new loginHandler());
        panel.add(loginButton);

        registerButton = new JButton("註冊");
        registerButton.setBounds(110, 130, 80, 25);
        registerButton.addActionListener(new registerHandler());
        panel.add(registerButton);
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
