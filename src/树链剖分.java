import javafx.util.Pair;

import java.util.Arrays;
import java.util.Comparator;

public class 树链剖分
{
    class graph //前向星
    {
        int Begin[], to[], Next[], e;
        void Add(int u, int v)
        {
            System.out.println(u+" "+v);
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
        int dep[],fa[],top[],size1[];
        int time,dfs_begin[],dfs_end[],dfsn[],len;
        tree_split(int n,int m)
        {
            dep=new int[n];
            fa=new int[n];
            top=new int[n];
            size1=new int[n];
            p=new graph(m);
            time=0;
            len=0;
            dfs_begin=new int[n];
            dfsn=new int[n];
            dfs_end=new int[n];
        }
        void dfs(int u)
        {
            int maxx=0,son=0,x=len+1;
            dfs_begin[++len]=++time;
            dfsn[len]=u;
            top[u]=u;
            size1[u]=1;
            for(int i=p.Begin[u];i!=-1;i=p.Next[i])
            {
                if(p.to[i]==fa[u])
                    continue;
                fa[p.to[i]]=u;
                dep[p.to[i]]=dep[u]+1;
                dfs(p.to[i]);
                size1[u]+=size1[p.to[i]];
                if(size1[p.to[i]]>maxx)
                {
                    son=p.to[i];
                    maxx = size1[son];
                }
            }
            if(son!=0)
                top[son]=u;
            dfs_end[x]=time;
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
    class virtual_tree
    {
        graph vir;
        tree_split p;
        int st[],tail,cur;
        int a[],b[],step[];
        Integer part[];
        virtual_tree(int n,int m) //输入实树
        {
            p=new tree_split(n,m);
            st=new int[m];
            part=new Integer[n];
            cur=0;
        }
        void init(int root)
        {
            p.dfs(root);
        }
        void add(int x)
        {
            part[cur++]=x;
        }
        int dp(int x)
        {
            return 1;
        /*int ans=0;
        for(int i=vir.Begin[x];i!=-1;i=vir.Next[i])
        {
            System.out.println(vir.to[i]);
            dp(vir.to[i]);
            a[x]+=a[i];
            b[x]+=b[i];
        }
        ans=a[x]*b[x]*step[x];
        return ans;
        */
        }
        void insert(int x)
        {
            if(tail==1)
            {
                st[++tail]=x;
                return;
            }
            int lca=p.LCA(x,st[tail]);
            if(lca==st[tail])
                return;
            while (tail>1 && p.dfsn[st[tail-1]]>=p.dfsn[lca])
            {
                vir.Add(st[tail-1],st[tail]);
                tail--;
            }
            if(lca!=st[tail])
            {
                vir.Add(lca,st[tail]);
                st[tail]=lca;
            }
            st[++tail]=x;
        }
        void build(int y)
        {
            st[tail=1]=0;
            System.out.println();
            vir=new graph(y*3);
            Comparator cmp=new cpr();
            Pair<Integer,Integer> ppp[]=new Pair[cur];
            for(int i=0;i<cur;++i)
                ppp[i]=new Pair<>(part[i],p.dfsn[i]);
            Arrays.sort(ppp,cmp);
            for(int i=0;i<cur;++i)
                insert(ppp[i].getKey());
            while (tail>0)
            {
                vir.Add(st[tail-1],st[tail]);
                tail--;
            }
            cur=0;
        }
        class cpr implements Comparator<Pair<Integer,Integer>>
        {
            @Override
            public int compare(Pair<Integer,Integer> o1,Pair<Integer,Integer> o2) {
                if(o1.getValue()<o2.getValue())
                    return -1;
                else
                    return 1;
            }
        }
    }
}
