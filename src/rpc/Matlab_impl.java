package rpc;
public class Matlab_impl implements Matlab  {
  public Result calcul(int in) {
    Result res = new Result();
    res.result = in;
    return res;
  }
  public int calcul2(Result r) {
    return r.result;
  }
  public Result calcul(Result r, int in) {
    Result r2 = new Result();
    r2.result = r.result + in;
    return r2;
  }
}