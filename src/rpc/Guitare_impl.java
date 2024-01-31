package rpc;
public class Guitare_impl implements Guitare {
  public Morceau play(String nom) throws Exception{
    Morceau m = new Morceau();
    m.morceau = nom; 
    return m;
  }
}
