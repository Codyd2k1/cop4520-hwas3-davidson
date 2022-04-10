package problem1secondtry;

public class Gift {
    int uniqueID;
    int index;

    public Gift(int index) {
        this.index = index;
        this.uniqueID = this.hashCode();
    }
}
