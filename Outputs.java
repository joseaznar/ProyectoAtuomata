public class Outputs{
	private String nextStateName;
	private int readsBit;
	private int writesBit;
	
	public Outputs( String s, int r, int w){
		nextStateName=s;
		readsBit=r;
		writesBit=w;
	}
	
	public String getNextStateName(){
		return this.nextStateName;
	}
	
	
}