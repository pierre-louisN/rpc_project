package rpc;
import java.io.*;
import java.net.*;
public class Matlab_stub implements Matlab {
	public rpc.Result calcul (int param0) throws Exception {
		String line = "int";
		java.net.Socket s = new java.net.Socket("localhost", 1234);
		java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(s.getOutputStream());
		oos.writeUTF("calcul");
		oos.writeUTF(line);
		oos.writeInt(param0);
		oos.flush();
		java.io.ObjectInputStream ois = new java.io.ObjectInputStream(s.getInputStream());
		return((rpc.Result)ois.readObject());
	}
	public rpc.Result calcul (Result param0, int param1) throws Exception {
		String line = "Result int";
		java.net.Socket s = new java.net.Socket("localhost", 1234);
		java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(s.getOutputStream());
		oos.writeUTF("calcul");
		oos.writeUTF(line);
		oos.writeObject(param0);
		oos.writeInt(param1);
		oos.flush();
		java.io.ObjectInputStream ois = new java.io.ObjectInputStream(s.getInputStream());
		return((rpc.Result)ois.readObject());
	}
	public int calcul2 (Result param0) throws Exception {
		String line = "Result";
		java.net.Socket s = new java.net.Socket("localhost", 1234);
		java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(s.getOutputStream());
		oos.writeUTF("calcul2");
		oos.writeUTF(line);
		oos.writeObject(param0);
		oos.flush();
		java.io.ObjectInputStream ois = new java.io.ObjectInputStream(s.getInputStream());
		return((int)ois.readObject());
	}
}