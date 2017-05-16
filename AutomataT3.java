import java.io.*;

public class AutomataT3{
	private States [] estados;
	public AutomataT3(){
		setAutomat();
	}

	public void setAutomat(){
		try {
			FileReader fileReader = new FileReader("test.txt");
		 BufferedReader br = new BufferedReader(fileReader);
			String line = br.readLine();
			String [] data;
			while (line != null) {
				data= line.split("	");
				for (int i=0; i<data.length ; i++) {
						System.out.println("elemento "+i+": "+data[i]);
				}

				line = br.readLine();
			}

			br.close();
		}catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + "'");
        }catch(IOException ex) {

             ex.printStackTrace();
        }
	}
}
