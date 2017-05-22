import java.io.*;
import java.awt.*;
import java.util.*;

public class main {

   public static void main(String []args){

		 AutomataT3 aut= new AutomataT3();
     String file=("test.txt");
     aut.setAutomat(file);

     aut = aut.GetDeterministicAut();
System.out.println("//////////////////////////////////////////");
     for(States s: aut.minimiza(aut.getSatates())){
       s.getInfo();
     }


   }

}
