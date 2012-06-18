package infiniware;

import java.util.StringTokenizer;

import infiniware.automatas.Automata;
import infiniware.automatas.Configurador;
import infiniware.procesos.Procesos;
import org.apache.commons.lang3.StringUtils;

public class Sistema {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Comprobamos argumentos
        validar(args);

        //Configuramos el sistema
        configurar(args);

        //Iniciamos el proceso
        iniciar(args);
    }

    private static void validar(String[] args) {
        String instrucciones = "java -Djava.security.policy=java.security.AllPermission infiniware.Sistema <" + StringUtils.join(Procesos.values(), '|') + ">";
        switch (args.length) {
            case 1:
                if (args[0].equals("-h") || args[0].equals("--help")) {
                    System.out.println(instrucciones);
                    System.exit(0);
                } else {
                    try {
                        Procesos.valueOf(args[0]);
                    } catch (IllegalArgumentException e) {
                        System.out.println("La proceso \"" + args[0] + "\" no existe.\n");
                        e.printStackTrace();
                        System.exit(-1);
                    }
                }
                break;
            case 4:
            	for(int i= 0; i < args.length; i++){
	            	if (args[i].equals("-h") || args[i].equals("--help")) {
	                    System.out.println(instrucciones);
	                    System.exit(0);
	                } else {
	                    try {
	                        Procesos.valueOf(args[i]);
	                    } catch (IllegalArgumentException e) {
	                        System.out.println("La proceso \"" + args[0] + "\" no existe.\n");
	                        e.printStackTrace();
	                        System.exit(-1);
	                    }
	                }
        		}
            	for(int i= 1; i < args.length; i++){
	            	StringTokenizer st = new StringTokenizer(args[i],".");
	            	if(st.countTokens()!=4){
	            		System.out.println("La IP \"" + args[i] + "\" no tiene el formato correcto. \"XXXX.XXXX.XXXX.XXXX\"\n");
                        System.exit(-1);
	            	}else System.exit(0);
            	}
                break;
            default:
                System.err.println("Sintaxis incorrecta:");
                System.err.println("\t" + instrucciones);
                System.exit(-1);
        }
    }

    private static void configurar(String[] args) {
        Configurador.configurar();
        if(args.length == 4){
        	for(int i= 1; i < args.length; i++){
        		Automata.INSTANCIAS.get(i).setHost(args[i]);
        	}
    	}
    }

    private static void iniciar(String[] args) {
        Procesos.iniciar(args[0]);
    }
}
