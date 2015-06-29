/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rtools.utilitarios;

import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.seguranca.utilitarios.SegurancaUtilitariosBean;
import br.com.rtools.sistema.ConfiguracaoUpload;
import static br.com.rtools.utilitarios.Diretorio.arquivo;
import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.ServiceLoader;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import org.apache.commons.io.FileUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@ManagedBean(name = "javaClassLoaderBean")
@SessionScoped
public class JavaClassLoader implements Serializable {

    private TreeNode root;
    private TreeNode selectedRoot;
    private List<ListDirs> list;
    private Integer index;
    private String selectedFolderClass;
    private File file;

    @PostConstruct
    public void init() {
        selectedFolderClass = "";
        root = null;
        list = new ArrayList<>();
        index = 0;
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("javaClassLoaderBean");
    }

    public void load(ConfiguracaoUpload cu) {
        try {

            String source = "package test; public class Test { static { System.out.println(\"hello\"); } public Test() { System.out.println(\"world\"); } }";
            String cliente = "";
            if (GenericaSessao.exists("sessaoCliente")) {
                cliente = GenericaSessao.getString("sessaoCliente");
                if (cliente.equals("")) {
                    return;
                }
            }
            String conteudo = "";
            try {
                file = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + cliente + "/" + cu.getDiretorio() + cu.getArquivo()));
                if (!file.exists()) {
                    return;
                }
                conteudo = FileUtils.readFileToString(file);
                conteudo = conteudo.toString();
            } catch (Exception e) {
            }
            String className = cu.getArquivo().replace(".java", "");
            String classFile = cu.getArquivo().replace(".java", ".class");
            if (GenericaSessao.exists(className + "Bean")) {
                GenericaSessao.getObject(className + "Bean");
            }
            String binPath = "";
            for (int i = 0; i < list.size(); i++) {
                if (selectedFolderClass.equals(list.get(i).getClassPath())) {
                    binPath = list.get(i).getAbsolutePath();
                    break;
                }
            }

// Prepare source somehow.
            String sourcez = "package test; public class Test { static { System.out.println(\"hello\"); } public Test() { System.out.println(\"world\"); } }";

// Save source in .java file.
            // ARQUIVO A SER MOVIDO
            // DESTINO
            File file_move = new File(binPath);
            File file_delete = new File(file_move + "/" + cu.getArquivo());
            if (file_delete.exists()) {
                file_delete.delete();
            }
            File file_compiler = new File(file_move + "/" + cu.getArquivo());
            boolean ok = file.renameTo(new File(file_move, file.getName()));
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            StandardJavaFileManager sjfm = compiler.getStandardFileManager(null, null, null);
            Iterable<? extends JavaFileObject> jfos = sjfm.getJavaFileObjectsFromFiles(Arrays.asList(new File[]{file_compiler}));
            FileOutputStream errorStream = new FileOutputStream(file_compiler);
            int compilationResult = compiler.run(null, null, errorStream, file_compiler.getPath());
            if (compilationResult == 0) {
                System.out.println("Compilation is successful");
            } else {
                System.out.println("Compilation Failed");
            }
            boolean success = compiler.getTask(null, sjfm, null, null, null, jfos).call();
            sjfm.close(); //            compiler.run(null, null, null, file_compiler.getPath());
            file.delete();

