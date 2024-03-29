package utils;

/**
 * Auxiliary class counter useful for making some magic with thymeleaf
 */
public class Counter
{
    private int count;

    public Counter()
    {
        count = 0;
    }

    public Counter(int init)
    {
        count = init;
    }

    public int get()
    {
        return count;
    }

    public void clear()
    {
        count = 0;
    }

    public int getAndIncrement()
    {
        int aux_cont = count;
        count++;
        return aux_cont;
    }

    public void increment()
    {
        count++;
    }

    public String toString()
    {
        return ""+count;
    }
}