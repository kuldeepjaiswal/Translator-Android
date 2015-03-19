package my.club;

public class Tree {
	
	public Tree t1;
    public String s;
    public Tree t2;
    public Tree(Tree a,String b,Tree c)
    {
        t1=a;
        s=b;
        t2=c;
    }
    public Tree(Tree a,String b)
    {
        t1=a;
        s=b;
        t2=null;
    }
    public Tree(String b)
    {
        t1=null;
        s=b;
        t2=null;
    }
    public Tree getLeft(Tree t)
    {
        return t.t1;
    }
    public Tree getRight(Tree t)
    {
        return t.t2;
    }
    public void shwap(Tree t)
    {
        Tree temp;
        temp=t.t1;
        t.t1=t.t2;
        t.t2=temp;
    }

}
