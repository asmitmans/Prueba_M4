package cl.fullstackjava.vista;

import cl.fullstackjava.modelo.CategoriaEnum;
import cl.fullstackjava.modelo.Cliente;
import cl.fullstackjava.servicio.*;
import cl.fullstackjava.utilidades.Utilidad;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private ClienteServicio clienteServicio;
    private ArchivoServicio archivoServicio;
    private ExportadorCsv exportadorCsv;
    private ExportadorTxt exportadorTxt;
    private String fileName;
    private String fileName1;
    private Scanner scanner;

    public Menu() {
        clienteServicio = new ClienteServicio();
        archivoServicio = new ArchivoServicio();
        exportadorCsv = new ExportadorCsv();
        exportadorTxt = new ExportadorTxt();
        fileName = "Clientes";
        fileName1 = "DBClientes.csv";
        scanner = new Scanner(System.in);
    }

    public void iniciarMenu() {

        int command = 0;
        do {
            mostrarMenu();
            command = leerCommandInt();
            limpiarPantalla();

            if (command == 1) {
                listarCliente();
            } else if (command == 2) {
                agregarCliente();
            } else if (command == 3) {
                editarCliente();
            } else if (command == 4) {
                importarDatos();
            } else if (command == 5) {
                exportarDatos();
            } else if (command == 6) {
                terminarPrograma();
            } else {
                mostrarMensaje("opcion invalida");
            }

        } while (command != 6);

    }

    public void mostrarMensaje(String mensaje) {
        Utilidad.mostrarMesaje(mensaje);
    }

    public void limpiarPantalla() {
        Utilidad.limpiarPantalla();
    }

    public void mostrarMenu() {
        String menu =
                "1. Listar Clientes\n" +
                        "2. Agregar Cliente\n" +
                        "3. Editar Cliente\n" +
                        "4. Cargar Datos\n" +
                        "5. Exportar Datos\n" +
                        "6. Salir\n" +
                        "Ingrese una opción: ";
        mostrarMensaje(menu);
    }

    public int leerCommandInt() {
        try {
            return Integer.valueOf(scanner.nextLine());
        } catch (NumberFormatException e) {
            mostrarMensaje("No se ingresó un numero valido");
        }
        return 0;
    }

    public void listarCliente() {
        // Requerimiento: Se deben utilizar iteraciones de la librería Streams.
        clienteServicio.listarClientes().stream()
                .map(cliente -> cliente.toString())
                .forEach(mensaje -> mostrarMensaje(mensaje));
        mostrarMensaje("\n");
    }

    public void agregarCliente() {

        mostrarMensaje("-------------Crear Cliente-------------\n");

        mostrarMensaje("Ingresa RUN del Cliente:");
        String runCliente = scanner.nextLine();
        mostrarMensaje("Ingresa Nombre del Cliente:");
        String nombreCliente = scanner.nextLine();
        mostrarMensaje("Ingresa Apellido del Cliente:");
        String apellidoCliente = scanner.nextLine();
        mostrarMensaje("Ingresa años como Cliente:");
        String aniosCliente = scanner.nextLine();
        if (aniosCliente.split(" ").length > 1) {
            aniosCliente = aniosCliente.split(" ")[0];
        }
        Cliente cliente = new Cliente(runCliente,nombreCliente,apellidoCliente,aniosCliente);

        try {
            clienteServicio.agregarCliente(cliente);
            mostrarMensaje("Cliente agregado exitosamente");
        } catch (IllegalArgumentException e) {
            mostrarMensaje("Error al agregar cliente: " + e.getMessage());
        }

        mostrarMensaje("---------------------------------------");
    }

    public void editarCliente() {

        int command = 0;
        int op = 0;

        menuEditarCliente();
        command = leerCommandInt();
        if (command == 1) {
            Cliente cliente = obtenerClientePorRun();
            if (cliente!=null) {
                menuCambiarEstado(cliente);
                op = leerCommandInt();
                cambiarCategoriaCliente(cliente,op);
            }
        } else if (command == 2) {
            Cliente cliente = obtenerClientePorRun();
            if(cliente!=null) {
                menuModificarDatos(cliente);
                op = leerCommandInt();
                modificarDatosCliente(cliente, op);
            }
        } else {
            mostrarMensaje("Opción ingresada inválida para editar cliente.");
        }
    }

    public Cliente obtenerClientePorRun() {

        mostrarMensaje("Ingrese RUN del Cliente a editar:");
        String runCliente = scanner.nextLine();
        Cliente cliente = clienteServicio.obtenerClientePorRun(runCliente);
        if (cliente == null) {
            mostrarMensaje("RUN ingresado no encontrado");
            return null;
        }

        return cliente;
    }

    public void menuEditarCliente() {
        mostrarMensaje("-------------Editar Cliente-------------");
        mostrarMensaje("Seleccione qué desea hacer:");
        mostrarMensaje("1.-Cambiar el estado del Cliente");
        mostrarMensaje("2.-Editar los datos ingresados del Cliente\n");
        mostrarMensaje("Ingrese opcion:");
    }

    public void menuCambiarEstado(Cliente cliente) {
        mostrarMensaje("-----Actualizando estado del Cliente----");
        mostrarMensaje("El estado actual es: " + cliente.getNombreCategoria());
        if (cliente.getNombreCategoria().equals(CategoriaEnum.Activo)) {
            mostrarMensaje("1.-Si desea cambiar el estado del Cliente a Inactivo");
        } else {
            mostrarMensaje("1.-Si desea cambiar el estado del Cliente a Activo");
        }
        mostrarMensaje("2.-Si desea mantener el estado del cliente " + cliente.getNombreCategoria());
        mostrarMensaje("\n");
        mostrarMensaje("Ingrese opcion: ");
    }

    public void cambiarCategoriaCliente(Cliente cliente, int op) {
        if (op == 1) {
            if (cliente.getNombreCategoria().equals(CategoriaEnum.Activo)) {
                clienteServicio.cambiarCategoriaCliente(cliente, CategoriaEnum.Inactivo);
            } else {
                clienteServicio.cambiarCategoriaCliente(cliente, CategoriaEnum.Activo);
            }
        } else if (op!=2) {
            mostrarMensaje("Opción ingresada inválida para cambiar categoria.");
        }
    }

    public void menuModificarDatos(Cliente cliente) {
        mostrarMensaje("----Actualizando datos del Cliente-----");
        mostrarMensaje("1.-El RUN del Cliente es: " + cliente.getRunCliente());
        mostrarMensaje("2.-El Nombre del Cliente es: " + cliente.getNombreCliente());
        mostrarMensaje("3.-El Apellido del Cliente es: " + cliente.getApellidoCliente());
        mostrarMensaje("4.-Los años como Cliente son: " + cliente.getAniosCliente() + " años");
        mostrarMensaje("\n");
        mostrarMensaje("Ingrese opcion a editar de los datos del cliente: ");
    }

    public void modificarDatosCliente(Cliente cliente, int op) {
        if (op == 1) {
            mostrarMensaje("Ingrese nuevo RUN del Cliente: ");
            String nuevoRun = scanner.nextLine();
            clienteServicio.actualizarRunCliente(cliente, nuevoRun);
        } else if (op == 2) {
            mostrarMensaje("Ingrese nuevo Nombre del Cliente: ");
            String nuevoNombre = scanner.nextLine();
            clienteServicio.actualizarNombreCliente(cliente, nuevoNombre);
        } else if (op == 3) {
            mostrarMensaje("Ingrese nuevo Apellido del Cliente: ");
            String nuevoApellido = scanner.nextLine();
            clienteServicio.actualizarApellidoCliente(cliente, nuevoApellido);
        } else if (op == 4) {
            mostrarMensaje("Ingrese años como Cliente: ");
            String nuevoAnios = scanner.nextLine();
            clienteServicio.actualizarAniosCliente(cliente, nuevoAnios);
        } else {
            mostrarMensaje("Opción ingresada inválida para modificar datos del cliente.");
        }
    }

    public void importarDatos() {
        mostrarMensaje("----------------Cargar Datos-------------------");
        mostrarMensaje("Ingresa la ruta en donde se encuentra el archivo DBClientes.csv:");
        String pathArchivo = scanner.nextLine();
        pathArchivo += File.separator + fileName1;
        mostrarMensaje("-----------------------------------------------");

        List<Cliente> clientesImportados = archivoServicio.cargarDatos(pathArchivo);
        if (clientesImportados != null && !clientesImportados.isEmpty()) {
            clienteServicio.setListaClientes(clientesImportados);
            mostrarMensaje("Datos cargados correctamente en la lista");
        } else {
            mostrarMensaje("No se encontraron datos para importar o hubo un error al cargar los datos.");
        }
    }

    public void exportarDatos() {
        String mensaje = "---------Exportar Datos-----------\n" +
                         "Seleccione el formato a exportar: \n" +
                         "1.-Formato csv \n" +
                         "2.-Formato txt \n \n" +
                         "Ingrese una opción para exportar:\n";
        mostrarMensaje(mensaje);
        int op = leerCommandInt();
        mensaje = "-----------------------------------------------\n" +
                  "---------Exportar Datos-----------\n";
        mostrarMensaje(mensaje);
        if (op==1) {
            mostrarMensaje("Ingresa la ruta en donde desea exportar el archivo clientes.csv:");
            String pathFile = scanner.nextLine();
            pathFile += File.separator + fileName.toLowerCase() + ".csv";
            archivoServicio.setExportador(exportadorCsv);
            archivoServicio.exportar(pathFile,clienteServicio.listarClientes());
        } else if (op==2) {
            mostrarMensaje("Ingresa la ruta en donde desea exportar el archivo clientes.txt:");
            String pathFile = scanner.nextLine();
            pathFile += File.separator + fileName.toLowerCase() + ".txt";
            archivoServicio.setExportador(exportadorTxt);
            archivoServicio.exportar(pathFile,clienteServicio.listarClientes());
        }
    }

    public void terminarPrograma() {
        mostrarMensaje("Cerrando...");
    }

}

