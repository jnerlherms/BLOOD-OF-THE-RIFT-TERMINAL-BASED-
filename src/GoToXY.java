public class GoToXY {
    public void move(int x, int y) {
        System.out.printf("\033[%d;%dH", y, x);
    }
}
