package top.enatsu.clientgui;

import top.enatsu.util.FileTool;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.*;
import java.io.File;
import java.rmi.RemoteException;

import static java.awt.SystemColor.menu;

public class FileListPane extends JScrollPane {
    public JTree tree;
    public ToolBarMenu menu = new ToolBarMenu();

    FileListPane(String dir) {
        ls(dir);
    }

    void ls(String dir) {
        try {
            DefaultMutableTreeNode top = new DefaultMutableTreeNode(dir);

            if (MainFrame.fileManager == null) {
                System.out.println("未定义");
                return;
            }
            File[] files = MainFrame.fileManager.getFileList(dir).getFiles();
            FileTool.printFileInfo(files);
            for (File x : files) {
                top.add(new FileItem(x));
            }
            tree = new JTree(top);

            tree.addTreeExpansionListener(new TreeExpansionListener() {
                @Override
                public void treeExpanded(TreeExpansionEvent treeExpansionEvent) {
                    MainFrame.reDirectTo(getSelectedPath());
                }

                @Override
                public void treeCollapsed(TreeExpansionEvent treeExpansionEvent) {

                }
            });
            tree.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                        boolean selected = MainFrame.dirView.tree.getSelectionPath() != null;
                        //menu.open.setEnabled(false);
                        menu.cut.setEnabled(selected);
                        menu.copy.setEnabled(selected);
                        menu.delete.setEnabled(selected);
                        menu.paste.setEnabled(!menu.copyFrom.isEmpty());
                        menu.show(tree, mouseEvent.getX(), mouseEvent.getY());
                    }
                }

                @Override
                public void mousePressed(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseReleased(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {

                }
            });
            setViewportView(tree);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    String getSelectedPath() {
        TreePath tp = MainFrame.dirView.tree.getSelectionPath();
        if (tp == null) {
            System.out.println(MainFrame.curPath);
            return MainFrame.curPath;
        };
        System.out.println("tp:"+tp.toString());
        String filename = tp.getLastPathComponent().toString();
        System.out.println("filename:"+filename);
        if (filename != null && !filename.isEmpty()) {
            String newPath = MainFrame.curPath + '/' + filename;
            return newPath.replace("//","/");
        }
        return "";
    }
}
