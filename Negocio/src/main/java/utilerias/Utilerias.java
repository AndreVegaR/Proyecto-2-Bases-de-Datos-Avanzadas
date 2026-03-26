package utilerias;

import excepciones.NegocioException;

/**
 * Lógica repetida sobre decisiones de negocio
 * @author Andre
 */
public class Utilerias {
    
    private static String NULO = "El objeto es nulo";
    
    public static void esNulo(Object o) {
        if (o == null) {
            throw new NegocioException(NULO);
        }
    }
}