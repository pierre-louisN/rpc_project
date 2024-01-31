
package rpc;
import java.io.*; 
import java.lang.reflect.Method;
import java.util.Arrays;
import java.lang.reflect.Array;  
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

public class Compilateur{
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
    
    
    public void create_boot(String socket, String interface_name){
        String boot_file =
        "package rpc;\n"+
        "import java.lang.reflect.*;\n"+
        "import java.util.*;\n"+
        "public class "+interface_name+"_boot {\n\t"+

          "public static void main(String [] arg) throws Exception {\n\t\t"+
            interface_name+" m = new "+interface_name+"_impl();\n\t\t"+
            "java.net.ServerSocket sos = new java.net.ServerSocket("+socket+");\n\t\t"+
            "HashMap<String, String> hmap = new HashMap<String, String>() {{\n      // type primitif en java : byte, short, int, long, float, double, boolean and char\n      put(\"byte\",\"Int\");\n      put(\"short\",\"Short\");\n      put(\"long\",\"Long\");\n      put(\"int\",\"Int\");\n      put(\"float\",\"Float\");\n      put(\"double\",\"Double\");\n      put(\"char\",\"Char\");\n      put(\"boolean\",\"Boolean\");\n      // type non primitif : String ou objet\n      put(\"String\",\"UTF\");\n    }};\n\t\t"+
            
            "while(true){\n\t\t\t"+
              "java.net.Socket s = sos.accept();\n\t\t\t"+
              "java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(s.getOutputStream());\n\t\t\t"+
              "oos.flush();\n\t\t\t"+
              "java.io.ObjectInputStream ois = new java.io.ObjectInputStream(s.getInputStream());\n\t\t\t"+
              "String fonction = ois.readUTF();\n\t\t\t"+
              "String line = ois.readUTF();\n\t\t\t"+
              "String[] params = line.split("+"\""+" "+"\""+",0); \n\t\t\t"+ // récupère le type des paramètres de la fonction

              "Class<?> clazz = Class.forName("+"\""+"rpc."+"\""+"+"+"\""+interface_name+"\""+");\n\t\t\t"+
              "Method[] methods  = clazz.getMethods();\n\t\t\t"+
              
              "for (Method method : methods) {\n\t\t\t\t"+
                "if (fonction.equals(method.getName())){\n\t\t\t\t\t"+ // si la fonction a le bon nom 
                "if(method.getParameterCount()==params.length){\n\t\t\t\t\t\t"+ // si elle a le bon nombre de paramètre
                  "Object[] args = new Object[method.getParameterCount()];\n\t\t\t\t\t\t"+
                  "for (int i = 0; i <  method.getParameterCount(); i++) {\n\t\t\t\t\t\t\t"+
                  "if(method.getParameterTypes()[i].getSimpleName().equals(params[i])){\n\t\t\t\t\t\t\t\t"+ // si le paramètre courant a le bon type

                    "Set st = (Set) hmap.entrySet();\n\t\t\t\t\t\t\t\t"+
                    "Iterator it = st.iterator();\n\t\t\t\t\t\t\t\t"+
                    "boolean trouve = false;\n\t\t\t\t\t\t\t\t"+
                    "while(it.hasNext()){\n\t\t\t\t\t\t\t\t\t"+ // itérateur pour lire les données et ne pas avoir à faire de parse
                        "Map.Entry entry = (Map.Entry)it.next();\n\t\t\t\t\t\t\t\t\t"+
                        "if(method.getParameterTypes()[i].getSimpleName().equals(entry.getKey())){\n\t\t\t\t\t\t\t\t\t\t"+ // un type qu'on connaît
                          "Method mthd = ois.getClass().getMethod("+"\""+"read"+"\""+"+"+"entry.getValue()"+");\n\t\t\t\t\t\t\t\t\t\t"+
                          "args[i] = mthd.invoke(ois);\n\t\t\t\t\t\t\t\t\t\t"+  
                          "trouve = true;\n\t\t\t\t\t\t\t\t\t"+
                        "}\n\t\t\t\t\t\t\t\t"+
                    "}\n\t\t\t\t\t\t\t\t"+
                    
                    "if(trouve==false){\n\t\t\t\t\t\t\t\t\t"+ // sinon on lit un objet 
                    "args[i] = ois.readObject();\n\t\t\t\t\t\t\t\t"+
                    "}\n\t\t\t\t\t\t\t"+
                   "}\n\t\t\t\t\t\t"+
                  
                   "}\n\t\t\t\t\t\t"+
                   "Object res = method.invoke(m,args);\n\t\t\t\t\t\t"+ // on invoque la méthode
                   "oos.writeObject(res);\n\t\t\t\t\t"+
                "}\n\t\t\t\t"+
                "}\n\t\t\t"+
              "}\n\t\t"+
            "}\n\t"+
          "}\n"+
        "}";
        try {
            FileWriter myWriter = new FileWriter(interface_name+"_boot.java");
            myWriter.write(boot_file);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
        
    }

    public void create_stub(String add_IP, String socket, String Interface_name) throws ClassNotFoundException, InstantiationException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        String stub_file  =
            "package rpc;\n"+
            "import java.io.*;\n"+
            "import java.net.*;\n"+
            "public class "+Interface_name+"_stub implements "+Interface_name+" {\n\t";

    
            // pas possible de créer une instance ici car on a pas accées à l'implémentation, récupérer les méthodes seulement via l'interface 
            Class<?> clazz = Class.forName("rpc."+Interface_name);
            Method[] methods  = clazz.getMethods();

            for (int i = 0; i < methods.length; i++){
              // si on a deux fonction qui ont le même nom et le même nombre de paramètre : IllegalArgumentException et readObject infini donc pbm 
              // donc le stub va envoyer la liste de ses paramètres en UTF (split côté boot pour les récupérer)
                  stub_file += "public "+ methods[i].getReturnType().getName()+" "+methods[i].getName()+" (";
                  String line = "\"";
                  for (int y = 0; y <  methods[i].getParameterCount(); y++) {
                    if(y ==  methods[i].getParameterCount()-1){ // si on est au dernier paramètre alors on ne mets pas de virgule après ni d'espace dans la liste des types
                      line +=methods[i].getParameterTypes()[y].getSimpleName()+"\""; 
                      stub_file += (methods[i].getParameterTypes()[y].getSimpleName()) +" param"+y;
                    }
                    else{
                      line += methods[i].getParameterTypes()[y].getSimpleName()+" "; // l'espace va permettre au boot de split 
                      stub_file += (methods[i].getParameterTypes()[y].getSimpleName()) +" param"+y+", ";
                    }   
                  }
                  
                  stub_file += ") throws Exception {\n\t\t"+
                  "String line = "+line+";\n\t\t"+
                  "java.net.Socket s = new java.net.Socket("+"\""+add_IP+"\""+", "+socket+");\n\t\t"+
                  "java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(s.getOutputStream());\n\t\t"+
                  
                  "oos.writeUTF("+"\""+methods[i].getName()+"\""+");\n\t\t"+
                  "oos.writeUTF(line);\n\t\t";
                  for (int y = 0; y <  methods[i].getParameterCount(); y++) {
                    Set st = (Set) this.hmap.entrySet();
                    Iterator it = st.iterator();
                    boolean trouve = false;
                    while(it.hasNext()){
                        Map.Entry entry = (Map.Entry)it.next();
                        if(methods[i].getParameterTypes()[y].getSimpleName().equals(entry.getKey())){ // c'est un type connu
                          stub_file +=  "oos.write"+entry.getValue()+"("+"param"+y+");\n\t\t";
                          trouve = true;
                        }
                    }
                    if(trouve==false){
                      stub_file +=  "oos.writeObject("+"param"+y+");\n\t\t";

                    }
                  }
                  stub_file += "oos.flush();\n\t\t"+
                  "java.io.ObjectInputStream ois = new java.io.ObjectInputStream(s.getInputStream());\n\t\t";
                  
                  stub_file +="return(("+methods[i].getReturnType().getName()+")ois.readObject());\n\t"+
                  "}\n";
                  if(i ==  methods.length-1){
                    stub_file += "}";
                  }else{
                    stub_file += "\t";
                  }
                }

            try {
                FileWriter myWriter = new FileWriter(Interface_name+"_stub.java");
                myWriter.write(stub_file);
                myWriter.close();
                System.out.println("Successfully wrote to the file.");
              } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
              }
    }
      
    public static void main(String [] args) throws Exception {

    System.out.println("Debut main Compilateur");

      if(args.length < 3)
    {
        System.out.println("Pas assez d'arguments, réésayer : @IP n°socket nom_interface");
        System.exit(0);
    }
      Compilateur c = new Compilateur();
      
      c.create_stub(args[0], args[1], args[2]);
      c.create_boot(args[1], args[2]);
      
      System.out.println("Fin main Compilateur");

    }
}