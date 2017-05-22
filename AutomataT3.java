import java.io.*;
import java.util.*;

public class AutomataT3{
	private ArrayList<States> estados;
	private int names;
	private boolean isDeterministic;
	private String entradas="";
	private String aceptacion="";



	public AutomataT3(){
		//name es una variable que se usa para nombrar los estados
		// apartir de su valor en ascii, el valor 65 pertenece a "A"
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
			entradas="";
			aceptacion="";
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
			System.out.println("Es determinista: "+((isDeterministic) ? "Si" : "No") );
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
		/////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////
		// parte para determinar automatas tipo 3, no determinados
		//////////////////////////////////////////////////////////////
		// aglutinar estados de entrada
		//guardar outputs para aglutinar

		String newStateName="";



		//ArrayList<States> undefinedStates= new  ArrayList<States>();
		ArrayList<String> undefinedStates= new  ArrayList<String>();
		ArrayList<Outputs> auxOuts= new  ArrayList<Outputs>();
		ArrayList<String> definedStates= new  ArrayList<String>();

		for(States s: estados){

			if(s.esEntrada()){
				//se recopilan sus outputs para menejarse despues
				for(Outputs o: s.getOutputs()){
					auxOuts.add(o);
				}


				//la primer entrada

				if(newStateName.length()==0){
					newStateName=s.getName();

				}else{
					String auxNameState="";
					int i=0;

					// se considera que los nombres ya van arreglados
					// como es el aglutinamiento los nombres aun son de un caracter
					char aux1= s.getName().charAt(0);


					while(i<newStateName.length() && aux1>newStateName.charAt(i)){
						auxNameState+=newStateName.charAt(i);
						i++;
					}
					if(i==newStateName.length()){
						newStateName+=s.getName();
					}else{
						newStateName=aux1+s.getName()+newStateName.charAt(i);
					}

				}
				/////////////////////
				///

			}

		}

		//creando el primer estado de nuestro automata
		System.out.println("///////////////////////////////////////");
		aux.setState(newStateName,auxOuts,aceptacion, entradas);


				definedStates.add(newStateName);
				for(States s: aux.getSatates()){
					for(Outputs o: s.getOutputs()){
						if(!definedStates.contains(o.getNextStateName())){
							undefinedStates.add(o.getNextStateName());
						}
					}
				}


				////// se insertan los estados no definidos
				while(!undefinedStates.isEmpty()){
					System.out.println("Undefined size:"+undefinedStates.size());

					Object [] auxStates=undefinedStates.toArray();

					for (int i=0; i<auxStates.length ; i++) {
						System.out.println("Undefined State name:" + auxStates[i].toString());
						aux.addUndefinedState(createUndefinedState(auxStates[i].toString()));
						definedStates.add(auxStates[i].toString());
					}
					undefinedStates=new  ArrayList<String>();
					for(States s: aux.getSatates()){
						System.out.println("State Name: "+ s.getName());
						String auxOutName="";
						for(Outputs o: s.getOutputs()){
							if(!definedStates.contains(o.getNextStateName())){
								auxOutName+=o.getNextStateName();
							}
						}
						undefinedStates.add(sortLabels(auxOutName));

					}
				}





		//Revisar estados
		aux.getStatesInfo();
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

// para crear los estados cuando se va determinando
public void setState(String name, ArrayList<Outputs> out, String aceptacion, String entrada){
	States auxS= new States(name);
	String auxNameR0 ="";
	String auxNameR1 ="";
	int auxW0=0;
	int auxW1=0;
	for(Outputs o: out){
		// juntar todos los outputs generados y aglutinarlos por lo que leen
		if(o.getReadsBit()==0){
			auxNameR0+=o.getNextStateName();
			if(o.getWritesBit()==1){
				auxW0=1;
			}
		}

		if(o.getReadsBit()==1){
			auxNameR1+=o.getNextStateName();
			if(o.getWritesBit()==1){
				auxW1=1;
			}
		}
	}

	auxNameR0= sortLabels(auxNameR0);
	auxNameR1= sortLabels(auxNameR1);


	auxS.crateOuts(auxNameR0,0,auxW0);
	auxS.crateOuts(auxNameR1,1,auxW1);
	auxS.soyEntrada(entrada);
	auxS.soyAceptacion(aceptacion);

	this.estados.add(auxS);

}
/////mantener en orden alfabetico los strings
public String sortLabels(String input){
	String res="";
	char arrChar[]= new char[input.length()];
	for (int i=0; i<input.length(); i++) {
		arrChar[i]=input.charAt(i);
	}
	Arrays.sort(arrChar);
	for (int i=0; i<arrChar.length; i++) {
		if(i>0 && arrChar[i-1]!=arrChar[i]){
			res+=arrChar[i];
		}else if(i==0) {
			res+=arrChar[i];
		}

	}
	return res;
}
public void getStatesInfo(){
	for(States s: estados){
		s.getInfo();
	}
}
public ArrayList<States> getSatates(){
	return estados;
}

public States createUndefinedState(String name){
	States res= new States(name);
	ArrayList<Outputs> auxOuts= new  ArrayList<Outputs>();
	String auxNameR0 ="";
	String auxNameR1 ="";
	int auxW0=0;
	int auxW1=0;

	for(States s:estados){
		// se considera que un automata al definirse tiene nombres de un caracter de longitud
		if( name.indexOf(s.getName().charAt(0))>=0 ){
			for(Outputs o: s.getOutputs()){
				// juntar todos los outputs generados y aglutinarlos por lo que leen
				if(o.getReadsBit()==0){
					auxNameR0+=o.getNextStateName();
					if(o.getWritesBit()==1){
						auxW0=1;
					}
				}

				if(o.getReadsBit()==1){
					auxNameR1+=o.getNextStateName();
					if(o.getWritesBit()==1){
						auxW1=1;
					}
				}
			}
		}
	}
	auxNameR0= sortLabels(auxNameR0);
	auxNameR1= sortLabels(auxNameR1);


	res.crateOuts(auxNameR0,0,auxW0);
	res.crateOuts(auxNameR1,1,auxW1);
	res.soyEntrada(entradas);
	res.soyAceptacion(aceptacion);

	return res;
}

public void addUndefinedState(States  s){
	estados.add(s);
}

}
