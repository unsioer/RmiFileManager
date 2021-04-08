package top.enatsu.clientgui;

import top.enatsu.util.FileTool;
import top.enatsu.util.Response;
import top.enatsu.util.ResponseImpl;

import javax.swing.*;
import java.io.*;

import java.rmi.RemoteException;

public class ToolMenu extends JPopupMenu {
    public JMenuItem mkdir = new JMenuItem("新建文件夹");
    public JMenuItem rename = new JMenuItem("重命名");
    public JMenuItem cut = new JMenuItem("剪切");
    public JMenuItem copy = new JMenuItem("复制");
    public JMenuItem paste = new JMenuItem("粘贴");
    public JMenuItem delete = new JMenuItem("删除");
    static String copyFrom = "";
    static boolean isCut = false;

    ToolMenu() {
        add(mkdir);
        add(rename);
        add(cut);
        add(copy);
        add(paste);
        add(delete);

        rename.addActionListener(actionEvent -> {
            if(MainFrame.dirView.tree.getSelectionPath()!=null) { //在某目录下，该目录不允许重命名
                String name = JOptionPane.showInputDialog(null,"请输入新的文件（夹）名称：",MainFrame.dirView.getSelectedName());
                if (name != null && !name.isEmpty() && FileTool.isFileNameLegal(name)) {
                    try {
                        System.out.println("srcPath:"+MainFrame.dirView.getSelectedPath());
                        System.out.println("dstName:"+name);
                        Response response = MainFrame.fileManager.renameFile(MainFrame.dirView.getSelectedPath(), name);
                        JOptionPane.showMessageDialog(null, response.getMessage());
                        MainFrame.reDirectTo(MainFrame.curPath);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "名称不合法");
                }
            }

        });
        mkdir.addActionListener(actionEvent -> {
            String name = JOptionPane.showInputDialog("请输入要创建的文件夹名称：");
            if (name != null && !name.isEmpty() && FileTool.isFileNameLegal(name)) {
                String path = MainFrame.curPath + "/" + name;
                try {
                    Response response = MainFrame.fileManager.createFolder(path.replace("//", "/"));
                    JOptionPane.showMessageDialog(null, response.getMessage());
                    MainFrame.reDirectTo(MainFrame.curPath);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "名称不合法");
            }
        });
        cut.addActionListener(actionEvent -> {
            String Path = MainFrame.dirView.getSelectedPath();
            if (!Path.isEmpty()) {
                isCut = true;
                copyFrom = Path;
            }
        });
        copy.addActionListener(actionEvent -> {
            String Path = MainFrame.dirView.getSelectedPath();
            if (!Path.isEmpty()) {
                isCut = false;
                copyFrom = Path;
            }
        });
        paste.addActionListener(actionEvent -> {
            String src = copyFrom;
            String dst = MainFrame.dirView.getSelectedPath();
            System.out.println("src:" + src);
            System.out.println("dst:" + dst);
            try {
                File dstFile = MainFrame.fileManager.getFile(dst);
                if (!dstFile.isDirectory()) dst = MainFrame.curPath;
                dstFile = MainFrame.fileManager.getFile(dst);
                if (!dstFile.isDirectory()) {
                    JOptionPane.showMessageDialog(null, "目标文件夹不存在");
                    return;
                }
                ResponseImpl response;
                if (isCut) {
                    response = MainFrame.fileManager.moveFile(src, dst);
                } else {
                    response = MainFrame.fileManager.copyFile(src, dst);
                }
                JOptionPane.showMessageDialog(null, response.getMessage());

            } catch (RemoteException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "操作失败");
            }
            copyFrom = "";
            MainFrame.reDirectTo(MainFrame.curPath);
        });
        delete.addActionListener(actionEvent -> {
            try {
                System.out.println(MainFrame.dirView.getSelectedPath()+" to be deleted");
                ResponseImpl response = MainFrame.fileManager.deleteFile(MainFrame.dirView.getSelectedPath());
                JOptionPane.showMessageDialog(null, response.getMessage());
                MainFrame.reDirectTo(MainFrame.curPath);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}
