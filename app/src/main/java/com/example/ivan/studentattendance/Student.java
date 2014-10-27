package com.example.ivan.studentattendance;

/**
 * Created by ivan on 22/10/2014.
 */
public class Student {

    public String nome;
    public int falta;
    public String id;

    public Student(String nome, int falta, String id) {
        this.nome = nome;
        this.falta = falta;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getFalta() {
        return falta;
    }

    public void setFalta(int falta) {
        this.falta = falta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
