package id.ac.ui.cs.mobileprogramming.rizkhiph.notifime;

public class JniManager {
    static {
        System.loadLibrary("native-lib");
    }

    public native String nBuildMessage(String title, String date, String time);

    public native String nBuildText(String title);
}
