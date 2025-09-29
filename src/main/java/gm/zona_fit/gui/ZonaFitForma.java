package gm.zona_fit.gui;

import gm.zona_fit.modelo.Cliente;
import gm.zona_fit.servicio.IClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

@Component
public class ZonaFitForma extends JFrame{
    private JPanel panelPrincipal;
    private JTable clientesTabla;
    private JTextField nombreTexto;
    private JTextField apellidoTexto;
    private JTextField membresiaTexto;
    private JButton guardarButton;
    private JButton eliminarButton;
    private JButton limpiarButton;
    IClienteServicio clienteServicio;
    private DefaultTableModel tablaModeloClientes;
    private Integer idCliente;

    @Autowired
    public ZonaFitForma(IClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;
        iniciarForma();
        guardarButton.addActionListener(e -> guardarCliente());
        clientesTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarClienteSeleccionado();
            }
        });
        eliminarButton.addActionListener(e -> eliminarCliente());
        limpiarButton.addActionListener(e -> limpiarFormulario());
    }

    private void iniciarForma() {
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,700);
        setLocationRelativeTo(null);
    }

    private void createUIComponents() {
        //this.tablaModeloClientes = new DefaultTableModel(0, 4);
        //Evitamos la edicion de los valores de la celda de la tabla.
        this.tablaModeloClientes = new DefaultTableModel(0, 4){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        String[] columnas = {"Id", "Nombre", "Apellido", "Membresia"};
        this.tablaModeloClientes.setColumnIdentifiers(columnas);
        this.clientesTabla = new JTable(tablaModeloClientes);
        //Restingimos la seleccion de la tabla a un solo registro
        this.clientesTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //Crear listado de clientes
        listarClientes();
    }

    private void listarClientes() {
        this.tablaModeloClientes.setRowCount(0);
        List<Cliente> clientes = this.clienteServicio.listarCliente();
        clientes.forEach(cliente -> {
            Object[] renglonCliente = {
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getMembresia()
            };
            this.tablaModeloClientes.addRow(renglonCliente);
        });
    }

    private void guardarCliente() {
        if (nombreTexto.getText().isEmpty()) {
            mostrarMensaje("Proporciona un nombre");
            nombreTexto.requestFocusInWindow();
            return;
        }
        if (membresiaTexto.getText().isEmpty()) {
            mostrarMensaje("Proporciona una membresia");
            nombreTexto.requestFocusInWindow();
            return;
        }
        String nombre = nombreTexto.getText();
        String apellido = apellidoTexto.getText();
        Integer membresia = Integer.parseInt(membresiaTexto.getText());
        this.clienteServicio.guardarCliente(new Cliente(this.idCliente ,nombre, apellido, membresia)); //Insertar o Modificar
        if (this.idCliente == null) {
            mostrarMensaje("Se agrego un nuevo cliente!");
        } else {
            mostrarMensaje("Se actualizo el cliente!");
        }
        limpiarFormulario();
        listarClientes();
    }

    private void eliminarCliente() {
        int renglon = clientesTabla.getSelectedRow();
        if (renglon != -1) {
            if(this.idCliente == null) {
                mostrarMensaje("Debe de seleccionar un cliente de la tabla!");
            } else {
                this.clienteServicio.eliminarCliente(this.idCliente);
                mostrarMensaje("Cliente con id:" + this.idCliente + " eliminado!");
                limpiarFormulario();
                listarClientes();
            }
        } else {
            mostrarMensaje("Debe de seleccionar un cliente a eliminar!");
        }
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    private void limpiarFormulario() {
        nombreTexto.setText("");
        apellidoTexto.setText("");
        membresiaTexto.setText("");
        //Limpiar el Id cliente seleccionado y la seleccion
        this.idCliente = null;
        this.clientesTabla.getSelectionModel().clearSelection();
    }

    private void cargarClienteSeleccionado() {
        int renglon = clientesTabla.getSelectedRow();
        if (renglon != -1) { //-1 Significa que no a seleccionado nada
            String id = clientesTabla.getModel().getValueAt(renglon, 0).toString();
            this.idCliente = Integer.parseInt(id);
            String nombre = clientesTabla.getModel().getValueAt(renglon, 1).toString();
            this.nombreTexto.setText(nombre);
            String apellido = clientesTabla.getModel().getValueAt(renglon, 2).toString();
            this.apellidoTexto.setText(apellido);
            String membresia = clientesTabla.getModel().getValueAt(renglon, 3).toString();
            this.membresiaTexto.setText(membresia);
        }
    }
}
