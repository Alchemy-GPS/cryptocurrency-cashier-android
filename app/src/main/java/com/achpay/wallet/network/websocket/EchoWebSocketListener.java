package com.achpay.wallet.network.websocket;

import com.achpay.wallet.utils.Log;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.util.List;
import java.util.Map;


public class EchoWebSocketListener extends WebSocketAdapter {
    private static final String TAG = "EchoWebSocketListener : ";

    private WebsocketProxy proxy;

    public EchoWebSocketListener() {

    }

    public EchoWebSocketListener(WebsocketProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        super.onConnected(websocket, headers);
        proxy.onConnected(websocket, headers);

        /*for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            // Header name.
            String name = entry.getKey();

            // Values of the header.
            List<String> values = entry.getValue();

            if (values == null || values.size() == 0) {
                // Print the name only.
                Log.i(TAG + name);
                continue;
            }

            for (String value : values) {
                // Print the name and the value.
                Log.i(TAG + name + " : " + value);
            }
        }*/
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);

        proxy.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) {
        try {
            super.onTextMessage(websocket, text);
            proxy.onTextMessage(websocket, text);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(e.getMessage());
        }
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
        super.onConnectError(websocket, exception);
        proxy.onConnectError(websocket, exception);
    }

    @Override
    public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) throws Exception {
        super.onMessageError(websocket, cause, frames);
        cause.printStackTrace();
        Log.i("onMessageError");
    }

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
        super.onError(websocket, cause);
        cause.printStackTrace();
        Log.i("onError");
        Log.i(cause.getMessage());
    }

    @Override
    public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        super.onPongFrame(websocket, frame);

        Log.i("PongFrame == " + frame.toString());
    }

    @Override
    public void onPingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
        super.onPingFrame(websocket, frame);
        Log.i("PingFrame == " + frame.toString());
    }
}
