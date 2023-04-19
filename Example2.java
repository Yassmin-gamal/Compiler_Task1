public class main {

    public static void main(String[] args) {
        int x=10;
        int y=6;
        myMethod();
        if(x==10&&y==6)
            System.out.println(x);

        else
            System.out.println("error");

        for(int i=0;i<x;i++){
        }
        if(x>5 || x==5 || y==6)x++;
        else x--;
        myMethod();
    }
    public static void myMethod() {
        System.out.println("I just got executed!");
    }
}

