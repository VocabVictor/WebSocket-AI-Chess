package Game;

public class Stack
{
    public Point point[];
    private int end;
    private int size;
    public Stack(int size)
    {
        point = new Point[size];
        this.end = -1;
        this.size = size;
    }
    public void push(int x,int y)
    {
        if( this.end < size)
        {
            this.end++;
            this.point[end] = new Point(x,y);
        }
    }
    public Point pop()
    {
        if(end!=-1)
        {
            return point[end--];
        }
        return null;
    }

    public boolean isEmpty()
    {
        return end==-1;
    }
}