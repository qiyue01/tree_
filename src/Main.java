import javafx.util.Pair;

import java.io.*;
import java.util.*;

public class Main
{
    public static InputReader in = new InputReader(System.in);
    public static PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
    public static void main(String[] args)
    {
        int n,m,r,u,v,query;
        n=in.nextInt();
        m=in.nextInt();
        r=in.nextInt();
        dfs_n part2=new dfs_n(n);
        int part[]=new int[n];
        for(int i=0;i<n;++i)
            part[i]=in.nextInt();
        for(int i=0;i<n-1;++i)
        {
            u=in.nextInt();
            v=in.nextInt();
            part2.p.add(u,v);
            part2.p.add(v,u);
        }
        part2.dfs(r,r);
        tree_array1 query1=new tree_array1(n);
        for(int i=0;i<n;++i)
            query1.a[i+1]=part[part2.dfn[i+1]-1];
        query1.init();
        int a,x;
        for(int i=0;i<m;++i)
        {
            query=in.nextInt();
            if(query==1)
            {
                a=in.nextInt();
                x=in.nextInt();
                query1.range_add(part2.dfs_begin[part2.pos[a]],part2.dfs_end[part2.pos[a]],x);
            }
            else
            {
                a=in.nextInt();
                out.println(query1.range_ask(part2.dfs_begin[part2.pos[a]],part2.dfs_end[part2.pos[a]]));
            }
        }
        out.flush();
        out.close();
    }

}
class graph_list
{
    LinkedList<Integer> edge[];
    void add(int u,int v)
    {
        edge[u].add(v);
    }
    graph_list(int n)
    {
        edge=new LinkedList[n];
        for(int i=0;i<n;++i)
            edge[i]=new LinkedList<>();
    }
}
class graph //前向星
{
    int Begin[], to[], Next[], e;
    void add(int u, int v)
    {
        to[++e] = v;
        Next[e] = Begin[u];
        Begin[u] = e;
    }
    graph(int a)
    {
        e=0;
        to=new int[a];
        Next=new int[a];
        Begin=new int[a];
        Arrays.fill(Begin,-1);
    }
}
class dfs_n // dfs序
{
    graph_list p;
    int dfn[],dfs_begin[],dfs_end[],time,len,pos[];
    dfs_n(int n)
    {
        p=new graph_list(2*n+20);
        dfn=new int[5+n];
        dfs_end=new int[5+n];
        dfs_begin=new int[5+n];
        pos=new int[5+n];
        len=0;
        time=0;
    }
    void dfs(int u,int fa)
    {
        int x=len+1;
        dfs_begin[++len]=++time;
        dfn[len]=u;
        pos[u]=len;
        for(int i=0;i<p.edge[u].size();++i)
            if(p.edge[u].get(i)!=fa)
                dfs(p.edge[u].get(i),u);
        dfs_end[x]=time;
    }
}
class tree_array1  //区间修改 + 区间和
{
    long sum1[],sum2[];
    long a[]; //原序列
    int n;
    tree_array1(int n1)
    {
        n=n1;
        sum1=new long[n+1];
        a=new long[n+1];
        sum2=new long[n+1];
    }
    void init()
    {
        for(int i=1;i<=n;++i)
            a[i]+=a[i-1];
        Arrays.fill(sum1,0);
        Arrays.fill(sum2,0);
    }
    void add(int p, long x){
        for(int i = p; i <= n; i += i & -i)
        {
            sum1[i] += x;
            sum2[i] += x * p;
        }
    }
    void range_add(int l, int r, long x){
        add(l, x);
        add(r + 1, -x);
    }
    long ask(int p){
        long res = 0;
        for(int i = p; i!=0; i -= i & -i)
            res += (p + 1) * sum1[i] - sum2[i];
        return res;
    }
    long range_ask(int l, int r){
        return ask(r) - ask(l - 1)+a[r]-a[l-1];
    }
}
class InputReader{
    private final static int BUF_SZ = 65536;
    BufferedReader in;
    StringTokenizer tokenizer;
    public InputReader(InputStream in) {
        super();
        this.in = new BufferedReader(new InputStreamReader(in),BUF_SZ);
        tokenizer = new StringTokenizer("");
    }
    public String next() {
        while (!tokenizer.hasMoreTokens()) {
            try {
                tokenizer = new StringTokenizer(in.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return tokenizer.nextToken();
    }
    public int nextInt() {
        return Integer.parseInt(next());
    }
    public long nextLong()
    {
        return Long.parseLong(next());
    }
}

