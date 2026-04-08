# ProgramaEligeTuCesta

Este proyecto en Java fue un ejercicio de la asignatura Acceso a Datos del grado superior de Desarrollo de aplicaciones multiplataforma.
Permite leer un fichero de empleados, generar un archivo .dat con los objetos Empleado y crear archivos XML distintos para empleados menores de 25 años y mayores de 55 años.

## Estructura del proyecto

- src/ProgramaEligeTuCesta/ → Contiene las clases Java Empleado.java y EligeTuCesta.java
- Incorporaciones/ → Contiene los ficheros de entrada .txt con los datos de los empleados
- Subvenciones/ → Carpeta donde se generan los archivos XML de salida (Contrataciones25.xml y Contrataciones55.xml)
- .gitignore → Ignora archivos .dat, .class, .log y directorios de compilación

## Cómo usarlo

1. Abrir el proyecto en Eclipse u otro IDE.
2. Crear un fichero de texto con los empleados siguiendo el formato:

ID#Nombre#Apellido1#Apellido2#Edad#Sexo#Teléfono#Salario#DNI

3. Ejecutar EligeTuCesta.java
4. Se generarán:
   - Empleados/empleadosNavidad.dat (archivo binario con objetos Empleado)
   - Subvenciones/Contrataciones25.xml → empleados menores de 25 años
   - Subvenciones/Contrataciones55.xml → empleados mayores de 55 años

## Notas

- El programa valida edad (0–120) y salario (no negativo)
- Líneas del .txt con formato incorrecto se ignoran y muestran un mensaje de error en consola
