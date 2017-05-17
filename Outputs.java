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

	public int getReadsBit(){
		return this.readsBit;
	}

	public int getWritesBit(){
			return this.writesBit;
		}
	public String getInfo(){
		return " en :"+readsBit+" va a: "+nextStateName+" y escribe: "+writesBit;
	}

}
