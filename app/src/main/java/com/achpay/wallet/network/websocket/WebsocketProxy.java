package com.achpay.wallet.network.websocket;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.util.List;
import java.util.Map;


public interface WebsocketProxy {

    void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer);

    void onTextMessage(WebSocket websocket, String text);

    void onConnectError(WebSocket websocket, WebSocketException exception);

    void onConnected(WebSocket websocket, Map<String, List<String>> headers);
}
