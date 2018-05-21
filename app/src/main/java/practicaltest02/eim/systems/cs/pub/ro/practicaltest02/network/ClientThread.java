package practicaltest02.eim.systems.cs.pub.ro.practicaltest02.network;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import practicaltest02.eim.systems.cs.pub.ro.practicaltest02.general.Constants;
import practicaltest02.eim.systems.cs.pub.ro.practicaltest02.general.Utilities;

public class ClientThread extends Thread {

    private String address;
    private int port;
    private Integer key;
    private String value, informationType;
    private TextView result;

    private Socket socket;

    public ClientThread(String address, int port, Integer key, String value, String informationType, TextView result) {
        this.address = address;
        this.port = port;
        this.key = key;
        this.value = value;
        this.informationType = informationType;
        this.result = result;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            printWriter.println(key);
            printWriter.flush();
            printWriter.println(value);
            printWriter.flush();
            printWriter.println(informationType);
            printWriter.flush();
            String weatherInformation;
            while ((weatherInformation = bufferedReader.readLine()) != null) {
                final String finalizedWeateherInformation = weatherInformation;
                result.post(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(finalizedWeateherInformation);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
