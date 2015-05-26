/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rtools.utilitarios;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class ListAllFolderClass {

    private static List<ListDirs> listDirs;
    private static List list;

    @PostConstruct
    public void init() {
        listDirs = new ArrayList<>();
        list = new ArrayList<>();
    }

    public static void loadFolders() {
        try {
            String path = "";
            Scanner scanner = new Scanner(new FileReader(FacesContext.getCurrentInstance().getExternalContext().getRealPath("") + "/resources/conf/path.txt"));
            while (scanner.hasNext()) {
                path += " " + scanner.next();
            }
            list.clear();
            listDirs = new ArrayList();
            recursive(new File(path.trim() + "/WEB-INF/classes/"));
            String replace = "";
            String absolutePath = "";
            String pathx = (path + "WEB-INF\\classes").trim();
            for (int i = 0; i < list.size(); i++) {
                absolutePath = list.get(i).toString().replace("\\\\", "//");
                replace = list.get(i).toString().replace("\\\\", "//");
                replace = replace.replace(pathx, "");
                replace = replace.replace("\\", ".");
                replace = replace.replace(".META-INF", "");
                if (!replace.isEmpty()) {
                    replace = replace.substring(1);
                }
                listDirs.add(new ListDirs(absolutePath, replace));
            }
        } catch (Exception e) {

        }
    }

    public static void load() {

    }

    public static List<ListDirs> getListDirs() {
        if (list == null || list.isEmpty()) {
            list = new ArrayList();
            loadFolders();
        }
        return listDirs;
    }

    public static boolean recursive(File f) {
        try {
            if (f.isDirectory()) {
                list.add(f.getAbsoluteFile());
                for (String s : f.list()) {
                    String subpath = f.getPath() + File.separator + s;
                    File subdir = new File(subpath);
                    recursive(subdir);
                }
            }
            if (f.exists()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;

    }

}
