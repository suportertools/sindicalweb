package br.com.rtools.utilitarios;

import java.io.File;
import java.io.Serializable;

public class MemoryFile implements Serializable {

    private String name;
    private File file;
    private Integer index;

    public MemoryFile() {
        this.name = "";
        this.file = null;
        this.index = null;
    }

    public MemoryFile(String name, File file, Integer index) {
        this.name = name;
        this.file = file;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
