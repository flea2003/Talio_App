package client.scenes;

import client.utils.ServerUtils;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
    CreateBoardCtrl board = new CreateBoardCtrl(mainCtrl);


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
//        assertTrue(create.getText().equals(""));
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
