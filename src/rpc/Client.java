package rpc;
public class Client {
  public static void main(String [] arg) throws Exception {

    
    // Matlab m = new Matlab_stub();
    // Result res = m.calcul(3);
    // System.out.println("-> " + res.result);

    // int in =  m.calcul2(res);
    // System.out.println("-> " + in);

    // Result res2 = m.calcul(res, 3);
    // System.out.println("-> " + res2.result);


    Guitare g = new Guitare_stub();
    Morceau mrc = g.play("Coucou");
    System.out.println("-> " + mrc.morceau);

  }
}
 
