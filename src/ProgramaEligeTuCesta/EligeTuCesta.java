package ProgramaEligeTuCesta;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

public class EligeTuCesta {  

    public static void main(String args[]) {
        String ficheroUsuario = seleccionFichero();
        crearDirectorios();
        int objetosEscritos = crearPuntoDat(ficheroUsuario);
        crearXML(objetosEscritos);
    }

    public static String seleccionFichero() {
        Scanner sc = new Scanner(System.in);
        String ficheroUsuario;
        boolean salirMenu = false;
        do {
            System.out.print("Indique el nombre del fichero que desea manipular (incluida la extensión del mismo): ");
            ficheroUsuario = sc.nextLine();
            if (ficheroUsuario.trim().isEmpty()) {
                System.out.println("Nombre de documento no válido.");
                ficheroUsuario = null;
            } else {
                salirMenu = true;
            }
        } while (salirMenu == false);
        sc.close();
        return ficheroUsuario;
    }

    public static void crearDirectorios() {
        File directorioPuntoDat = new File("Empleados");
        directorioPuntoDat.mkdir();
        File directorioXML = new File("Subvenciones");
        directorioXML.mkdir();
        System.out.println(".> Creando directorios de almacenamiento...");
    }

    public static int crearPuntoDat(String ficheroUsuario) {
        String lineaLeida;
        int objetosEscritos = 0;
        String[] camposLeidos = new String[9];

        try {
            System.out.println(".> Creando flujo de datos para lectura...");
            File ficheroLeido = new File("Incorporaciones/" + ficheroUsuario);
            BufferedReader lectorTxt = new BufferedReader(new FileReader(ficheroLeido));
            System.out.println(".> Fichero .txt leido...");

            System.out.println(".> Creando archivo .dat...");
            File empleadosDat = new File("Empleados/empleadosNavidad.dat");
            System.out.println(".> Creando flujo de datos para la escritura...");
            FileOutputStream ficheroSalida = new FileOutputStream(empleadosDat);
            ObjectOutputStream objetoSalida = new ObjectOutputStream(ficheroSalida);
            System.out.println(".> Comenzando la escritura de objetos...");

            while ((lineaLeida = lectorTxt.readLine()) != null) {
                camposLeidos = lineaLeida.split("#");
                // Validación de número de campos
                if (camposLeidos.length != 9) {
                    System.err.println("Línea con formato incorrecto (se esperaban 9 campos): " + lineaLeida);
                    continue; // saltar esta línea
                }
                try {
                    int edad = Integer.parseInt(camposLeidos[4].trim());
                    double salario = Double.parseDouble(camposLeidos[7].trim().replace(",", "."));
                    Empleado empleado = new Empleado(
                        camposLeidos[0], camposLeidos[1], camposLeidos[2], camposLeidos[3],
                        edad, camposLeidos[5], camposLeidos[6], salario, camposLeidos[8]
                    );
                    objetoSalida.writeObject(empleado);
                    objetosEscritos++;
                } catch (IllegalArgumentException e) {
                    System.err.println("Error en datos numéricos de la línea: " + lineaLeida);
                }
            }

            System.out.println(".> Escritura de objetos terminada...");
            System.out.println(".> Cerrando flujo de datos para lectura del archivo...");
            lectorTxt.close();
            System.out.println(".> Cerrando flujo de datos para escritura de objetos...");
            ficheroSalida.close();
            objetoSalida.close();

        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Archivo no encontrado.");
        } catch (IOException e) {
            System.out.println("ERROR: Se encontró un error a la hora de leer el archivo.");
        }
        return objetosEscritos;
    }

