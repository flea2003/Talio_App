import client.scenes.MainCtrl;
import client.scenes.TaskCreationCtrl;
import client.scenes.services.taskCreations;
import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class TaskCreationCtrlTest {

    private TaskCreationCtrl taskCreationCtrl;

    @Mock
    private ServerUtils serverUtils;

    @Mock
    private MainCtrl mainCtrl;

    @Mock
    private AnchorPane pane;

    @Mock
    private Text error;

    private List listCurr;
    private long boardId;
    private Scene taskCreation;
    private Stage newStage;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        taskCreationCtrl = new TaskCreationCtrl(serverUtils, mainCtrl);
//        taskCreationCtrl.   setError("Error");
//        taskCreationCtrl.setPane(pane);
//        taskCreationCtrl.setTaskName(new TextField());
//        taskCreationCtrl.setTaskDescription(new TextField());
//        listCurr = new List(1, null, "Test List", new Board(1, null, "Test Board"));
//        boardId = 1;
//        taskCreation = new Scene(new AnchorPane());
//        newStage = new Stage();
    }

    @Test
    void processClickShouldCreateNewCard() {
//        Card card = new Card("Test description", "Test card", listCurr, 1);
//        when(serverUtils.getList(listCurr.id)).thenReturn(listCurr);
//        when(serverUtils.getBoard(boardId)).thenReturn(new Board(boardId, null, "Test Board"));
//        taskCreationCtrl.setListCurr(listCurr);
//        taskCreationCtrl.setBoardId(boardId);
//        taskCreationCtrl.sendData(newStage, taskCreation, boardId, listCurr);
//        taskCreationCtrl.getTaskName().setText("Test card");
//        taskCreationCtrl.getTaskDescription().setText("Test description");
//        taskCreationCtrl.processClick(null);
//        verify(serverUtils, times(1)).updateBoard(any(Board.class));
//        verify(taskCreations.getInstance(), times(1)).remove(taskCreationCtrl);
//        verify(newStage, times(1)).close();
//        assert (listCurr.cards.size() == 1);
//        assert (listCurr.cards.get(0).equals(card));
    }

    @Test
    void processClickShouldNotCreateCardWithEmptyName() {
//        when(serverUtils.getList(listCurr.id)).thenReturn(listCurr);
//        when(serverUtils.getBoard(boardId)).thenReturn(new Board(boardId, "Test Board"));
//        taskCreationCtrl.setListCurr(listCurr);
//        taskCreationCtrl.setBoardId(boardId);
//        taskCreationCtrl.sendData(newStage, taskCreation, boardId, listCurr);
//        taskCreationCtrl.getTaskName().setText("");
//        taskCreationCtrl.getTaskDescription().setText("Test description");
//        taskCreationCtrl.processClick(null);
//        verify(serverUtils, never()).updateBoard(any(Board.class));
//        verify(taskCreations.getInstance(), never()).remove(taskCreationCtrl);
//        verify(newStage, never()).close();
//        assert (listCurr.cards.size() == 0);
    }
}