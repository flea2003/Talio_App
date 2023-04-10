//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import client.scenes.MainCtrl;
//import client.scenes.TaskEditCtrl;
//import client.scenes.services.CardControllerState;
//import client.scenes.services.taskEdits;
//import client.utils.ServerUtils;
//import commons.Board;
//import commons.Card;
//import commons.List;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextArea;
//import javafx.scene.text.Text;
//import javafx.stage.Stage;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//class TaskEditCtrlTest {
//
//    @Mock
//    private ServerUtils server;
//
//    @Mock
//    private MainCtrl mainCtrl;
//
//    @Mock
//    private Scene scene;
//
//    @Mock
//    private Card card;
//
//    @Mock
//    private TextArea name;
//
//    @Mock
//    private TextArea description;
//
//    @Mock
//    private Text error;
//
//    @Mock
//    private Button done;
//
//    @Mock
//    private List listCurr;
//
//    @Mock
//    private Board boardCurr;
//
//    @Mock
//    private Stage stage;
//
//    @InjectMocks
//    private TaskEditCtrl taskEditCtrl;
//
//    @BeforeEach
//    void setUp() {
////        MockitoAnnotations.openMocks(this);
//        taskEditCtrl = mock(TaskEditCtrl.class);
//        name = mock(TextArea.class);
//        description = mock(TextArea.class);
//        card = mock(commons.Card.class);
//    }
//
//    @Test
//    void testRenderInfo() {
//        Card q = new Card();
//        q.setName("Test");
//        q.setDescription("Test description");
//
//        taskEditCtrl.renderInfo(q);
//
//        verify(name).setText("Test");
//        verify(description).setText("Test description");
//        verifyNoMoreInteractions(name, description);
//    }
//
//    @Test
//    void testSetDone() {
////        when(name.getText()).thenReturn("Test");
////        when(description.getText()).thenReturn("Test description");
////
////        taskEditCtrl.setDone();
////
////        verify(server).updateBoard(boardCurr);
////        verify(error).setText("");
////        verify(currCard).setName("Test");
////        verify(currCard).setDescription("Test description");
////        verify(listCurr).getID();
////        verify(boardCurr).lists();
////        verifyNoMoreInteractions(server, error, currCard, listCurr, boardCurr);
//    }
//
//    @Test
//    void testSetDoneEmptyName() {
//        when(name.getText()).thenReturn("");
//        when(description.getText()).thenReturn("Test description");
//
//        taskEditCtrl.setDone();
//
//        verify(error).setText("Task Name cannot be empty. Please try again!");
//        verifyNoMoreInteractions(error);
//    }
//
//    @Test
//    void testSetCard() {
//        when(name.getText()).thenReturn("Test");
//        when(description.getText()).thenReturn("Test description");
//
//        Card result = taskEditCtrl.setCard(card);
//
//        assertEquals(result, card);
//        verify(card).setName("Test");
//        verify(card).setDescription("Test description");
//        verifyNoMoreInteractions(card);
//    }
//
//    @Test
//    void testSetListCurr() {
////        List list = new List();
////
////        taskEditCtrl.setListCurr(list);
////
////        assertEquals(list, taskEditCtrl.listCurr);
//    }
//
//    @Test
//    void testSetBoardCurr() {
////        Board board = new Board();
////
////        taskEditCtrl.setBoardCurr(board);
////
////        assertEquals(board, taskEditCtrl.boardCurr);
//    }
//
//    @Test
//    void testGetCard() {
////        Card result = taskEditCtrl.getCard();
////
////        assertEquals(result, taskEditCtrl.currCard);
//    }
//
//    @Test
//    void testGetStage() {
//        assertNull(taskEditCtrl.getStage());
//    }
//
//}