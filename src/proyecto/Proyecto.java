package proyecto;

import java.util.ArrayList;
/**
 *
 * @author Milton
 */

public class Proyecto {
    public static ArrayList<Integer> cromosoma = new ArrayList<>();             //camino que se agrega a una poblacion 
    public static ArrayList<ArrayList<Integer>> poblacion = new ArrayList<>();  //poblacion total
    public static ArrayList<Double> distancias = new ArrayList<>();             //distancias de la poblacion inicial
    public static ArrayList<ArrayList<Integer>> mejores = new ArrayList<>();    //mejor cromosoma de cada generacion
    public static ArrayList<Double> Mdistancias = new ArrayList<>();            //distancia del mejor cromosoma de cada generacion
    
    //coordenadas de las ciudades, el numero indica la coordenada y la posicion la ciudad
    private static final int[] x = { 5, 7, 5, 4, 3, 4, 2, 1, 1, 3, 6, 7, 6, 4, 1, 1, 4, 7, 9, 8,10,11,10,13,12 };
    private static final int[] y = { 4, 4, 6, 3, 6, 5, 4, 3, 5, 2, 3, 7, 1, 1, 1, 7, 7, 2, 2, 5, 4, 1, 7, 6, 8 };
    
    public double distanciaPuntos(int x1, int y1, int x2, int y2){ //calcula la distancia entre dos ciudades
        double dist = Math.sqrt(Math.pow((x1-x2), 2) + Math.pow((y1-y2), 2));
        return dist;
    }

    public void calcularDistancia(int i){ //el parametro que recibe es la posicion del vector que va a usar, para calcular su distancia
        double distancia = 0;
        int rep = 0;
        while(rep<25){
            distancia += distanciaPuntos(x[poblacion.get(i).get(rep)-1], y[poblacion.get(i).get(rep)-1], x[poblacion.get(i).get(rep+1)-1], y[poblacion.get(i).get(rep+1)-1]);
            rep++;
        }  
        distancias.add(distancia);
    }
    
    public void llenarAleatorio(int i){ //crea la poblacion inicial de manera aleatoria
        cromosoma = new ArrayList<>();
        cromosoma.add(1);
        int numero = 0;
        while(true){
            if(cromosoma.size() == 25)
                break;
            numero = (int) (Math.random() * 25) + 1;
            if(!cromosoma.contains(numero)){
                cromosoma.add(numero);
            }
        }
        cromosoma.add(1);
        poblacion.add(cromosoma);
        calcularDistancia(i);
    }

    public void ordenar(ArrayList<ArrayList<Integer>> auxPoblacion, ArrayList<Double> auxDistancias){
        Double keyDistancia;
        ArrayList<Integer> keyList;
        int i;
        for(int j=1; j<auxDistancias.size(); j++){
            keyDistancia = auxDistancias.get(j);
            keyList = auxPoblacion.get(j);
            i = j-1;
            while(i>=0 && auxDistancias.get(i)>keyDistancia){
                auxDistancias.set(i+1, auxDistancias.get(i));
                auxPoblacion.set(i+1, auxPoblacion.get(i));
                i--;
            }
            auxDistancias.set(i+1, keyDistancia);
            auxPoblacion.set(i+1, keyList);
        }
        poblacion = new ArrayList(auxPoblacion);
        distancias = new ArrayList(auxDistancias);
    }
    
    public void cruzamiento(){
        int i = 0, posicion=0, k=0, numero=0;
        int[] posiciones = new int[26];
        
        ArrayList<ArrayList<Integer>> nuevaPoblacion = new ArrayList<>(); //nueva poblacion temporal que reeemplazará a la poblacion original
        while(i<poblacion.size()/4){ //usa la mejor cuarta parte de la poblacion para generar la nueva poblacion
                k=i;
                while(k<i+4){ //usa 4 siguientes de cada padre parahacer elcruzamiento
                    posicion = 0;
                    numero = 0;
                    for(int x=0; x<26; x++) //llena un vector con 0's el sirve para saber que posiciones del padre y madre pondremos en el hijo
                        posiciones[x] = 0;
                    posiciones[0] = 1;
                    posiciones[25] = 1;
                    posicion = poblacion.get(i).indexOf(poblacion.get(i).get(1));
                    posiciones[posicion] = 1;
                    while(true){ //comienza a identificar el ciclo usando el operador CX
                        if(numero == poblacion.get(i).get(1))
                            break;
                        numero = poblacion.get(k+1).get(posicion);
                        posicion = poblacion.get(i).indexOf(numero);
                        posiciones[posicion] = 1;
                        }
                    cromosoma = new ArrayList();
                    for(int x=0; x<26; x++){ //se agregan los valores obtenidos en el ciclo al hijo
                        if(posiciones[x]==1){
                            cromosoma.add(poblacion.get(i).get(x));
                        }else{
                            cromosoma.add(poblacion.get(k+1).get(x));
                        }
                    }
                    nuevaPoblacion.add(cromosoma); //se agrega el hijo a la nueva poblacion
                    k++;
                }
            i++;
        }
        poblacion = new ArrayList(nuevaPoblacion);
    }
    
    public void mutacion(int totalPoblacion){
        int mutar = 0, valor;
        while(mutar < totalPoblacion){
            valor = poblacion.get(mutar).get(3);
            poblacion.get(mutar).set(3, poblacion.get(mutar).get(22));
            poblacion.get(mutar).set(22, valor);
            mutar++;
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Proyecto proy = new Proyecto();
        int i = 0;
        int totalPoblacion = 100;
        int generaciones = 100, evolucionar = 1;
        //Generacion aleatoria
        System.out.println("Generacion Inicial");
        while(i<totalPoblacion){
            proy.llenarAleatorio(i);
            i++;
        }
        //a partir de aqui se hacen las generaciones
        proy.ordenar(poblacion, distancias);
        mejores.add(poblacion.get(0));
        Mdistancias.add(distancias.get(0));
        for(int k=0; k<distancias.size(); k++){
            System.out.println("Camino " + (k+1) + ": " + poblacion.get(k) + ", Distancia: " + distancias.get(k));
        }
        while(evolucionar<=generaciones){
            System.out.println("Generacion " + evolucionar);
            proy.cruzamiento(); //hace el cruzamiento de los padres
            proy.mutacion(totalPoblacion); //muta los cromosomas hijos
            distancias = new ArrayList();
            i = 0;
            while(i<totalPoblacion){ //calcula la distancia de cada cromosoma
                proy.calcularDistancia(i);
                i++;
            }   
            proy.ordenar(poblacion, distancias); //ordena en base a la lista de distancias
            mejores.add(poblacion.get(0)); //guarda el mejor cromosoma de cada generacios
            Mdistancias.add(distancias.get(0)); //guada la distancia del mejor cromosoma de la generacion
            for(int k=0; k<distancias.size(); k++){
                System.out.println("Camino " + (k+1) + ": " + poblacion.get(k) + ", Distancia: " + distancias.get(k));
            } 
            evolucionar++;
        }
        System.out.println("Mejores de cada Generacion");
        for(int j=0; j<mejores.size(); j++)
            System.out.println("Generacion " + j + ": " + mejores.get(j) + ", Distancia: " + Mdistancias.get(j));
    }
}
