import javafx.util.Pair;

import java.util.Arrays;
import java.util.Comparator;

public class 树链剖分
{

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
