package rpc;
import java.io.*;
import java.net.*;
public class Guitare_stub implements Guitare {
	public rpc.Morceau play (String param0) throws Exception {
		String line = "String";
		java.net.Socket s = new java.net.Socket("localhost", 1234);
		java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(s.getOutputStream());
		oos.writeUTF("play");
		oos.writeUTF(line);
		oos.writeUTF(param0);
		oos.flush();
		java.io.ObjectInputStream ois = new java.io.ObjectInputStream(s.getInputStream());
		return((rpc.Morceau)ois.readObject());
	}
}