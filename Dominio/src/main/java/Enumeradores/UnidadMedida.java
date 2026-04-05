package Enumeradores;

/**
 * Posibles unidades medida para un ingrediente
 * @author Andre
 */
public enum UnidadMedida {
    PIEZAS,
    KILOGRAMOS,
    LITROS;
    
    @Override
    public String toString() {
        switch (this) {
            case PIEZAS: return "Piezas";
            case KILOGRAMOS: return "Kilogramos";
            case LITROS: return "Litros";
            default: return name();
        }
    }
}