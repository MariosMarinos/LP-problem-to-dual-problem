import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Main{

	public static void main(String[] args) {
		ArrayList<String> c = new ArrayList<String>();
		ArrayList<String> b = new ArrayList<String>();
		ArrayList<Integer> Eqin = new ArrayList<Integer>();
		HashMap<Integer, ArrayList<String>> hmap = new HashMap<Integer, ArrayList<String>>();
		int MinMax = 0;
		String s_t = "";
		try {
			int i;
			File f = new File ("LP-1.txt");
			FileReader freader = new FileReader("LP-1.txt");
			BufferedReader breader = new BufferedReader(freader);
			String line = breader.readLine();
			
			if (line.length()<3) {
				System.out.println("Your file has nothing.");
				System.exit(1);
			}
			line = line.replaceAll("\\s", "");
			line.replaceAll("\\s", "");
			String maxmin = (line.substring(0,3));
			char charr = line.charAt(3);
			if (maxmin.toLowerCase().equals("max") && ( charr == '-' || charr=='x' || !Character.isDigit(charr) ) )
				MinMax = 1 ;
			else if (maxmin.toLowerCase().equals("min")) 
				MinMax = -1 ;
			else {
				System.out.println("There is an error on what you want to do(optimization/minimazation) put min/max in your file.");
				System.exit(1);
			}
			line = line.substring(3);
			ArrayList<Integer> index = new ArrayList<Integer>();
			ArrayList<String> temp = new ArrayList<String>();
			while (!(line.isEmpty()) && !((line.matches("[0-9]+") && line.length() ==1))) {
				line = Help.diavasma_suntelewstwn(line, temp,index);
			}
			int max= 0;
			for (i=0;i<index.size();i++) {
				int deikths =index.get(i);
				if (deikths > max)
					max = index.get(i);
			}
			for (i =0;i<max;i++) {
				c.add("0");
			}
			for (i=0;i<index.size();i++) {
				int variable = index.get(i);
				c.set(variable-1, temp.get(i));
			}
			for (i=0;i<index.size();i++)
				if (Collections.frequency(index,index.get(i))>1) {
					System.out.println("You can't put twice a variable in your function.");
					System.exit(1);
				}
			line = breader.readLine();
			line = line.replaceAll("\\s", "");
			s_t = line.substring(0,4);
			if (!s_t.equals("s.t.")) {
				System.out.println("There is an error. You haven't define your restrictinons.");
				System.exit(1);
			}
			line = line.substring(4);
			int j=0,periorismoi = 0;
			String line1=null, line2=null;
			while (line!=null) {
				temp = new ArrayList<String>();
				periorismoi++;
				String [] split_lines = null;
				if (line.contains(">=")) {
					split_lines = line.split(">=");
					Eqin.add(1);
				}
				else if  (line.contains("<=")) {
					split_lines = line.split("<=");
					Eqin.add(-1);
				}
				else if (line.contains("=")) {
					split_lines = line.split("=");
					Eqin.add(0);
				}
				else {
					System.out.println("There is an error. You haven't put <= or >= or = in your restrictions.");
					System.exit(1);
				}
				int len = line.length();
				if (!Character.isDigit(line.charAt(len-1))) {
					System.out.println("There is an error. You haven't put a number after >= ,<= , = in your restriction.");
					System.exit(1);
				}
				else {
					line1 = split_lines[0];
					line2 = split_lines[1];
					b.add(line2);
				}
				index = new ArrayList<Integer>();
				while (!(line1.isEmpty()) && !((line1.matches("[0-9]+") && line1.length() ==1))) { 
					line1 = Help.diavasma_suntelewstwn(line1, temp,index);
				}
				for (i=0;i<index.size();i++)
					if (Collections.frequency(index,index.get(i))>1) {
						System.out.println("You can't put twice a variable in your restrictions.");
						System.exit(1);
					}
				ArrayList<String> A = new ArrayList<String>();
				max= 0;
				for (i=0;i<index.size();i++) {
					int deikths =index.get(i);
					if (deikths > max)
						max = index.get(i);
				}
				for (i =0;i<max;i++) {
					A.add("0");
				}
				for (i=0;i<index.size();i++) {
					int variable = index.get(i);
					A.set(variable-1, temp.get(i));
				}
				hmap.put(periorismoi, A);
				line = breader.readLine();
				if (line!=null)
					line = line.replaceAll("\\s", "");
			}
			max=0;
			for (i=1;i<=hmap.size();i++) {
				ArrayList<String> restrict_size = hmap.get(i);
				if (restrict_size.size()>max)
					max = restrict_size.size();
			}
			while (max>c.size())
				c.add("0");
			max=0;
				if (c.size()>max)
					max = c.size();
			for (i=1;i<=hmap.size();i++) {
				temp = hmap.get(i);
				while (max>temp.size())
					temp.add("0");
			}
			
			breader.close();
			freader.close();
//write the LP-2 file.
			double helpTemp;
			boolean flag = true;
			FileWriter writer = new FileWriter("LP-2.txt");
			writer.write("Dual Problem :\r\n");
			
			//invert 2d array  .
			String [][] A = new String[c.size()][b.size()];
			for (i=0;i<b.size();i++) {
				ArrayList<String> temp_ = hmap.get(i+1);
				for (j=0;j<c.size();j++) {
					A[j][i]=temp_.get(j);
				}
			}
			while (flag) {
				if (MinMax==1){ // evresi duikou provlimatos an einai max.
					writer.write("min  C : [");
					for (int k=0;k<b.size();k++) {
						helpTemp=Help.convertFraction(b.get(k));
						if (k==0)
							writer.write(""+b.get(k));
						else
							writer.write("," + b.get(k)); 
				}
					writer.write("]");
					writer.write("\r\n");
					
					writer.write("s.t. Matrix A : \r\n");
					for (i=0 ;i<=A.length-1;i++){
						writer.write("[");
						for (j=0;j<=A[i].length-1;j++) {
							if (j==0)
								writer.write(" "+A[i][j]);
							else
								writer.write("," + A[i][j]);
						}
						writer.write("]\r\n");
					}
					writer.write("b : [");
					for (int k=0;k<c.size();k++) {
						helpTemp=Help.convertFraction(c.get(k));
						if (k==0)
							writer.write(""+c.get(k));
						else
							writer.write("," + c.get(k)); 
					}
					writer.write(" ]");
					writer.write("\r\n");
					writer.write("Eqin : [");
					for (i=0;i<c.size();i++) {
						if (i==0)
							writer.write( "1" );
						else 
							writer.write(",1" );
					}
					writer.write("]");
					writer.write("\r\n");
					for (i=0;i<b.size();i++) {
						if (Eqin.get(i)==1)
							writer.write("w"+ (i+1)+ "<=0");
						else if(Eqin.get(i)==-1)
							writer.write("w"+ (i+1)+ ">=0");
						else 
							writer.write("w"+ (i+1)+ " free");
						if (i!=b.size()-1)
							writer.write(",");
					}
					
				
				}
				else { // evresi duikou provlimatos an einai min.
					writer.write("max  C : [");
					for (int k=0;k<b.size();k++) {
						helpTemp=Help.convertFraction(b.get(k));
						if (k==0)
							writer.write(""+b.get(k));
						else
							writer.write("," + b.get(k)); 
				}
					writer.write("]");
					writer.write("\r\n");
					
					writer.write("s.t. Matrix A : \r\n");
					for (i=0 ;i<=A.length-1;i++){
						writer.write("[");
						for (j=0;j<=A[i].length-1;j++) {
							if (j==0)
								writer.write(" "+A[i][j]);
							else
								writer.write("," + A[i][j]);
						}
						writer.write("]\r\n");
					}							
					writer.write("b : [");
					for (int k=0;k<c.size();k++) {
						helpTemp=Help.convertFraction(c.get(k));
						if (k==0)
							writer.write(""+c.get(k));
						else
							writer.write("," + c.get(k)); 
					}
					writer.write(" ]");
					writer.write("\r\n");
					writer.write("Eqin : [");
					for (i=0;i<c.size();i++) {
						if (i==0)
							writer.write( "-1" );
						else 
							writer.write(",-1" );
					}
					writer.write("]\r\n");
					for (i=0;i<b.size();i++) {
						if (Eqin.get(i)==1)
							writer.write("w"+ (i+1)+ ">=0");
						else if(Eqin.get(i)==-1)
							writer.write("w"+ (i+1)+ "<=0");
						else 
							writer.write("w"+ (i+1)+ " free");
						if (i!=b.size()-1)
							writer.write(",");
					}
				}
				flag = false;
			}
			writer.close();
		}
		 catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
