import javafx.util.Pair;

import java.io.*;
import java.util.*;

public class Main
{
    public static InputReader in = new InputReader(System.in);
    public static PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
    public static void main(String[] args)
    {
        int n,m,q,u,v,q1,q2,q3;
        String flag;
        while(in.hasNext())
        {
            n=in.nextInt();
            m=in.nextInt();
            q=in.nextInt();
            tree_split ts=new tree_split(n+2,2*n+2);
            for(int i=1;i<=n;++i)
                ts.add_weight(i,in.nextInt());
            for(int i=0;i<m;++i)
            {
                u=in.nextInt();
                v=in.nextInt();
                ts.add_edge(u,v);
                ts.add_edge(v,u);
            }
            ts.init(1);
            tree_array1 query=new tree_array1(n);
            for(int i=1;i<=n;++i)
                query.a[i]=ts.new_weight[i];
            query.init();
            for(int i=0;i<q;++i)
            {
                flag=in.next();
                if(flag.equals("I"))
                {
                    q1=in.nextInt();
                    q2=in.nextInt();
                    q3=in.nextInt();
                    ts.query(query,q1,q2,q3);
                }
                else if(flag.equals("Q"))
                {
                    q1=in.nextInt();
                    out.println(query.range_ask(ts.id[q1],ts.id[q1]));
                }
                else
                {
                    q1=in.nextInt();
                    q2=in.nextInt();
                    q3=in.nextInt();
                    ts.query(query,q1,q2,-q3);
                }
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

class tree_split
{
    graph p;
    int dep[],fa[],top[],size1[],son[],id[],weight[],new_weight[];
    int len;
    void query(tree_array1 p,int x,int y,int k) //区间加值 query具体看题目写
    {
        while(top[x]!=top[y])
        {
            if(dep[top[x]]<dep[top[y]]){ int mid=x;x=y;y=mid;}
            p.range_add(id[top[x]],id[x],k);
            x=fa[top[x]];
        }
        if(dep[x]>dep[y]){ int mid=x;x=y;y=mid;}
        p.range_add(id[x],id[y],k);
    }
    tree_split(int n,int m)
    {
        dep=new int[n+3];
        fa=new int[n+3];
        top=new int[n+3];
        size1=new int[n+3];
        son=new int[n+3];
        id=new int[n+3];
        weight=new int[n+3];
        new_weight=new int[n+3];
        Arrays.fill(fa,0);
        Arrays.fill(top,0);
        Arrays.fill(size1,0);
        Arrays.fill(son,0);
        p=new graph(m);
        len=0;
    }
    void init(int root)
    {
        dep[root]=0;
        dfs1(root,0);
        dfs2(root,0);
    }
    void add_weight(int i,int w)
    {
        weight[i]=w;
    }
    void add_edge(int u,int v)
    {
        p.add(u,v);
    }
    void dfs0(int u) //单纯用来求LCA的dfs
    {
        int maxx=0,son=0;
        top[u]=u;
        size1[u]=1;
        for(int i=p.Begin[u];i!=-1;i=p.Next[i])
        {
            if(p.to[i]==fa[u])
                continue;
            fa[p.to[i]]=u;
            dep[p.to[i]]=dep[u]+1;
            dfs0(p.to[i]);
            size1[u]+=size1[p.to[i]];
            if(size1[p.to[i]]>maxx)
            {
                son=p.to[i];
                maxx = size1[son];
            }
        }
        if(son!=0)
            top[son]=u;
    }
    void dfs1(int u,int father)//一和二是完整树链剖分的预处理
    {
        dep[u]=dep[father]+1;
        fa[u]=father;
        size1[u]=1;
        for(int i=p.Begin[u];i!=-1;i=p.Next[i])
        {
            if(p.to[i]!=father)
            {
                dfs1(p.to[i],u);
                size1[u]+=size1[p.to[i]];
                if(size1[p.to[i]]>size1[son[u]])
                    son[u]=p.to[i];
            }
        }
    }
    void dfs2(int u,int topf)
    {
        id[u]=++len;
        new_weight[len]=weight[u];
        top[u]=topf;
        if(son[u]==0)return;
        dfs2(son[u],topf);
        for(int i=p.Begin[u];i!=-1;i=p.Next[i])
        {
            if(p.to[i]==fa[u] || p.to[i]==son[u]) continue;
            dfs2(p.to[i],p.to[i]);
        }
    }
    int find(int u)
    {
        return top[u] = top[u] == u ? u : find(top[u]);
    }
    int LCA(int u, int v) {
        if (find(u) != find(v))
            return dep[top[u]] > dep[top[v]] ? LCA(fa[top[u]], v) : LCA(u, fa[top[v]]);
        else return dep[u] > dep[v] ? v : u;
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
    public boolean hasNext() {  //处理EOF
        while (tokenizer == null || !tokenizer.hasMoreTokens()) {
            try {
                String line = in.readLine();
                if(line == null) return false;
                tokenizer = new StringTokenizer(line);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }
    public int nextInt() {
        return Integer.parseInt(next());
    }
    public long nextLong()
    {
        return Long.parseLong(next());
    }
}