            // Load and instantiate compiled class.
            URLClassLoader classLoaderz = URLClassLoader.newInstance(new URL[]{file_move.toURI().toURL()});
            URL[] urls = null;
            URL url = file_move.toURI().toURL();
            urls = new URL[]{url};
            URLClassLoader ucl = URLClassLoader.newInstance(urls);
//
            Class c = ucl.loadClass("br.com.rtools.teste.Testes");
            Class<?> clsz = Class.forName("br.com.rtools.teste.Testes", true, classLoaderz); // Should print "hello".
            Object instancez = clsz.newInstance(); // Should print "world".
            System.out.println(instancez); // Should print "test.Test@hashcode";

// Save source in .java file.
//            File rootx = new File("C:\\java"); // On Windows running on C:\, this is C:\java.
//            File sourceFile = new File(rootx, "teste/" + cu.getArquivo());
//            sourceFile.getParentFile().mkdirs();
//            new FileWriter(sourceFile).append(sourcez).close();
//
//// Compile source file.
//            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//            compiler.run(null, null, null, sourceFile.getPath());
//
//// Load and instantiate compiled class.
//            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{rootx.toURI().toURL()});
//            Class<?> clsx = Class.forName("br.com.rtools.academia.AcademiaGrade"); // Should print "hello".
//            Object instancex = clsx.newInstance(); // Should print "world".
//            Class<?> cls = Class.forName("teste.AcademiaGrade", false, classLoader); // Should print "hello
//            Object instance = cls.newInstance(); // Should print "world".
            //System.out.println(instance); // Should print "test.Test@hashcode".                
            //sourceFile.delete();
        } catch (Exception e) {
            e.getMessage();
        }

//        try {
//            String binPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/bin/");
//            byte[] buffer = new byte[(int) file.length()];
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(buffer);
//            fos.flush();
//            fos.close();
//            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
////            if (new File(binPath + "/" + classFile).exists()) {
////                new File(binPath + "/" + classFile).delete();
////            }
//
//            compiler.run(null, null, null, file.getPath());
//// Load and instantiate compiled class.
//            URL[] classLoaderUrls = new URL[]{new URL("/Cliente/" + cliente + "/" + cu.getDiretorio() + cu.getArquivo())};
//            URLClassLoader classLoader = null;
//            URLClassLoader urlClassLoader = new URLClassLoader(classLoaderUrls);
//            Class<?> cls = Class.forName(selectedFolderClass + "." + className, true, classLoader); // Should print "hello".
//            Object instance = cls.newInstance(); // Should print "world".
//            System.out.println(instance);
//
////            StandardJavaFileManager standardJavaFileManager = compiler.getStandardFileManager(null, null, null);
////            standardJavaFileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(new File(binPath)));
////            CompilationTask compilationTask = compiler.getTask(null, standardJavaFileManager, null, null, null, standardJavaFileManager.getJavaFileObjectsFromFiles(Arrays.asList(file)));
////            compilationTask.call();
////
////            Class clazz = Class.forName(selectedFolderClass + "." + className);
////
////            Class compilerx = ServiceLoader.load(clazz.getClass()).getClass();
////
////            Object o = clazz.getClasses();
//
////            System.out.println(clazz);
//        } catch (IOException | ClassNotFoundException e) {
//            e.getMessage();
//        }
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void selected() {
        if (selectedRoot != null) {
            selectedFolderClass = selectedRoot.getData().toString();
        }
    }

    public TreeNode getRoot() {
        if (root == null) {
            root = new DefaultTreeNode("root", null);
            list = ListAllFolderClass.getListDirs();
            for (int i = 0; i < list.size(); i++) {
                TreeNode treeNode = new DefaultTreeNode(list.get(i).getClassPath(), root);
            }
        }
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedRoot() {
        return selectedRoot;
    }

    public void setSelectedRoot(TreeNode selectedRoot) {
        this.selectedRoot = selectedRoot;
    }

    public void upload(FileUploadEvent event) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
        String cliente = "";
        if (GenericaSessao.exists("sessaoCliente")) {
            cliente = GenericaSessao.getString("sessaoCliente");
            if (cliente.equals("")) {
                return;
            }
        }
        ConfiguracaoUpload configuracaoUpload = new ConfiguracaoUpload();
        configuracaoUpload.setArquivo(event.getFile().getFileName());
        configuracaoUpload.setDiretorio("Arquivos/compiler/");
        configuracaoUpload.setEvent(event);
        configuracaoUpload.setSubstituir(true);
        if (Upload.enviar(configuracaoUpload, true)) {
            load(configuracaoUpload);
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
