package org.example;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.List;
import java.util.Scanner;


public class Main {

    public static SessionFactory sessionFactory = buildSessionFactory();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion = 0;
        while(opcion!=5){

            System.out.println("===========Menu===========");

            System.out.println("Seleccione una opcion: ");
            System.out.println("""
                    1. Insertar usuario.
                    2. Ver usuario.
                    3. Actualizar usuario.
                    4. Eliminar usuario.
                    5. Salir.""");
            System.out.print("Opcion: ");
            opcion = sc.nextInt();

            switch (opcion){
                case 1 -> insertarUsuarios();
                case 2 -> verUsuarios();
                case 3 -> actualizarUsuarios();
                case 4 -> eliminarUsuarios();
                case 5 -> System.out.println("Saliendo");
                default -> System.out.println("Selecciona una opcion correcta.");
            }
        }
    }

    public static void insertarUsuarios(){

        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
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
            session.merge(usuario);
            session.getTransaction().commit();
        } catch (HibernateException e){
            System.err.println("ERROR al agregar un usuario.");
        }


    }

    public static void verUsuarios(){

        System.out.println("===========Lista de Usuarios===========");
        List<Usuario> listaUsuarios = obtenerLista();
        for(Usuario usuario : listaUsuarios){
            System.out.println("ID: "+usuario.getId()+", Nombre: "+usuario.getNombre()+", Email: "+usuario.getEmail()+", Edad: "+usuario.getEdad());
        }

    }

    public static void actualizarUsuarios(){

        System.out.println("===========Actualizar usuario===========");
        System.out.println("Selecciona el ID de un usuario para modificarlo.");
        int id = sc.nextInt();
        try(Session session = sessionFactory.openSession()){

            session.beginTransaction();
            Usuario usuario = session.get(Usuario.class, id);

            if(usuario==null){
                System.out.println("Usuario NO encontrado.");
                session.getTransaction().rollback();
                return;
            }

            int opcion = -1;

            while(opcion!=4){
                System.out.println("Actualizar nombre(1), email(2), edad(3) o salir(4)");
                opcion = sc.nextInt();

                switch (opcion){

                    case 1 ->{
                        System.out.print("Elige el nuevo nombre del usuario: ");
                        usuario.setNombre(sc.next());
                        mostrarUsuario(usuario);
                    }
                    case 2 ->{
                        System.out.print("Elige el nuevo correo del usuario: ");
                        usuario.setEmail(sc.next());
                        mostrarUsuario(usuario);
                    }
                    case 3 ->{
                        System.out.print("Elige el nuevo nombre del usuario: ");
                        usuario.setEdad(sc.nextInt());
                        mostrarUsuario(usuario);
                    }
                    case 4 -> System.out.println("Cerrando el menu de actualizacion.");
                    default -> System.out.println("Selecciona una opcion correcta.");
                }

            }
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    public static void eliminarUsuarios() {
        System.out.println("===========Eliminar usuario===========");
        System.out.println("Selecciona el ID del usuario que quieras eliminar.");

        try(Session session = sessionFactory.openSession()){

            session.beginTransaction();
            Usuario usuario = session.get(Usuario.class, sc.nextInt());

            if(usuario == null){
                System.out.println("NO se encontró al usuario.");
                session.getTransaction().rollback();
                return;
            }

            System.out.println("Estas seguro que seas eliminar al usuario: ");
            mostrarUsuario(usuario);

            System.out.println("Si(1), No(0)");
            int opcion = sc.nextInt();

            if (opcion == 1) {
                session.remove(usuario);
                System.out.println("Usuario borrado correctamente.");
            } else {
                System.out.println("No se ha eliminado ningun usuario.");
            }

            session.getTransaction().commit();

        } catch (HibernateException e) {
            System.err.println("Error al eliminar al usuario.");
            throw new RuntimeException(e);
        }

    }

    public static void mostrarUsuario(Usuario usuario){
        try{
            List<Usuario> listaUsuarios = obtenerLista();
            for(Usuario user : listaUsuarios){
                if(user.getId()==usuario.getId()){
                    System.out.println("ID: "+usuario.getId()+", Nombre: "+usuario.getNombre()+", Email: "+usuario.getEmail()+", Edad: "+usuario.getEdad());
                }
            }
        } catch (HibernateException e) {
            System.err.println("Error al mostrar el usuario.");
            throw new HibernateException(e);
        }

    }

    public static List<Usuario> obtenerLista(){
        try(Session session = getSessionFactory().getCurrentSession()){
            session.beginTransaction();
            List<Usuario> lista = session.createQuery("FROM Usuario", Usuario.class).list();
            session.getTransaction().commit();
            return lista;
        } catch (HibernateException e) {
            System.err.println("Erro al obtener la lista de usuarios");
            throw new HibernateException(e);
        }

    }

    public static SessionFactory buildSessionFactory(){
        try{
            return new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Usuario.class).buildSessionFactory();
        } catch (HibernateException e) {
            System.err.println("Error al conectar con hibernate");
            throw new RuntimeException(e);
        }
    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }

}

