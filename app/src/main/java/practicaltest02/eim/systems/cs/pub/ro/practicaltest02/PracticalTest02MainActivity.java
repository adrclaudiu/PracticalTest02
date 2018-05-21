package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import practicaltest02.eim.systems.cs.pub.ro.practicaltest02.general.Constants;
import practicaltest02.eim.systems.cs.pub.ro.practicaltest02.network.ClientThread;
import practicaltest02.eim.systems.cs.pub.ro.practicaltest02.network.ServerThread;

public class PracticalTest02MainActivity extends AppCompatActivity {

    // Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    // Client widgets
    private EditText clientPutKey = null;
    private EditText clientPutValue = null;
    private EditText clientGetKey = null;

    private Button postButton = null;
    private Button getButton = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private TextView result = null;


    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }

    }

    private PutButtonClickListener putButtonClickListener = new PutButtonClickListener();
    private class PutButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = "localhost";
            Integer clientPort = 5000;
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            Integer key = Integer.parseInt(clientPutKey.getText().toString());
            String value = clientPutValue.getText().toString();
            String informationType = "post";

            result.setText("");
            clientThread = new ClientThread(
                    clientAddress, clientPort, key, value, informationType, result
            );
            clientThread.start();
        }

    }

    private GetButtonClickListener getButtonClickListener = new GetButtonClickListener();
    private class GetButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = "localhost";
            Integer clientPort = 5000;
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            Integer key = Integer.parseInt(clientGetKey.getText().toString());
            String informationType = "get";

            result.setText("");
            clientThread = new ClientThread(
                    clientAddress, clientPort, key, "", informationType, result
            );
            clientThread.start();
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        clientPutKey = (EditText)findViewById(R.id.put_key);
        clientPutValue = (EditText)findViewById(R.id.put_value);
        postButton = (Button)findViewById(R.id.put_button);
        postButton.setOnClickListener(putButtonClickListener);

        clientGetKey = (EditText)findViewById(R.id.get_key);
        getButton = (Button)findViewById(R.id.get_button);
        getButton.setOnClickListener(getButtonClickListener);

        result = (TextView)findViewById(R.id.result);
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
