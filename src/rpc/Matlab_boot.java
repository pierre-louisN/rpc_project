package rpc;
import java.lang.reflect.*;
import java.util.*;
public class Matlab_boot {
	public static void main(String [] arg) throws Exception {
		Matlab m = new Matlab_impl();
		java.net.ServerSocket sos = new java.net.ServerSocket(1234);
		HashMap<String, String> hmap = new HashMap<String, String>() {{
      // type primitif en java : byte, short, int, long, float, double, boolean and char
      put("byte","Int");
      put("short","Short");
      put("long","Long");
      put("int","Int");
      put("float","Float");
      put("double","Double");
      put("char","Char");
      put("boolean","Boolean");
      // type non primitif : String ou objet
      put("String","UTF");
    }};
		while(true){
			java.net.Socket s = sos.accept();
			java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(s.getOutputStream());
			oos.flush();
			java.io.ObjectInputStream ois = new java.io.ObjectInputStream(s.getInputStream());
			String fonction = ois.readUTF();
			String line = ois.readUTF();
			String[] params = line.split(" ",0); 
			Class<?> clazz = Class.forName("rpc."+"Matlab");
			Method[] methods  = clazz.getMethods();
			for (Method method : methods) {
				if (fonction.equals(method.getName())){
					if(method.getParameterCount()==params.length){
						Object[] args = new Object[method.getParameterCount()];
						for (int i = 0; i <  method.getParameterCount(); i++) {
							if(method.getParameterTypes()[i].getSimpleName().equals(params[i])){
								Set st = (Set) hmap.entrySet();
								Iterator it = st.iterator();
								boolean trouve = false;
								while(it.hasNext()){
									Map.Entry entry = (Map.Entry)it.next();
									if(method.getParameterTypes()[i].getSimpleName().equals(entry.getKey())){
										Method mthd = ois.getClass().getMethod("read"+entry.getValue());
										args[i] = mthd.invoke(ois);
										trouve = true;
									}
								}
								if(trouve==false){
									args[i] = ois.readObject();
								}
							}
						}
						Object res = method.invoke(m,args);
						oos.writeObject(res);
					}
				}
			}
		}
	}
}