import javafx.util.Pair;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

public class 树链剖分
{
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
                edge[i]=new LinkedList<Integer>();
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
        void query(tree_array1 p,int x,int y,int k) //区间加值 query具体看题目写 输入原树编号
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
        long query2(tree_array1 p,int x,int y) //区间询问 query具体看题目写 输入原树编号
        {
            long ans=0;
            while(top[x]!=top[y])
            {
                if(dep[top[x]]<dep[top[y]]){ int mid=x;x=y;y=mid;}
                ans+=p.range_ask(id[top[x]],id[x]);
                x=fa[top[x]];
            }
            if(dep[x]>dep[y]){ int mid=x;x=y;y=mid;}
            ans+=p.range_ask(id[x],id[y]);
            return ans;
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
            dfs1(root,root);
            dfs2(root,root);
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
    /*
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
        int ans=0;
        for(int i=vir.Begin[x];i!=-1;i=vir.Next[i])
        {
            System.out.println(vir.to[i]);
            dp(vir.to[i]);
            a[x]+=a[i];
            b[x]+=b[i];
        }
        ans=a[x]*b[x]*step[x];
        return ans;

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
    */
}
