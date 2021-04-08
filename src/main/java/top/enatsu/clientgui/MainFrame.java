package top.enatsu.clientgui;

import top.enatsu.facade.FileManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.*;
import java.awt.*;

import javax.naming.NamingException;
import java.rmi.RemoteException;

public class MainFrame extends JFrame {
    static String curPath = "/";

    public static String url = "rmi://127.0.0.1:5000/file"; //默认

    public static FileManager fileManager = null;

    public static FileListPane dirView = null;

    public static ToolBar toolBar = new ToolBar(curPath);

    public static void reDirectTo(String path) {
        curPath=path;
        dirView.ls(curPath);
        toolBar.pathText.setText(curPath);
        toolBar.setAncestorButton();
    }

    public MainFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(dirView, BorderLayout.CENTER);
        add(toolBar, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        String newUrl = JOptionPane.showInputDialog(null,
                "请输入远程RMI地址", url);
        if (!newUrl.equals("")) {
            url = newUrl;
        }
        try {
            Context namingContext = new InitialContext();
            fileManager = (FileManager) namingContext.lookup(url);
            String message = fileManager.hello();
            JOptionPane.showMessageDialog(null, message);
            dirView = new FileListPane(curPath);
        } catch (NamingException | RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,e);
        }
        MainFrame frame = new MainFrame();
        frame.setTitle("RMI远程文件管理器 ["+url+"]");
        frame.setSize(600, 480);
        frame.setVisible(true);
    }
}
