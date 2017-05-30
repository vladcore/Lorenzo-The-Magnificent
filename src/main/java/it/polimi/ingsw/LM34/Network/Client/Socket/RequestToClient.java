package it.polimi.ingsw.LM34.Network.Client.Socket;

import it.polimi.ingsw.LM34.Network.Server.Socket.RequestToServer;

import java.io.IOException;
import java.util.List;

/**
 * Created by vladc on 5/28/2017.
 * inspired by https://stackoverflow.com/questions/12935709/call-a-specific-method-based-on-enum-type
 */
public enum RequestToClient {
    LOGIN {
        @Override
        void readAndHandle(SocketClient socketConnection) {
            try {
                Boolean loginResult = socketConnection.getInputStream().readBoolean();

                socketConnection.getNetworkController().loginResult(loginResult);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    },
    CONTEXT_SELECTION {
        @Override
        void readAndHandle(SocketClient socketConnection) {
            try {
                List<String> contexts = (List<String>) socketConnection.getInputStream().readObject();

                socketConnection.getOutputStream().writeUTF(RequestToServer.CONTEXT_SELECTION.name());
                Integer sel = socketConnection.contextSelection(contexts);
                socketConnection.getOutputStream().writeInt(sel);
                socketConnection.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    };

    abstract void readAndHandle(SocketClient socketConnection);
}
