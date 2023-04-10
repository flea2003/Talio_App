package client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import client.MyFXML;
import client.scenes.TaskViewCtrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;

import com.google.inject.Injector;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Pair;

//@ExtendWith(MockitoExtension.class)
public class MyFXMLTest {

    private MyFXML myFXML;

    @Mock
    private Injector mockInjector;

    @BeforeEach
    public void setUp() {
        myFXML = new MyFXML(mockInjector);
    }

    @Test
    public void testLoad() throws IOException {
        // Arrange
        String fxmlPath = "path/to/fxml";
        String controllerPath = "path/to/controller";

        var expectedParent = mock(Parent.class);
        var expectedController = mock(TaskViewCtrl.class);

        var mockLoader = mock(FXMLLoader.class);
        Mockito.when(mockLoader.load()).thenReturn(expectedParent);
        Mockito.when(mockLoader.getController()).thenReturn(expectedController);

        Mockito.when(mockInjector.getInstance(TaskViewCtrl.class)).thenReturn(expectedController);

//        Mockito.whenNew(FXMLLoader.class).withAnyArguments().thenReturn(mockLoader);

        // Act
        Pair<TaskViewCtrl, Parent> actualResult = myFXML.load(TaskViewCtrl.class, fxmlPath, controllerPath);

        // Assert
        assertEquals(expectedController, actualResult.getKey());
        assertEquals(expectedParent, actualResult.getValue());
    }
}