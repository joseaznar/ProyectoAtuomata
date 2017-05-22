import java.util.*;

public class States{
	private ArrayList<Outputs> outs;
	private String name;
	private boolean entrada;
	private boolean aceptacion;


	public States(String n){
		this.name=n;
		this.outs=new ArrayList<Outputs>();
		this.entrada=false;
		this.aceptacion=false;
	}

	public States(String n,boolean acep, boolean ent){
		this.name=n;
		this.outs=new ArrayList<Outputs>();
		this.entrada=ent;
		this.aceptacion=acep;
	}

	public void crateOuts(String name ,int reads ,int writes){

		outs.add( new Outputs(name,reads,writes) );
	}

	public void crateOut(String name ,int reads ,int writes, boolean entrada, boolean aceptacion){
		this.entrada=entrada;
		this.aceptacion=aceptacion;
		outs.add( new Outputs(name,reads,writes) );
	}

	public void getInfo(){
		for (Outputs o : outs) {
				System.out.println("Estado: "+name+ o.getInfo()+
				((aceptacion) ? "-- soy estado de aceptacion" : "// No soy estado de aceptacion") +
				((entrada) ? "-- soy entrada" : "// No soy entrada"));
		}


	}
	public boolean esEntrada(){
		return entrada;
	}
	public boolean esAceptacion(){
		return aceptacion;
	}
	public String getName(){
		return name;
	}

	public void soyEntrada(String s){
		int i=0;
		while(i<name.length() && s.indexOf(name.charAt(i))<0){
			i++;
		}
		if(i<name.length()){
			entrada=true;
		}
	}
	public void soyAceptacion(String s){
		int i=0;
		while(i<name.length() && s.indexOf(name.charAt(i))<0){
			i++;
		}
		if(i<name.length()){
			aceptacion=true;
		}
	}
	public String writeOutputs(){
		String tab="	";
		String reads0="";
		String reads1="";
		for(Outputs o: outs){
			if(o.getReadsBit()==0){
				reads0=o.getNextStateName()+tab+o.getWritesBit();
			}
			if(o.getReadsBit()==1){
				reads1=o.getNextStateName()+tab+o.getWritesBit();
			}
		}
		return reads0+tab+reads1;
	}
	public ArrayList<Outputs> getOutputs(){
		return outs;
	}

}
