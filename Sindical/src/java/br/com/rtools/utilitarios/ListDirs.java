package br.com.rtools.utilitarios;

public class ListDirs {

    private String absolutePath;
    private String classPath;

    public ListDirs(String absolutePath, String classPath) {
        this.absolutePath = absolutePath;
        this.classPath = classPath;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

}
