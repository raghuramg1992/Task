package program;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GetNames {

	public static <T> Stream<List<T>> batches(List<T> source, int length) {
		if (length <= 0)
			throw new IllegalArgumentException("length = " + length);
		int size = source.size();
		if (size <= 0)
			return Stream.empty();
		int fullChunks = (size - 1) / length;
		return IntStream.range(0, fullChunks + 1).mapToObj(
				n -> source.subList(n * length, n == fullChunks ? size : (n + 1) * length));

	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> list=new ArrayList<String>();
	
		List<List<String>> output=new ArrayList<List<String>>();
		Connection con;
		Statement st;
		ResultSet rs;
		try{
			//database connection
			con = DriverManager.getConnection("jdbc:mysql://localhost/telusko","root","raghu");
			st = con.createStatement();
			rs = st.executeQuery("select * from names");
			while(rs.next()){
				list.add(rs.getString(2));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		System.out.println("By 2:");
		batches(list, 2).forEach(System.out::println);

		output = batches(list, 2).collect(Collectors.toList());

		Collections.reverse(output);
		System.out.println(output);

		try {

			for (int i = 0; i < output.size(); i++) {
				File file = new File("F:\\raghu\\output\\file_"+ i + ".txt");
				if (file.exists()) {
					file.createNewFile();
				}
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(output.get(i).toString());
				bw.flush();
				bw.close();
			}
//			ZipUtil.pack(new File("F:\\raghu\\output\\"), new File("F:\\raghu\\output.zip"));


			//	    	FileOutputStream fos = new FileOutputStream("F:\\raghu\\output\\file" + output.get(i) + ".txt"); 
			//	    	ObjectOutputStream oos = new ObjectOutputStream(fos) ;
			//	    	oos.writeObject(output.get(i)) ;
			//	    	oos.close() ;
		} catch (IOException e) {
			System.out.println("Unable to write out names");
		}
		
		
	}




}
