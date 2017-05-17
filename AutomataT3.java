import java.io.*;
import java.util.*;

public class AutomataT3{
	private ArrayList<States> estados;
	private int names;
	private boolean isDeterministic;



	public AutomataT3(){
		names=65;
		estados = new  ArrayList<States>();
		isDeterministic=true;

	}

	public void setAutomat(String file){
		try {
			FileReader fileReader = new FileReader(file);
		 BufferedReader br = new BufferedReader(fileReader);
			String line = br.readLine();
			String [] data, splitEstados;
			String entradas="";
			String aceptacion="";
			String estadosRev="";
			int auxNames=names;
			int row=0;

			while (line != null) {

				data= line.split("	");

				System.out.println("linea :"+row);
				System.out.println(line);
				////Se define el automata

				// los outputs al leer en cero y en 1
					int writeAt0 = Integer.parseInt(data[1]);
					int writeAt1 = Integer.parseInt(data[3]);

				/// el primer renglon es la lectura de los estados de entrada(nota tal ez no sellaman asi, verificar)
				if(row==0){

					splitEstados= data[0].split(",");

					// no se verifica que este repetido por que es lo primero que reciven las variables
					for (int i=0; i<splitEstados.length ; i++ ) {

						//añadido para verificar los estados de entrada
							entradas+=splitEstados[i];

						// añadido para agregar, o no , el estado vacio
							estadosRev+=splitEstados[i];

						//Para verificar cuales son los estados de aceptación
						if( writeAt0==1){
							aceptacion+=splitEstados[i];
						}
					}


						// se buscan todos los estados posibles para entrada cuando se lee un uno
					splitEstados= data[2].split(",");
					// a qui sí se verifica, pues puede que solo exista una entrada
					for (int i=0; i<splitEstados.length ; i++ ) {
						// añadido para agregar qué son entradas
						if(entradas.indexOf(splitEstados[i])<0){
							entradas+=splitEstados[i];
						}

						// añadido para agregar, o no , el estado vacio
						if(estadosRev.indexOf(splitEstados[i])<0){
							estadosRev+=splitEstados[i];
						}

						//Para verificar cuales son los estados de aceptación
						if(aceptacion.indexOf(splitEstados[i])<0 && writeAt1==1){
							aceptacion+=splitEstados[i];
						}
					}
				}else{
					States aux= new States( Character.toString((char) auxNames));
					if( estadosRev.indexOf( Character.toString((char) auxNames) ) <0){
						estadosRev+= Character.toString((char) auxNames);
					}

						// se buscan todos los estados posibles para entrada cuando se lee un cero
						splitEstados= data[0].split(",");

						for (int i=0; i<splitEstados.length ; i++ ) {
							aux.crateOuts(splitEstados[i],0,writeAt0);
							// añadido para agregar, o no , el estado vacio
							if(estadosRev.indexOf(splitEstados[i])<0){
								estadosRev+=splitEstados[i];
							}
							//Para verificar cuales son los estados de aceptación
							if(aceptacion.indexOf(splitEstados[i])<0 && writeAt0==1){
								aceptacion+=splitEstados[i];
							}
						}
						//Si existe un estado con una coma quiere decir que hay dos
						// no es determinista
						if(splitEstados.length>1){
							isDeterministic=false;
						}
							// se buscan todos los estados posibles para entrada cuando se lee un uno
						splitEstados= data[2].split(",");

						for (int i=0; i<splitEstados.length ; i++ ) {
							aux.crateOuts(splitEstados[i],1,writeAt1);
							// añadido para agregar, o no , el estado vacio
							if(estadosRev.indexOf(splitEstados[i])<0){
								estadosRev+=splitEstados[i];
							}
							//Para verificar cuales son los estados de aceptación
							if(aceptacion.indexOf(splitEstados[i])<0 && writeAt1==1){
								aceptacion+=splitEstados[i];
							}
						}
						// de la misma mare ase dice que es no determinista
						if(splitEstados.length>1){
							isDeterministic=false;
						}



					estados.add(aux);
					auxNames++;
				}

				line = br.readLine();
				row++;
			}





			System.out.println("Estados de entrada: "+entradas);
			System.out.println("Estados de aceptación: "+aceptacion );
			System.out.println("Estados: "+estadosRev);
			auxNames-=names;
			System.out.println(""+auxNames);
			//si el numero de estados descritos en la tabla
			//es menor al numero de estados descritos en la tabla, significa que existe un estado vacio
			if(estadosRev.length()-auxNames>0){
				auxNames+=names;
				States aux= new States( Character.toString((char) auxNames));
				aux.crateOuts(Character.toString((char) auxNames),0,0);
				aux.crateOuts(Character.toString((char) auxNames),1,0);

				estados.add(aux);
			}

			for (States s: estados) {
				s.soyEntrada(entradas);
				s.soyAceptacion(aceptacion);
				s.getInfo();
			}


			br.close();
		}catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + "'");
        }catch(IOException ex) {

             ex.printStackTrace();
        }

}

public void GetDeterministicAut(){
	AutomataT3 aux = new AutomataT3();
	if(isDeterministic){
		// si es determinista se devuelve el mismo automata que se le dio como input
		writeAutomata(this.estados,"Deterministic");
	}else{

	}
}

public void writeAutomata(ArrayList<States> states, String name){
	ArrayList<States> auxS=states;
	String auxName="";
	int writes0=0,writes1=0;
	String tab="	";
	try{
    PrintWriter writer = new PrintWriter(name+"AutomataT3.txt", "UTF-8");
		for (States s : auxS) {

			// se escribe primero la entrada
			if(s.esEntrada()){
					//por el tipo de formato se omite preguntar si es algo más
						if(auxName.equals("")){
							auxName+=s.getName();
							writes0 = ((s.esAceptacion()) ? 1 : 0);
							writes1 = ((s.esAceptacion()) ? 1 : 0);
						}else{
							auxName+=","+s.getName();
						}

			}


		}
		//realmente no deberia importarnos que escribe en 0 y en uno, porque es una entrada
		//si es estado de aceptacion lo obvio es que las dos columnas tengan un 1
		writer.println(auxName+tab+writes0+tab+auxName+tab+writes1);
		for(States s: auxS){
			writer.println(s.writeOutputs());
		}
    writer.close();
} catch (IOException e) {
   // do something
}
}


}