    public static void crearXML(int objetosEscritos) {
        try {
            File binarioLeido = new File("Empleados/empleadosNavidad.dat");
            FileInputStream entradaBinario = new FileInputStream(binarioLeido);
            ObjectInputStream lecturaObjeto = new ObjectInputStream(entradaBinario);
            System.out.println(".> Flujo de datos para la lectura de objetos completo...");

            ArrayList<Empleado> listaObjetos = new ArrayList<>();
            ArrayList<Empleado> menores25 = new ArrayList<>();
            ArrayList<Empleado> mayores55 = new ArrayList<>();
            System.out.println(".> Creando listas de empleados...");

            Empleado empleado;
            for (int i = 0; i < objetosEscritos; i++) {
                empleado = (Empleado) lecturaObjeto.readObject();
                listaObjetos.add(empleado);
            }

            for (int j = 0; j < objetosEscritos; j++) {
                int edadMirada = listaObjetos.get(j).getEdadEmpleado();
                if (edadMirada < 25) {
                    menores25.add(listaObjetos.get(j));
                } else if (edadMirada > 55) {
                    mayores55.add(listaObjetos.get(j));
                }
            }
            System.out.println(".> Añadiendo cada empleado a su lista correspondiente...");

            System.out.println(".> Comenzando escritura de XMLs...");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            System.out.println(".> Instanciando DocumentBuilderFactory...");
            DocumentBuilder builder = factory.newDocumentBuilder();
            System.out.println(".> Construyendo Parser...");

            // Generar XML para menores de 25
            System.out.println(".> Creando XML virtual para empleados menores de 25 años...");
            Document documento25 = builder.newDocument();  // más simple que DOMImplementation
            Element raiz25 = documento25.createElement("Empleados");
            documento25.appendChild(raiz25);
            for (int k = 0; k < menores25.size(); k++) {
                Empleado emp = menores25.get(k);
                Element empleadoElem = documento25.createElement("empleado");
                raiz25.appendChild(empleadoElem);
                crearElemento("DNI", emp.getDniEmpleado(), empleadoElem, documento25);
                crearElemento("Nombre", emp.getNombreEmpleado(), empleadoElem, documento25);
                crearElemento("Apellidos", emp.getApellido1Empleado() + " " + emp.getApellido2Empleado(), empleadoElem, documento25);
                crearElemento("Edad", String.valueOf(emp.getEdadEmpleado()), empleadoElem, documento25);
                crearElemento("Telefono", emp.getTelefonoEmpleado(), empleadoElem, documento25);
                crearElemento("Sexo", emp.getSexoEmpleado(), empleadoElem, documento25);
                double salarioAnual = emp.getSalarioEmpleado() * 14;
                crearElemento("Sueldo", String.valueOf(salarioAnual), empleadoElem, documento25);
            }
            System.out.println(".> XML virtual de empleados menores de 25 años creado...");

            System.out.println(".> Generando XML físico de empleados menores de 25 años...");
            DOMSource source25 = new DOMSource(documento25);
            StreamResult resultado25 = new StreamResult(new File("Subvenciones/Contrataciones25.xml"));
            Transformer transformer25 = TransformerFactory.newInstance().newTransformer();
            transformer25.setOutputProperty(OutputKeys.INDENT, "yes");  // mejora legibilidad
            transformer25.transform(source25, resultado25);
            System.out.println(".> XML físico de empleados menores de 25 años creado con éxito...");

            // Generar XML para mayores de 55 (mismo patrón)
            System.out.println(".> Generando XML virtual para empleados mayores de 55 años...");
            Document documento55 = builder.newDocument();
            Element raiz55 = documento55.createElement("Empleados");
            documento55.appendChild(raiz55);
            for (int k = 0; k < mayores55.size(); k++) {
                Empleado emp = mayores55.get(k);
                Element empleadoElem = documento55.createElement("empleado");
                raiz55.appendChild(empleadoElem);
                crearElemento("DNI", emp.getDniEmpleado(), empleadoElem, documento55);
                crearElemento("Nombre", emp.getNombreEmpleado(), empleadoElem, documento55);
                crearElemento("Apellidos", emp.getApellido1Empleado() + " " + emp.getApellido2Empleado(), empleadoElem, documento55);
                crearElemento("Edad", String.valueOf(emp.getEdadEmpleado()), empleadoElem, documento55);
                crearElemento("Telefono", emp.getTelefonoEmpleado(), empleadoElem, documento55);
                crearElemento("Sexo", emp.getSexoEmpleado(), empleadoElem, documento55);
                double salarioAnual = emp.getSalarioEmpleado() * 14;
                crearElemento("Sueldo", String.valueOf(salarioAnual), empleadoElem, documento55);
            }
            System.out.println(".> XML virtual de empleados mayores de 55 años creado...");

            System.out.println(".> Generando XML físico de empleados mayores de 55 años...");
            DOMSource source55 = new DOMSource(documento55);
            StreamResult resultado55 = new StreamResult(new File("Subvenciones/Contrataciones55.xml"));
            Transformer transformer55 = TransformerFactory.newInstance().newTransformer();
            transformer55.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer55.transform(source55, resultado55);
            System.out.println(".> XML físico de empleados mayores de 55 años creado con éxito...");

            System.out.println(".> Ficheros XML generados con éxito...");
            System.out.println(".> Cerrando los flujos de datos...");
            entradaBinario.close();
            lecturaObjeto.close();
            System.out.println(".> Programa terminado.");

        } catch (ClassNotFoundException e) {
            System.out.println("ERROR: No se ha podido realizar la lectura del objeto.");
        } catch (ParserConfigurationException e) {
            System.out.println("ERROR: No se pudo crear la factoría.");
        } catch (TransformerException e) {
            System.out.println("ERROR: Hubo un fallo con la transformación del archivo");
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: No se pudo encontrar el archivo buscado.");
        } catch (IOException e) {
            System.out.println("ERROR: No se pudo establecer el flujo de lectura y escritura.");
        }
    }

    private static void crearElemento(String datoEmpleado, String valorDato, Element raiz, Document documento) {
        Element elemen = documento.createElement(datoEmpleado);
        Text text = documento.createTextNode(valorDato);
        raiz.appendChild(elemen);
        elemen.appendChild(text);
    }
}