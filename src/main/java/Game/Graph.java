package Game;

public class Graph{
    private int GrapgMatraix[][];
    private int chess_num;
    private int rows;
    private int cols;
    public Graph(int rows,int cols)
    {
        GrapgMatraix = new int[rows][cols];
        this.rows = rows;
        this.cols = cols;
        chess_num = 0;
    }
    public void set(int x,int y,int player)
    {
        if(x>0&&x<rows&&y>0&&y<cols)
        {
            GrapgMatraix[x][y] = player;
            chess_num++;
        }
    }
    public int get_chess_num()
    {
        return chess_num;
    }
    public int getLine(Point p,int direction,int step)
    {
        return direction;
    }
}