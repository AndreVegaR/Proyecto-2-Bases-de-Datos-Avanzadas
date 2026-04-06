package utilerias;

import excepciones.NegocioException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Lógica repetida sobre decisiones de negocio
 *
 * @author Andre
 */
public class UtilNegocio {

    //Mensajes centralizados
    private static String NULO = "Objeto vacío";
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
     * Formato para normalizar una fecha hora en un string cómodo de leer
     */
    public static DateTimeFormatter FORMATO_FECHA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    //Regex para validar que el precio solo sea numeros
    private static String REGEX_PRECIO = "^-?\\d+(\\.\\d+)?$";
    
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

        Field[] campos = objeto.getClass().getDeclaredFields();
        try {
            for (Field campo : campos) {

                if (campo.getType().equals(String.class)) {

                    campo.setAccessible(true);

                    if (esCampoOpcional(campo)) {
                        continue;
                    }

                    String valor = (String) campo.get(objeto);

                    if (valor == null || valor.isBlank()) {
                        System.out.println("String inválido: " + campo.getName());
                        return true;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new NegocioException("Todos los campos especificados de " 
                    + objeto.getClass().getSimpleName() + " deben ser obligatorios");
        }
        return false;
    }

    /**
     * Determina si un objeto es nulo
     * Imprime también el objeto en cuestión
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

    /**
     * Transforma una fecha en un String legible y bonito para presentación
     * Solo se usa en el flujo entidad -> DTO
     * Utiliza una constante definida en UtilNegocio
     *
     * @param fecha a formatear
     * @return fecha formateada en String
     */
    public static String formatearFecha(LocalDateTime fecha) {
        UtilNegocio.esNulo(fecha);
        return fecha.format(FORMATO_FECHA_HORA);
    }

    /**
     * Transforma un String de fecha en un LocalDateTime
     * Solo se usa en el flujo DTO -> Entidad
     * Utiliza la misma constante para un proceso inverso
     *
     * @param fechaString a desformatear
     * @return fecha en LocalDateTime
     */
    public static LocalDateTime desformatearFecha(String fechaString) {
        if (fechaString == null || fechaString.isBlank()) {
            return LocalDateTime.now();
        }
        return LocalDateTime.parse(fechaString, FORMATO_FECHA_HORA);
    }

    /**
     * Crea un folio único para una comanda
     * Utiliza un prefijo establecido, la fecha y el número de la comanda de ese día
     * Este número se lo pasa el BO, al que llama a un método del DAO
     * Está aquí porque es formato, es traducción
     * BO solo orquesta dándole el número consecutivo
     *
     * @param numConsecutivo
     * @return el folio armado
     */
    public static String crearFolio(int numConsecutivo) {

        String PREFIJO = "OB";

        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String clave = String.format("%03d", numConsecutivo);

        return PREFIJO + "-" + fecha + "-" + clave;
    }
    
    /*
    Metodo para validar el precio para que no sean string o sea null
    */
    public static Double validarPrecio(String precio){
       if (precio == null || precio.trim().isEmpty()) {
       throw new NegocioException("El precio no puede estar vacío");
         }
       if (!precio.matches(REGEX_PRECIO)) {
      throw new NegocioException("Escriba un precio válido");
    }
    return Double.parseDouble(precio);
    }
}