package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.SwitchPoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Usuario.class).buildSessionFactory();

        //1. Abrir una sesión:
        Session session = sessionFactory.openSession();

        mostrarMenu(session);

        //3. Cerrar la sesión:
        session.close();

    }

    public static void mostrarMenu(Session session){
        int opcion = 0;
        while(opcion!=5){

            System.out.println("Seleccione una opcion: ");
            System.out.println("1. Insertar usuario.\n" +
                    "2. Ver usuario.\n" +
                    "3. Actualizar usuario.\n" +
                    "4. Eliminar usuario.\n" +
                    "5. Salir.");
            System.out.print("Opcion: ");
            opcion = sc.nextInt();

            switch (opcion){
                case 1:
                    insertarUsuarios(session);
                    break;
                case 2:
                    verUsuarios(session);
                    break;
                case 3:
                    actualizarUsuarios(session);
                    break;
                case 4:
                    eliminarUsuarios(session);
                    break;
                case 5:
                    System.out.println("Saliendo");
                    break;
            }

        }

    }

    public static void insertarUsuarios(Session session){
        //2. Usar la sesión:
        Transaction tx = session.beginTransaction();
        // Operaciones...tx.commit();

        System.out.println("===========Insertar usuario===========");
        System.out.println("Escribe el nombre del usuario.");
        String nombre = sc.next();
        System.out.println("Escribe el email.");
        String email = sc.next();
        System.out.println("Elige la edad del usuario.");
        int edad = sc.nextInt();

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setEdad(edad);
        session.save(usuario);
        tx.commit();

    }

    public static void verUsuarios(Session session){

        System.out.println("===========Lista de Usuarios===========");
        List<Usuario> listaUsuarios = session.createQuery("FROM Usuario", Usuario.class).list();
        for(Usuario usuario : listaUsuarios){
            System.out.println("ID: "+usuario.getId()+", Nombre: "+usuario.getNombre()+", Email: "+usuario.getEmail()+", Edad: "+usuario.getEdad());
        }

    }

    public static void actualizarUsuarios(Session session){
        System.out.println("===========Insertar usuario===========");
        System.out.println("Selecciona el ID de un usuario para modificarlo.");
        int id = sc.nextInt();
        Usuario usuario = session.get(Usuario.class, id);
        menuUpdate(usuario, session);
    }

    public static void menuUpdate(Usuario usuario, Session session){
        Transaction tx;
        System.out.println("Actualizar nombre(1), email(2), edad(3) o salir(0)");
        int opcion = sc.nextInt();
        switch (opcion){
            case 1 ->{
                tx = session.beginTransaction();
                System.out.print("Elige el nombre nuevo del usuario: ");
                String nombeActualizar = sc.next();
                usuario.setNombre(nombeActualizar);
                session.update(usuario);
                tx.commit();
                mostrarUsuario(usuario, session);
                menuUpdate(usuario, session);
            }
            case 2 ->{
                tx = session.beginTransaction();
                System.out.print("Elige el email nuevo del usuario: ");
                String emailActualizar = sc.next();
                usuario.setEmail(emailActualizar);
                session.update(usuario);
                tx.commit();
                mostrarUsuario(usuario, session);
                menuUpdate(usuario, session);
            }
            case 3 ->{
                tx = session.beginTransaction();
                System.out.print("Elige la edad nuevo del usuario: ");
                int edad = sc.nextInt();
                usuario.setEdad(edad);
                session.update(usuario);
                tx.commit();
                mostrarUsuario(usuario, session);
                menuUpdate(usuario, session);
            }
            case 0->{
                mostrarMenu(session);
            }
            default -> {
                menuUpdate(usuario, session);
            }

        }
    }

    public static void eliminarUsuarios(Session session){

        Transaction tx = session.beginTransaction();

        System.out.println("===========Eliminar usuario===========");
        System.out.println("Selecciona el ID del usuario que quieras eliminar.");
        int idEliminar = sc.nextInt();
        Usuario usuario = session.get(Usuario.class, idEliminar);

        System.out.println("Estas seguro que seas eliminar al usuario: ");
        mostrarUsuario(usuario, session);
        System.out.println("Si(1), No(0)");
        int opcion = sc.nextInt();

        if (opcion == 1) {
            session.delete(usuario);
            tx.commit();
        } else {
            System.out.println("No se ha eliminado ningun usuario.");
            tx.rollback();
        }

        mostrarMenu(session);

    }

    public static void mostrarUsuario(Usuario usuario, Session session){
        List<Usuario> listaUsuarios = obtenerLista(session);
        for(Usuario user : listaUsuarios){
            if(user.getId()==usuario.getId()){
                System.out.println("ID: "+usuario.getId()+", Nombre: "+usuario.getNombre()+", Email: "+usuario.getEmail()+", Edad: "+usuario.getEdad());
            }
        }
    }

    public static List obtenerLista(Session session){
        return session.createQuery("FROM Usuario", Usuario.class).list();
    }

}

