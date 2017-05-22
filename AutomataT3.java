import java.awt.*;
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
			auxNames-=names;
			System.out.println(estadosRev.length()-auxNames);
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

public AutomataT3 GetDeterministicAut(){

	AutomataT3 aux = new AutomataT3();
	if(isDeterministic){
		// si es determinista se devuelve el mismo automata que se le dio como input
		writeAutomata(this.estados,"Deterministic");
		for(States s: estados){
			aux.addUndefinedState(s);
		}
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
				int sCount=0;

				while(!undefinedStates.isEmpty() ){
					System.out.println("Undefined size:"+undefinedStates.size());

					Object [] auxStates=undefinedStates.toArray();

					for (int i=0; i<auxStates.length ; i++) {
						System.out.println("Undefined State name:" + auxStates[i].toString());
						aux.addUndefinedState(createUndefinedState(auxStates[i].toString()));
						definedStates.add(auxStates[i].toString());
					}
					undefinedStates=new  ArrayList<String>();
					//se busca en los nuevos estados si los estados a los que llegan no estan definidos todavia
					for(States s: aux.getSatates()){
						for(Outputs o: s.getOutputs()){
							System.out.println("Esta definido el estado: "+o.getNextStateName()+" "+ ((definedStates.contains(o.getNextStateName())) ? "-- Si" : "// No ") );
							if(!definedStates.contains(o.getNextStateName()) && !undefinedStates.contains(o.getNextStateName()) ){
								undefinedStates.add(o.getNextStateName());
							}
						}
					}

				}


		//su variable para saber si es determinista cambia a cierta
		isDeterministic=true;


		//Revisar estados
		aux= aux.renameIt(aux);

		aux.getStatesInfo();
		writeAutomata(aux.getSatates(),"Deterministic");

	}
	return aux;
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


	Set<Character> charSet = new LinkedHashSet<Character>();
	for (char c : arrChar) {
    charSet.add(c);
	}
	for (Character character : charSet) {
    res+=character;
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

	for(States s: this.estados){
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

public AutomataT3 renameIt(AutomataT3 a){
	int auxNames= names;
	ArrayList<States> auxS=a.getSatates();
	String [][] table= new String[a.getSatates().size()][];
	AutomataT3 aux =new AutomataT3();
	for (int i=0; i<table.length; i++ ) {
		table[i]=new String[2];
		table[i][0]=auxS.get(i).getName();
		table[i][1]=Character.toString((char) auxNames);
		auxNames++;
	}
	String auxEntrada="";
	String auxAcept="";
	for(States s: auxS){
		int name=0;
		while(name<table.length && !table[name][0].equals(s.getName())){
			name++;
		}

		States temp= new States(table[name][1]);
		if(s.esEntrada()){
			auxEntrada+=table[name][1];
		}
		if(s.esAceptacion()){
			auxAcept+=table[name][1];
		}
		for(Outputs o: s.getOutputs()){
			int nameO=0;

			while(nameO<table.length && !table[nameO][0].equals(o.getNextStateName()) ) {
				System.out.println("OutName: "+o.getNextStateName());
				System.out.println("Compare to:" +table[nameO][0]);
				nameO++;
			}
			temp.crateOuts(table[nameO][1], o.getReadsBit(), o.getWritesBit());
		}
		temp.soyEntrada(auxEntrada);
		temp.soyAceptacion(auxAcept);
		aux.addUndefinedState(temp);
	}

	return aux;
}

public ArrayList<States> minimiza(ArrayList<States> fda) {

	ArrayList<HashSet<HashSet<Integer>>> particiones = new ArrayList<HashSet<HashSet<Integer>>>();

	int tam = 0;
	    //llenar con las posibles paritciones iniciales
	//las dividimos en las que escriben 00 , 01, 10 y 11

	/*HashSet<Integer> inicialCC = new HashSet<Integer>();
	HashSet<Integer> inicialUC = new HashSet<Integer>();
	HashSet<Integer> inicialCU = new HashSet<Integer>();
	HashSet<Integer> inicialUU = new HashSet<Integer>();*/
	HashSet<Integer> aceptacion = new HashSet<Integer>();
	HashSet<Integer> noAceptacion = new HashSet<Integer>();


	for (int i = 0; i < fda.size(); i++) {



	    if(fda.get(i).esAceptacion())
		aceptacion.add(i);
	    else
		noAceptacion.add(i);
	}

	//particion inicial
	HashSet<HashSet<Integer>> inicial = new HashSet<>();
	//solo agregamos conjuntos de estados existentes
	//anadimos la primer particion a la lista de particiones
	//System.out.println(aceptacion);
	inicial.add(aceptacion);
	inicial.add(noAceptacion);
	particiones.add(inicial);

	int index = 0;
	ArrayList<Point> opciones;
	HashSet<HashSet<Integer>> pOriginal;
	//procedemos a reducir
	do {

	    pOriginal = particiones.get(index);
	    //HashSet<String> pSiguiente =  new HashSet<String>();
	    opciones = new ArrayList<>();
	    //ArrayList<Outputs> temp = new ArrayList<>();
	    //revisamos para cada estado la posible combinacion de siguientes estados en base a la aprticion a la que irian
	    for(States s:fda){
				//temp = s.getOutputs();
				//sacamos los nombres de los estados a los que va
				String entraCero = s.getOutputs().get(0).getReadsBit()== 0 ? s.getOutputs().get(0).getNextStateName() : s.getOutputs().get(1).getNextStateName();
				String entraUno = s.getOutputs().get(0).getReadsBit() == 1 ? s.getOutputs().get(0).getNextStateName() : s.getOutputs().get(1).getNextStateName();

				int a=0,b=0;
				int i=0;
				//buscamos a que particiones pertenecen los estados a los que va
				for(HashSet<Integer> subc:pOriginal){
				    for(Integer indice : subc){
							if(fda.get(indice).getName().equals(entraCero) )
							    a = i;
							if( fda.get(indice).getName().equals(entraUno))
							    b= i;
				    }
				    i++;
				}
				//solo añadimos el id de la particion si no esta en el conjunto
				Point id = new Point(a,b);
				//System.out.println(id);
				if(!opciones.contains(id))
				    opciones.add(id);
				//preparamos la nueva particion
	    }

	    ArrayList<HashSet<Integer>> lista = new ArrayList<>();
	    for(int i=0;i<opciones.size();i++)
		    lista.add(new HashSet<Integer>());
	    //asignamos los subconjuntos en la nueva particion
	    int k=0;
	    for(States s:fda){

				String entraCero = s.getOutputs().get(0).getReadsBit()== 0 ? s.getOutputs().get(0).getNextStateName() : s.getOutputs().get(1).getNextStateName();
				String entraUno = s.getOutputs().get(0).getReadsBit() == 1 ? s.getOutputs().get(0).getNextStateName() : s.getOutputs().get(1).getNextStateName();
				System.out.println(entraCero+entraUno);
				int a=0,b=0;
				int i=0;
				//buscamos a que particiones pertenecen los estados a los que va
				for(HashSet<Integer> subc:pOriginal){
				    for(Integer indice : subc){
						if(fda.get(indice).getName().equals(entraCero) )
						    a = i;
						if( fda.get(indice).getName().equals(entraUno))
						    b= i;
					  }
				    i++;
				}
				Point id = new Point(a,b);
				//int cual=0;
				//if(!opciones.contains(id)){
		    lista.get(opciones.indexOf(id)).add(k);
				//}
				k++;
	    }
	    //CREAMOS LA NUEVA PARTICION
	    HashSet<HashSet<Integer>> nuevap = new HashSet<>();

	    for(HashSet<Integer> l:lista){
		nuevap.add(l);
	    }

	    particiones.add(nuevap);
	    index++;
	    //paramos cuando el numero de conjuntos en dos particiones sucesivas no sean modificados
	} while (particiones.get(particiones.size() - 1).size() != particiones.get(particiones.size() - 2).size());

			//return particiones.get(particiones.size() - 1);
			HashSet<HashSet<Integer>> ultimo = particiones.get(particiones.size() - 1);

			// creamoslos estados ddel nuevo automata
			ArrayList<States> salida = new ArrayList<>();
			ArrayList<Point> escribe = new ArrayList<>();
			for( HashSet<Integer> conjunto : ultimo){
				System.out.println(conjunto);
			}
			for( HashSet<Integer> conjunto : ultimo){
				String nuevoNombre = "";
				boolean acc = false;
				boolean ent=false;
				int siCero=0, siUno=0;

				//creamos el nuevo nombre concatenando los nombres de los que formal el estado
				for(Integer c:conjunto){
					nuevoNombre+= fda.get(c).getName();
					System.out.println(nuevoNombre);
					acc = fda.get(c).esAceptacion() ? true:acc;
					ent = fda.get(c).esEntrada() ? true:ent;
					siCero = fda.get(c).getOutputs().get(0).getReadsBit()== 0 ? fda.get(c).getOutputs().get(0).getWritesBit() :fda.get(c).getOutputs().get(1).getWritesBit();
					siUno = fda.get(c).getOutputs().get(0).getReadsBit() == 1 ? fda.get(c).getOutputs().get(0).getWritesBit() :fda.get(c).getOutputs().get(1).getWritesBit();

				}
				States	nuevo = new States(nuevoNombre,acc,ent);
				salida.add(nuevo);
				escribe.add(new Point(siCero,siUno));
			}

			//hacemos las transiciones de acuerdo al nuevo esquema
			AutomataT3 Aux = new AutomataT3();
			for(int y=0;y< salida.size();y++){
					Point trans = opciones.get(y);
					System.out.println(trans);
					salida.get(y).crateOuts(salida.get((int)trans.getX()).getName(),0,(int)escribe.get(y).getX());
					salida.get(y).crateOuts(salida.get((int)trans.getY()).getName(),1,(int)escribe.get(y).getY());
					Aux.addUndefinedState(salida.get(y));
			}

			Aux=Aux.renameIt(Aux);

			writeAutomata(Aux.getSatates(),"Minimizado");

			return Aux.getSatates();

    }



}
