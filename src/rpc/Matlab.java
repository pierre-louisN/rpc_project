package rpc;
public interface Matlab {
  public Result calcul(int i) throws Exception;
  public int calcul2(Result r) throws Exception;
  public Result calcul(Result r, int in) throws Exception;
}
    