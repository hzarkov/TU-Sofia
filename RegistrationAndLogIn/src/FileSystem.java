import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileSystem {

	private static final File usersFile = new File("DB/users.bin");
	private static final File usersLogFile = new File("Log/users.log");
	public static List<User> LoadUsers(){
		if(!usersFile.exists()){
			return new ArrayList<User>();
		}
		List<User> users = new ArrayList<User>();
		ObjectInputStream ois = null;
		try {
			FileInputStream fin = new FileInputStream(usersFile);
			ois = new ObjectInputStream(fin);
			User readUser;
			while(true){
				try {
					readUser = (User)ois.readObject();
					users.add(readUser);
				} catch (EOFException e) {
					break;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (EOFException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				ois.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return users;
	}
	
	public static void SafeUsers(List<User> users){
		ObjectOutputStream oos = null;
		try {
			FileOutputStream fout = new FileOutputStream(usersFile);
			oos = new ObjectOutputStream(fout);
			for (User user : users) {
				oos.writeObject(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static boolean LogIn(User user){
		ObjectInputStream ois = null;
		try {
			FileInputStream fin = new FileInputStream(usersFile);
			ois = new ObjectInputStream(fin);
			User readUser;
			while(true){
				try {
					readUser = (User)ois.readObject();
					if(readUser.equals(user)){
						Date date = new Date();
						PrintWriter dateFin = new PrintWriter(usersLogFile);
						dateFin.println(user+"\t"+date);
						dateFin.close(); //where to close?
						return true;
					}
				} catch (EOFException e) {
					break;
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (EOFException e) {
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				ois.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
}
