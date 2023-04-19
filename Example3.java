public class main {

    public static void main(String[] args) {
        int x=10;
        int y=6;
        myMethod();
        if(x==0&&y==6)
            System.out.println(x);

        else
            System.out.println("error");

        for(int i=0;i<x;i++){
        }
        if(y>10||x<10&&x==6)x++;
        else x--;
        myMethod();

        while(y>0 ||x<5)
            y--;
    }
    public static void myMethod() {
        System.out.println("I just got executed!");
    }
}

