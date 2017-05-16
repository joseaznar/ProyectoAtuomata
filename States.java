public class States{
	private Outputs[] outs;
	private String name;
	
	public States(){
		this.outs=new Outputs[2];
	}
	
	public void crateState(String name ,int reads ,int writes){
		Outputs[] aux = new Outputs[ outs.length+1];
		outs=aux;
		outs[outs.length-1]=new Outputs(name,reads,writes);
	}
	
}