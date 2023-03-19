package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import javafx.scene.control.TextField;
import org.checkerframework.checker.units.qual.K;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * WIP: doesn't work yet
 */

class CreateBoardCtrlTest {

    List<String> mockedList = mock(List.class);

    @Mock
    private ServerUtils server;
    @Mock
    private MainCtrl mainCtrl;
    @Mock
    CreateBoardCtrl board = new CreateBoardCtrl(server, mainCtrl);


    @Test
    void emptyTextField() {
        TextField create = board.getCreate();
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                e.setSource(create);
                System.out.println(e.getSource());
                //emptyTextField(); // > triggers stack overflow
            }
        };
        ActionEvent event = mock(ActionEvent.class);
        listener.actionPerformed(event);
        //assertTrue(create.getText().equals(""));
    }

    @Test
    void processClick() {

    }

    @Test
    void keyPressed() {
    }

    @Test
    void goBack() {
    }
}