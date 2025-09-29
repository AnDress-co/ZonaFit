package gm.zona_fit;

import ch.qos.logback.core.net.server.Client;
import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicio.IClienteServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

//@SpringBootApplication
public class ZonaFitApplication implements CommandLineRunner {
    Scanner scanner = new Scanner(System.in);

    @Autowired
    private IClienteServicio clienteServicio;
    private static final Logger logger = LoggerFactory.getLogger(ZonaFitApplication.class);

	public static void main(String[] args) {
        logger.info("Iniciando la aplicacion");
		SpringApplication.run(ZonaFitApplication.class, args);
        logger.info("Aplicacion finalizada!");
	}

    @Override
    public void run(String... args) throws Exception {
        boolean salir = false;

        logger.info("**** Aplicacion Zona Fit (GYM) ****");

        while (!salir){
            int seleccion = mostrarMenu();
            salir = ejecutarOpciones(seleccion);
        }
    }

    public int mostrarMenu() {
        logger.info("""
                \n<< Menu >>
                \t1.Listar clientes
                \t2.Agregar cliente
                \t3.Modificar cliente
                \t4.Eliminar cliente
                \t5.Buscar cliente por ID
                \t0. Salir
                Selecciona una opcion:""");
        return Integer.parseInt(scanner.nextLine());
    }

    public boolean ejecutarOpciones(int seleccion) {
        switch (seleccion) {
            case 1 -> {
                listaClientes();
            }
            case 2 -> {
                agregarCliente();
            }
            case 3 -> {
                actualizarCliente();
            }
            case 4 -> {
                eliminarCliente();
            }
            case 5 -> {
                buscarClientePorId();
            }
            case  0 -> {
                logger.info("Fin del programa!");
                return true;
            }
        }
        return false;
    }

    public void listaClientes() {
        logger.info("\n<< Lista de clientes >>");
        for (Cliente cliente : clienteServicio.listarCliente()){
            logger.info(cliente.toString());
        }
    }

    public void agregarCliente() {
        logger.info("\n<< Guardar un cliente >>");
        logger.info("Nombre del nuevo cliente:");
        String nombre = scanner.nextLine();
        logger.info("Apellido del nuevo cliente:");
        String apellido = scanner.nextLine();
        logger.info("Cual es la nueva membresia:");
        Integer membresia = Integer.parseInt(scanner.nextLine());
        Cliente nuevoCliente = new Cliente(null, nombre, apellido, membresia);
        clienteServicio.guardarCliente(nuevoCliente);
        logger.info("Cliente guardado!");
    }

    public void actualizarCliente() {
        logger.info("\n<< Actualizar un cliente >>");
        logger.info("Cual es el ID:");
        Integer idCliente = Integer.parseInt(scanner.nextLine());
        Cliente cliente = clienteServicio.buscarClientePorId(idCliente);
        if(cliente != null) {
            logger.info("Nombre del cliente:");
            String nombre = scanner.nextLine();
            logger.info("Apellido del cliente:");
            String apellido = scanner.nextLine();
            logger.info("Cual es la membresia:");
            Integer membresia = Integer.parseInt(scanner.nextLine());
            Cliente nuevoCliente = new Cliente(idCliente, nombre, apellido, membresia);
            clienteServicio.guardarCliente(nuevoCliente);
            logger.info("Cliente actualizado!");
        } else {
            logger.info("Cliente no encontrado!");
        }
    }

    public void eliminarCliente() {
        logger.info("\n<< Eliminar un cliente >>");
        logger.info("Ingresa el ID del cliente:");
        Integer id = Integer.parseInt(scanner.nextLine());

        if(clienteServicio.buscarClientePorId(id) != null) {
            clienteServicio.eliminarCliente(id);
            logger.info("Cliente eliminado!");
        } else {
            logger.info("Cliente no se encontro!");
        }
    }

    public void buscarClientePorId() {
        logger.info("\n<< Buscar un cliente por ID >>");
        logger.info("Ingresa el Id del cliente:");
        Integer id = Integer.parseInt(scanner.nextLine());
        Cliente cliente = clienteServicio.buscarClientePorId(id);
        if (cliente != null) {
            logger.info(cliente.toString());
        } else {
            logger.info("El cliente no fue encontrado!");
        }
    }
}
