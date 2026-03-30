package Utilerias;
import Coordinadores.CoordinadorPantallas;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * En este archivo se aplica la lógica de diferentes partes del sistema
 * Como botones o sistemas de filtrado por búsqueda
 * 
 * @author Andre
 */
public class UtilLogica {
    
    /**
     * Se encarga de inyectar lógica en todo los botones CRUD
     * Itera sobre el tamaño de OPCIONES_CRUD_MINUS, obtiene lo necesario y lo pone en el mapa
     * Modifica el mapa del parámetro, por lo que la lista va a quedar con botones funcionales
     * 
     * @param panel
     * @param botones El mapa String, BotonPersonalizado que crea el método dibujarBotonesCRUD
     * @param dialogos Un arrayList de suppliers de JDialog en este orden: Registrar, Actualizar, Eliminar
     */
    public static void inyectarLogicaCRUD(JPanel panel, Map<String, JButton> botones, ArrayList<Supplier<? extends JDialog>> dialogos) {
        
        //Rescata en una variable el arreglo de opciones para manejarlo fácilmente
        String[] opciones = Constantes.OPCIONES_CRUD_MINUS;
        
        //Itera sobre el arreglo de opciones. O sea, trabaja sobre los botones definidos
        for (int i = 0; i < opciones.length; i++) {
            
            //Crea un botón ya con lógica de abrir el formulario
            //Si es 0, se trata de refrescar y no debe tener esa lógica
            String opcion = opciones[i];
            if (i > 0){
   
                //El arreglo de suppliers solo tiene tres elementos, debe ser ajustado
                int indiceDialogo = i-1;
                
                //Guarda el botón actual del mapa de botones
                //Como opciones no tiene "Refresca", se ajusta el índice por desfase
                JButton boton = botones.get(opciones[i]);
                
                //Guarda el supplier actual
                Supplier<? extends JDialog> dialogo = dialogos.get(indiceDialogo);
                
                //Inyecta logica al botón
                boton.addActionListener(e -> {
                    CoordinadorPantallas.getInstance().abrirDialogo(dialogo);
                });
            }
        }
    }
    

    
    /**
     * Inyecta la lógica a los botones de búsqueda de filtrado
     * Se guía con los arreglos de los parámetros para saber qué filtrar y qué indices usar
     * La primera parte consiste en aplicar la lógica a los botones creados para que funcionen al hacer clic
     * La segunda parte se apoya de DocumentListener para sentir los cambios en tiempo real
     * 
     * @param campoBuscar
     * @param filtros
     * @param columnasTabla
     * @param botonesFiltros
     * @param sorter
     * @param columnaActiva 
     */
    public static void inyectarLogicaBusqueda(JTextField campoBuscar,
                                            String[] filtros,
                                            String[] columnasTabla,
                                            Map<String, JButton> botonesFiltros, 
                                            TableRowSorter<DefaultTableModel> sorter,
                                            int[] columnaActiva) {
        
        //Es la condición base del Regex, que filtra cualquier cosa que coincida
        String regexBase = "(?i)";
        
        //Bucle que creará un botón con lógica según los necesarios (filtrar Lógica)
        //Entonces todo lo que pasa dentro es por cada iteración
        for (String filtro: filtros) {

            //Busca el botón en el mapa con la llave (que es filtro)
            JButton boton = botonesFiltros.get(filtro.toLowerCase());
            
            //Añade lógica al botón
            if (boton != null) {
                boton.addActionListener(e -> {
                    
                    /**
                    * La columna respecto a la tabla cuyo campo será usado para filtrar
                    * Convierte el arreglo en una lista para usar indexOf
                    * Busca el índice con base en el texto, o sea, en el filtro anteriormente declarado
                    * Utiliza "filtro" como parámetro, así que no importa el orden, sabe el índice del campo a filtrar
                    *
                    */
                    int columnaFiltrada = Arrays.asList(columnasTabla).indexOf(filtro);
                    
                    //Guarda el índice a filtrar en el arreglo para que se pueda usar
                    columnaActiva[0] = columnaFiltrada; 
                    
                    //Llama al método auxiliar para aplicar los filtros de regex
                    aplicarFiltro(columnaActiva[0], regexBase, campoBuscar, sorter);
                });
            }
        }
        
        
        /**
         * getDocument da acceso interno a donde se guarda el texto
         * addDocumentListener es que detecta cualquier cambio en el texto ingresado
         * Tiene tres overrides:
         * -insertUpdate actualiza cuando le agregas texto
         * -removeUpdate actualiza cuando le quitas texto
         * -changedUpate actualiza cuando cambian los atributos del texto
         * Y cada uno sabe que debe hacerlo cuando se llama al método interno filtrar()
         * Filtrar() llama al método aplicarFiltro que está más abajo
         */
        campoBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            private void filtrar() {
                aplicarFiltro(columnaActiva[0], regexBase, campoBuscar, sorter);
            }
        });  
    }
    
    
    
    /**
     * Centraliza la lógica de aplicar un filtro pues se aplica en más de un punto
     * 
     * @param columnaActiva índice de la columna de la tabla a filtrar
     * @param regexBase Condición del regex
     * @param campoBuscar
     * @param sorter Mecanismo que filtra los datos de tableModel sin modificarlos
     */
    private static void aplicarFiltro(int columnaActiva, 
                                         String regexBase, 
                                         JTextField campoBuscar,
                                         TableRowSorter<DefaultTableModel> sorter) {
        
         //Regex completo, que incluye condición y el texto a buscar tal cual
         String regex = regexBase +  campoBuscar.getText();

         //Filtra dinámicamnente los elementos de la tabla
         sorter.setRowFilter(RowFilter.regexFilter(regex, columnaActiva));
    } 
}