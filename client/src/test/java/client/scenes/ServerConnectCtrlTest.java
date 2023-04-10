package client.scenes;

import client.scenes.MainCtrl;
import client.scenes.ServerConnectCtrl;
import client.utils.ServerUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ServerConnectCtrlTest {
    @Mock
    private ServerUtils serverUtils;
    @Mock
    private MainCtrl mainCtrl;
    @Mock
    private ServerConnectCtrl serverConnectCtrl;

//    @Mock
//    private Button connectButton;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//
////        Text message = mock(Text.class);
////        when(message.setText(anyString())).thenCallRealMethod();
////        when(message.setText("lol")).thenCallRealMethod();
//        serverConnectCtrl.setMessage(new Text());
//
//    }
//
//    @Test
//    public void testConnectToTheServer() {
//        Button connectButton = new Button("lol");
////         connectButton = mock(Button.class);
//        serverConnectCtrl.setConnectButton(connectButton);
//
//        ActionEvent event = mock(ActionEvent.class);
//        when(event.getSource()).thenReturn(connectButton);
//        when(serverUtils.getServer()).thenReturn("http://localhost:8080");
//        when(serverConnectCtrl.serverExists("http://localhost:8080")).thenReturn(true);
//        boolean connected = serverConnectCtrl.connectToTheServer(event);
//        assertEquals(true, connected);
//        verify(serverUtils).setServer("http://localhost:8080");
//        verify(serverUtils).initialiseSession("localhost");
//    }

//    @Test
//    public void testConnectToTheServerInvalidInput() {
//        ActionEvent event = mock(ActionEvent.class);
//        when(serverUtils.getServer()).thenReturn("");
//        boolean connected = serverConnectCtrl.connectToTheServer(event);
//        assertEquals(false, connected);
//        verify(serverUtils, never()).setServer(ArgumentMatchers.anyString());
//        verify(serverUtils, never()).initialiseSession(ArgumentMatchers.anyString());
//    }
//
//    @Test
//    public void testServerExists() throws IOException {
//        HttpURLConnection connection = mock(HttpURLConnection.class);
//        InputStream inputStream = mock(InputStream.class);
//        BufferedReader bufferedReader = mock(BufferedReader.class);
//        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
//        when(connection.getInputStream()).thenReturn(inputStream);
//        when(bufferedReader.readLine()).thenReturn("Talio app-74", null);
//        when(serverConnectCtrl.serverExists("http://localhost:8080")).thenCallRealMethod();
//        when(new URL("http://localhost:8080").openConnection()).thenReturn(connection);
//        boolean exists = serverConnectCtrl.serverExists("http://localhost:8080");
//        assertEquals(true, exists);
//    }
//
//    @Test
//    public void testServerExistsNotFound() throws IOException {
//        HttpURLConnection connection = mock(HttpURLConnection.class);
//        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);
//        when(serverConnectCtrl.serverExists("http://localhost:8080")).thenCallRealMethod();
//        when(new URL("http://localhost:8080").openConnection()).thenReturn(connection);
//        boolean exists = serverConnectCtrl.serverExists("http://localhost:8080");
//        assertEquals(false, exists);
//    }
//
//    @Test
//    public void testServerExistsNotTalioApp() throws IOException {
//        HttpURLConnection connection = mock(HttpURLConnection.class);
//        InputStream inputStream = mock(InputStream.class);
//        BufferedReader bufferedReader = mock(BufferedReader.class);
//        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
//        when(connection.getInputStream()).thenReturn(inputStream);
//        when(bufferedReader.readLine()).thenReturn("not a Talio app", null);
//        when(serverConnectCtrl.serverExists("http://localhost:8080")).thenCallRealMethod();
//        when(new URL("http://localhost:8080").openConnection()).thenReturn(connection);
//        boolean exists = serverConnectCtrl.serverExists("http://localhost:8080");
//
//    }
}