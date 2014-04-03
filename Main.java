import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;


public class Main {
    static  boolean flag = false;
    public static void main(String[] args) throws IOException {

        BufferedReader bf = new BufferedReader(new FileReader("kthmax.in"));
        StringTokenizer st = new StringTokenizer(bf.readLine());
        PrintWriter out = new PrintWriter("kthmax.out");
        int n = Integer.parseInt(st.nextToken());
        /*ArrayList<Integer> mas =new ArrayList<Integer>();
        for (int i = 0; i < 40;i++)
        {
            Random a = new Random();
            int t= a.nextInt(1000000);
            out.println("1 "+t);
            mas.add(t);

        }
        for (int i = 0; i < mas.size();i++)
        {
            out.println("-1 "+ mas.get(i));
        }
        out.close();*/
        Tree t = new Tree();
        for (int i = 0; i < n;i++)
        {
            st = new StringTokenizer(bf.readLine());
            int op = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());
            if (op == 1)
            {
               flag = false;
               insert(k, t);
            }
            if (op == 0)
            {
                out.println(findKth(t.left.size - k+1,t.left));
            }
            if (op == -1)
            {
                flag = false;
                delete(t.left,k);
            }
        }
        out.close();
    }
    public static class Tree
    {
        Tree left,right;
        int dif;
        int value;
        int size;
        int leftSize;
        Tree prev;
        boolean weLeft;
        Tree()
        {
            left = right = null;
            dif = 0;
            size = 0;
            leftSize = 0;
            prev = null;
            value = Integer.MAX_VALUE;
        }
        Tree(int k)
        {
            value = k;
            dif = 0;
            size = 1;
            leftSize = 0;
        }
        Tree(Tree b)
        {
            this.left = b.left;
            this.right = b.right;
            this.leftSize = b.leftSize;
            this.size = b.size;
            this.dif = b.dif;
            this.value = b.value;
        }

    }
    static void insert(int k,Tree tek)
    {
         if (k < tek.value)
         {
             if (tek.left == null)
             {
                 tek.left = new Tree(k);
                 tek.left.prev = tek;
                 tek.left.weLeft = true;
                 tek.dif++;

             }  else
             {
                 tek.leftSize++;
                 tek.size++;
                 insert(k,tek.left);
                 if (flag)
                 {
                     return;
                 }
                 tek.dif++;
             }
         }  else
         {
             if (tek.right == null)
             {
                 tek.right = new Tree(k);
                 tek.right.prev = tek;
                 tek.right.weLeft = false;
                 tek.dif--;
             } else
             {
                 tek.size++;
                 insert(k, tek.right);

                 if (flag)
                 {
                     return;
                 }
                 tek.dif--;

             }
         }
        if (tek.prev == null)
        {
            return;
        }
        if (tek.dif == 0)
        {
            tek.size = size(tek.left) + size(tek.right) + 1;
            tek.leftSize = size(tek.left);
            flag = true;
            return;
        }
        if (tek.dif == 2 || tek.dif == -2)
        {
            rotate(tek,false);
            /*if (tek.dif == 0)
            {
                flag = true;
            } */
            return;
        }
        tek.size = size(tek.left) + size(tek.right) + 1;
        tek.leftSize = size(tek.left);


    }
    static void rotate(Tree tek,boolean flagDelete)
    {
        if (tek.dif == -2 &&(getDif(tek.right) == -1 || getDif(tek.right) == 0))
        {
            smallLeftRotate(tek,flagDelete);
        } else
        if (tek.dif == -2 && tek.right.dif == 1 &&(getDif(tek.right.left) * getDif(tek.right.left)<=1))
        {
            bigLeftRotate(tek,flagDelete);
        } else
        if (tek.dif == 2 && (getDif(tek.left) == 1 || getDif(tek.left) == 0))
        {
            smallRightRotate(tek,flagDelete);
        } else
        {
            bigRightRotate(tek,flagDelete);
        }


    }
    static int getDif(Tree a)
    {
        if (a == null)
        {
            return 0;
        }
        return a.dif;
    }
    static void smallLeftRotate(Tree tek,boolean flagDelete)
    {
        
        Tree b = tek.right;
        if (tek.weLeft)
        {
            tek.prev.left = tek.right;
            b.weLeft = true;
        }  else
        {
            tek.prev.right = tek.right;
            b.weLeft = false;
        }
        if (b.left != null)
        {
            b.left.prev = tek;
            b.left.weLeft = false;
        }
        tek.right = b.left;
        b.left = tek;
        b.dif++;
        tek.dif =  -b.dif;

        tek.size = size(tek.left) + size(tek.right) + 1;
        tek.leftSize = size(tek.left);
        b.size = size(b.left) + size(b.right) + 1;
        b.leftSize = size(b.left);
        b.prev = tek.prev;
        tek.prev = b;
        tek.weLeft = true;
        if (!flagDelete){
            flag = (b.dif == 0);
        } else
        {
            flag = (b.dif == 1 || b.dif == -1);
        }

    }
    static void bigLeftRotate(Tree a,boolean flagDelete)
    {
        Tree c = a.right.left;
        if (a.weLeft)
        {
            a.prev.left = c;
             c.weLeft = true;
        } else
        {
            a.prev.right = c;
            c.weLeft = false;
        }
        Tree b = a.right;
        b.left = c.right;
        if (b.left != null)
        {
            b.left.prev = b;
            b.left.weLeft = true;
        }
        a.right = c.left;

        if (a.right != null)
        {
            a.right.prev = a;
            a.right.weLeft = false;
        }

        c.left = a;
        c.right = b;

        b.prev = c;
        b.weLeft = false;

        c.prev = a.prev;
        a.prev = c;
        a.weLeft = true;


        if (c.dif == 1)
        {
            a.dif = 0;
            b.dif = -1;
        }  else{
            if (c.dif == -1)
            {
                a.dif = 1;

            }  else a.dif = 0;
            b.dif = 0;
        }
        c.dif = 0;
        if (!flagDelete){
            flag = true;
        }
        b.size = size(b.left)+size(b.right) + 1;
        b.leftSize = size(b.left);
        a.size = size(a.left) + size(a.right) + 1;
        a.leftSize = size(a.left);
        c.leftSize = size(c.left);
        c.size = size(c.left) +size(c.right) + 1;

    }
    static void smallRightRotate(Tree a,boolean flagDelete)
    {
       Tree b = a.left;
       if (a.weLeft)
       {
           a.prev.left = b;
           b.weLeft = true;
       } else
       {
           a.prev.right = b;
           b.weLeft = false;
       }
        a.left = b.right;
        b.right = a;
        b.prev = a.prev;

        a.prev = b;
        a.weLeft = false;
        if (a.left != null)
        {
            a.left.prev = a;
            a.left.weLeft = true;
        }

        b.dif--;
        a.dif = -b.dif;
        a.size = size(a.left) + size(a.right) + 1;
        a.leftSize = size(a.left);
        b.size = size(b.left) + size(b.right) + 1;
        b.leftSize = size(b.left);
        if (!flagDelete){
            flag = (b.dif == 0);
        }  else
        {
            flag = (b.dif == 1 || b.dif == -1);
        }
    }
    static void bigRightRotate(Tree a,boolean flagDelete)
    {
        Tree c = a.left.right;
        if (a.weLeft)
        {
            a.prev.left = c;
            c.prev = a.prev;
            c.weLeft = true;
        } else
        {
            a.prev.right = c;
            c.prev = a.prev;
            c.weLeft = false;
        }
        Tree b = a.left;
        a.left = c.right;
        b.right = c.left;
        c.left = b;
        c.right = a;

        a.prev = c;
        a.weLeft = false;

        b.prev = c;
        b.weLeft = true;

        if (b.right != null)
        {
            b.right.prev = b;
            b.right.weLeft = false;
        }
        if (a.left != null)
        {
            a.left.prev = a;
            a.left.weLeft = true;
        }
        if (c.dif == -1)
        {
            b.dif = 1;
            a.dif = 0;
        }  else
        {
            if (c.dif == 1)
            {
                a.dif = -1;
            } else a.dif =0;
            b.dif = 0;
        }
        c.dif = 0;
        if (!flagDelete){
            flag = true;
        }
        b.size = size(b.left) + size(b.right) + 1;
        b.leftSize = size(b.left);
        a.size = size(a.left)+size(a.right) + 1;
        a.leftSize = size(a.left);
        c.size = size(c.left) +size(c.right) + 1;
        c.leftSize = size(c.left);
    }
    static int  findKth(int k, Tree t)
    {
        if (k == t.leftSize + 1)
        {
            return t.value;
        }
        if (k < t.leftSize + 1)
        {
            return findKth(k,t.left);
        }  else
        {
            return findKth(k - t.leftSize - 1,t.right);
        }
    }
    static int size(Tree tek)
    {
        if (tek == null)
        {
            return 0;
        }
        return tek.size;
    }
    static void delete(Tree tek,int key)
    {
        if (tek.value > key)
        {
            tek.size--;
            tek.leftSize--;
            delete(tek.left,key);

            if (flag)
            {
                return;
            }
            tek.dif--;


        } else
        if (tek.value < key)
        {
            tek.size--;
            delete(tek.right,key);

            if (flag)
            {
                return;
            }
            tek.dif++;
        } else
        if (tek.left == null && tek.right == null)
        {
              if (tek.weLeft)
              {
                  tek.prev.left = null;
              } else
              {
                  tek.prev.right = null;
              }
            return;
        }  else
        {

            int r = findRight(tek.right);
            int l = findLeft(tek.left);
            if (r != Integer.MAX_VALUE)
            {
                tek.value = r;
                tek.size--;
                delete (tek.right, r);

                if (flag)
                {
                    return;
                }
                tek.dif++;
            }     else
            {
                tek.value = l;
                tek.size--;
                tek.leftSize--;
                delete(tek.left,l);

                if (flag)
                {
                    return;
                }
                tek.dif--;

            }
        }
        if (tek.dif == 1 || tek.dif == -1)
        {
            flag = true;
            return;
        }
        if (tek.dif == 2 || tek.dif == -2)
        {
            rotate(tek,true);
        }

    }
    static int findRight(Tree tek)
    {
        if (tek == null)
        {
            return Integer.MAX_VALUE;
        }
         if (tek.left != null)
         {
             return findRight(tek.left);
         }
        return tek.value;
    }
    static int findLeft(Tree tek)
    {
        if (tek == null)
        {
            return -1;
        }
        if (tek.right!= null)
        {
            return findLeft(tek.right);
        }
        return tek.value;
    }

}
