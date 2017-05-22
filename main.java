import java.io.*;

public class main {

   public static void main(String []args){

		 AutomataT3 aut= new AutomataT3();
     String file=("test.txt");
     aut.setAutomat(file);

     aut.GetDeterministicAut();


   }

}
