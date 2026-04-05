package utilerias;

import excepciones.NegocioException;
import java.lang.reflect.Field;

/**
 * Lógica repetida sobre decisiones de negocio
 *
 * @author Andre
 */
public class UtilNegocio {

    //Mensajes centralizados
    private static String NULO = "El objeto es nulo";
    private static String VACIO = "no puede estar vacio";
    private static String POSITIVO = "debe ser un número positivo";

    // Regex para validar que el nombre contenga al menos una letra
    private static final String REGEX_NOMBRE = ".*[a-zA-ZáéíóúÁÉÍÓÚñÑ].*";

    /**
     * Valida que un nombre contenga al menos una letra
     *
     * @param nombre a validar
     */
    public static void validarNombre(String nombre) {
        if (!nombre.matches(REGEX_NOMBRE)) {
            throw new NegocioException("El nombre debe contener al menos una letra");
        }
    }

    //Regex para validar el formato de un correo electrónico
    private static final String REGEX_CORREO = "^[A-Za-z0-9._%+-]" //Uno o más caracteres de usuario
            + "+@" //Arroba obligatoria entre usuario y dominio
            + "[A-Za-z0-9.-]+" //Caracteres para dominio
            + "\\." //Tiene que haber un punto para marcar el tipo (.com, .gob...)
            + "[A-Za-z]{2,}$"; //Caracteres del tipo de organización, mínimo dos letras

    //Regex para validar un teléfono: diez dígitos cualesquiera, del 0 al 9
    private static final String REGEX_TELEFONO = "^\\d{10}$";

    /**
     * Este arreglo de Strings define qué campos de los atributos de cualquier
     * clase son opcionales Sirve para ser comparados en el método
     * esCampoOpcional, auxiliar de stringsVacios Los elementos del arreglo son
     * los NOMBRES LITERALES DEL ATRBIBUTO EN JAVA Si coincide, no hace la
     * validación de que si es blank o nulo: puede existir vacío
     */
    private static String[] CAMPOS_OPCIONALES = {"correo"};

    /**
     * Compara que el campo del parámetro se halle en el arreglo de campos
     * opcionales Es auxiliar de stringsVacios Si hay una coincidencia, lo deja
     * pasar
     *
     * @param campo a verificar
     * @return si es posible que exista vacío o no
     */
    private static boolean esCampoOpcional(Field campo) {
        for (String campoOpcional : CAMPOS_OPCIONALES) {
            if (campo.getName().equalsIgnoreCase(campoOpcional)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Método reflexivo que valida si sus campos (por lo menos strings) no sean
     * blank Es genérico en el ámbito de que puede procesar cualquier tipo de
     * objeto La reflexión consiste en que el código se analiza en tiempo de
     * ejecución Es una herramienta poderosa: con decir que puede acceder a
     * atributos privados es suficiente
     *
     * @param objeto a validar sus Strings
     * @return si son válidos o no
     */
    public static boolean stringsVacios(Object objeto) {
        if (objeto == null) {
            return true;
        }

        //Obtiene todos los campos en tiempo de ejecución
        Field[] campos = objeto.getClass().getDeclaredFields();
        try {
            for (Field campo : campos) {

                //Filtra solo los strings
                if (campo.getType().equals(String.class)) {

                    //Desbloquea los atributos privados
                    campo.setAccessible(true);

                    //Descarta si es un campo opcional
                    if (esCampoOpcional(campo)) {
                        continue;
                    }

                    //Convierte a String cada campo del objeto
                    String valor = (String) campo.get(objeto);

                    //Verifica que no sea nulo o blank (String vacío)
                    if (valor == null || valor.isBlank()) {
                        System.out.println("String inválido: " + campo.getName());
                        return true;
                    }
                }
            }
        } //Excepción si algo sale mal
        catch (IllegalAccessException e) {
            throw new NegocioException("Todos los campos especificados de " + objeto.getClass().getSimpleName() + "son obligatorios");
        }
        return false;
    }

    /**
     * Determina si un objeto es nulo
     *
     * @param o Objeto
     */
    public static void esNulo(Object o) {
        if (o == null) {
            throw new NegocioException(NULO);
        }
    }

    /**
     * Une las dos validaciones más importantes: de String y objeto null El
     * combo ganador, el clásico esNulo() y stringsVacios() siguen siendo
     * públicos porque se puede necesitar
     *
     * @param o
     */
    public static void validarObjeto(Object o) {
        UtilNegocio.esNulo(o);
        UtilNegocio.stringsVacios(o);
    }

    /**
     * Determina si una cadena está vacía Algo desactualizada 👀 👀 👀 👀,
     * revisar el método stringsVacios() La dejé para no fregarme trabajo ajeno
     *
     * @param cadenaTexto
     * @param nombreCampo
     */
    public static void esCadenadaVacia(String cadenaTexto, String nombreCampo) {
        esNulo(cadenaTexto);
        if (cadenaTexto.trim().isEmpty()) {
            throw new NegocioException(nombreCampo + " " + VACIO);
        }
    }

    /**
     * Determina si un número es mayor a 0
     *
     * @param n
     * @param nombreCampo
     */
    public static void esNumeroPositivo(Number n, String nombreCampo) {
        esNulo(n);
        if (n.doubleValue() < 0) {
            throw new NegocioException(nombreCampo + " " + POSITIVO);
        }
    }

    /**
     * Valida un teléfono usando el regex constante
     *
     * @param telefono a validar
     */
    public static void validarTelefono(String telefono) {
        if (!telefono.matches(REGEX_TELEFONO)) {
            throw new NegocioException("Teléfono en formato inválido");
        }
    }

    /**
     * Valida un correo electrónico usando el regex constante
     *
     * @param correo a valida
     */
    public static void validarCorreo(String correo) {
        if (!correo.matches(REGEX_CORREO)) {
            throw new NegocioException("Correo en formato inválido");
        }
    }
}
