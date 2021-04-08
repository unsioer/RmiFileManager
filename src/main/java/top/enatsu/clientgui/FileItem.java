package top.enatsu.clientgui;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

public class FileItem extends DefaultMutableTreeNode {
    public File file;
    FileItem(File newFile) {
        super(newFile.getName());
        file = newFile;
        if (file.isDirectory()) {
            add(new DefaultMutableTreeNode(""));
        }
    }
}
