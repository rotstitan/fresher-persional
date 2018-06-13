package zcore.dos;

/**
 *
 * @author lybn
 */
public class DOSInfo {

    private int counter;
    private long firstTime;

    public DOSInfo(int counter, long firstTime) {
        this.counter = counter;
        this.firstTime = firstTime;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void increaseCounter() {
        this.counter++;
    }

    public long getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(long firstTime) {
        this.firstTime = firstTime;
    }
}
