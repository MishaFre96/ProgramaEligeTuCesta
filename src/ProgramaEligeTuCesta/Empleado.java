package ProgramaEligeTuCesta;

import java.io.Serializable;

public class Empleado implements Serializable {
    private static final long serialVersionUID = 1L;

    private String idEmpleado;
    private String nombreEmpleado;
    private String apellido1Empleado;
    private String apellido2Empleado;
    private int edadEmpleado;          
    private String sexoEmpleado;
    private String telefonoEmpleado;
    private double salarioEmpleado;     
    private String dniEmpleado;

    // Getters y setters 
    public String getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(String idEmpleado) { this.idEmpleado = idEmpleado; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public String getApellido1Empleado() { return apellido1Empleado; }
    public void setApellido1Empleado(String apellido1Empleado) { this.apellido1Empleado = apellido1Empleado; }

    public String getApellido2Empleado() { return apellido2Empleado; }
    public void setApellido2Empleado(String apellido2Empleado) { this.apellido2Empleado = apellido2Empleado; }

    public int getEdadEmpleado() { return edadEmpleado; }
    public void setEdadEmpleado(int edadEmpleado) { this.edadEmpleado = edadEmpleado; }

    public String getSexoEmpleado() { return sexoEmpleado; }
    public void setSexoEmpleado(String sexoEmpleado) { this.sexoEmpleado = sexoEmpleado; }

    public String getTelefonoEmpleado() { return telefonoEmpleado; }
    public void setTelefonoEmpleado(String telefonoEmpleado) { this.telefonoEmpleado = telefonoEmpleado; }

    public double getSalarioEmpleado() { return salarioEmpleado; }
    public void setSalarioEmpleado(double salarioEmpleado) { this.salarioEmpleado = salarioEmpleado; }

    public String getDniEmpleado() { return dniEmpleado; }
    public void setDniEmpleado(String dniEmpleado) { this.dniEmpleado = dniEmpleado; }

    // Constructor
    public Empleado(String id, String nombre, String apellido1, String apellido2,
                    int edad, String sexo, String telefono, double salario, String dni) {
        this.idEmpleado = id;
        this.nombreEmpleado = nombre;
        this.apellido1Empleado = apellido1;
        this.apellido2Empleado = apellido2;
        // Validacion simple
        if (edad <= 0 || edad > 120) {
            throw new IllegalArgumentException("Edad fuera de rango");
        }
        this.edadEmpleado = edad;
        this.sexoEmpleado = sexo;
        this.telefonoEmpleado = telefono;
        if (salario < 0) {
            throw new IllegalArgumentException("Salario negativo");
        }
        this.salarioEmpleado = salario;
        this.dniEmpleado = dni;
    }
}