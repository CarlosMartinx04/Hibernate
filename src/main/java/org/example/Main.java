package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.lang.invoke.SwitchPoint;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        //1. Abrir una sesión:
        Session session = sessionFactory.openSession();
        //2. Usar la sesión:
        Transaction tx = session.beginTransaction();
        // Operaciones...tx.commit();



        //3. Cerrar la sesión:
        session.close();

    }

    public static void mostrarMenu(){

        System.out.println("Seleccione una opcion: ");
        System.out.println("1. Insertar usuario." +
                "2. Ver usuario." +
                "3. Actualizar usuario." +
                "4. Eliminar usuario" +
                "5. Salir");

        int opcion = sc.nextInt();

        switch (opcion){
            case 1:
                insertarUsuarios();
                mostrarMenu();
                break;
            case 2:
                verUsuarios();
                mostrarMenu();
                break;
            case 3:
                actualizarUsuarios();
                mostrarMenu();
                break;
            case 4:
                eliminarUsuarios();
                mostrarMenu();
                break;
            case 5:
                break;
            default:
                mostrarMenu();
        }
    }

    public static void insertarUsuarios(){
        String nombre, apellido;
        int edad;

        System.out.println("===========Insertar usuario===========");

    }

    public static void verUsuarios(){
    }

    public static void actualizarUsuarios(){

    }

    public static void eliminarUsuarios(){

    }
}