package top.enatsu.clientgui;

import top.enatsu.util.FileInfoImpl;
import top.enatsu.util.ResponseImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class ToolBar extends JPanel {
    public JButton ancestorButton = new JButton("..");
    public JButton uploadButton = new JButton("上传");
    public JPanel buttonGroup = new JPanel();
    public JTextField pathText;
    public JFrame rootFrame;
    public void setRootFrame(JFrame root) {
        rootFrame = root;
    }
    public ToolBar(String path) {
        pathText = new JTextField(path);
        pathText.addActionListener(actionEvent -> MainFrame.reDirectTo(pathText.getText()));
        ancestorButton.addActionListener(actionEvent -> {
            String[] path1 = MainFrame.curPath.split("/");
            StringBuilder newPath = new StringBuilder();
            for (int i = 0; i< path1.length-1; i++) {
                newPath.append(path1[i]).append("/");
            }

            MainFrame.reDirectTo(newPath.toString().replace("//","/"));
        });
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc=new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.showDialog(new JLabel(), "选择文件");
                File file=jfc.getSelectedFile();
                byte[] content = new byte[(int) file.length()];
                BufferedInputStream inputStream = null;
                try {
                    inputStream = new BufferedInputStream(new FileInputStream(file));
                    inputStream.read(content);
                    FileInfoImpl info = new FileInfoImpl();
                    info.setInfo(file.getName(), MainFrame.curPath, content);
                    ResponseImpl response=MainFrame.fileManager.uploadFile(info);
                    JOptionPane.showMessageDialog(null, response.getMessage());
                    MainFrame.reDirectTo(MainFrame.curPath);
                } catch (IOException err) {
                    err.printStackTrace();
                    JOptionPane.showMessageDialog(null,err.toString());
                }
            }
        });

        setLayout(new BorderLayout());
        buttonGroup.setLayout(new GridLayout(1, 4));
        buttonGroup.add(ancestorButton);
        buttonGroup.add(uploadButton);

        add(buttonGroup, BorderLayout.WEST);
        add(pathText, BorderLayout.CENTER);
    }
    public void setAncestorButton(){
        if(MainFrame.curPath.equals("/")) ancestorButton.setEnabled(false);
        else ancestorButton.setEnabled(true);
    }
}