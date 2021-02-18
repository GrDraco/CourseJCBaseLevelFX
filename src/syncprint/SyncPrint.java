package syncprint;

class SyncPrint extends Thread {
    private static final Object syncObj = new Object();
    private static volatile char currentLetter;

    public static void startPrint(int countRepeat, char... charsToPrint) {
        if (charsToPrint == null || charsToPrint.length == 0)
            return;
        currentLetter = charsToPrint[0];
        int next = 0;
        for(char ch: charsToPrint) {
            next = next < charsToPrint.length - 1 ? next + 1 : 0;
            new SyncPrint(ch, charsToPrint[next], countRepeat).start();
        }
    }

    private final char charPrint;
    private final char charNextPrint;
    private final int countRepeat;

    public SyncPrint(char charPrint, char charNextPrint, int countRepeat) {
        this.charPrint = charPrint;
        this.charNextPrint = charNextPrint;
        this.countRepeat = countRepeat;
    }

    @Override
    public void run() {
        if(countRepeat == 0)
            return;
        synchronized (syncObj) {
            try {
                for (int i = 0; i < countRepeat; i++) {
                    while (currentLetter != charPrint) {
                        syncObj.wait();
                    }
                    System.out.print(charPrint);
                    currentLetter = charNextPrint;
                    syncObj.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
