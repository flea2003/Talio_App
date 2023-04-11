package server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServerTest {

    @Test
    public void configTest(){
        Config config = new Config();
        assertNotNull(config.getRandom());
    }

    @Test
    public void someControllerTest(){
        SomeController someController = new SomeController();
        assertEquals("Talio app-74", someController.index());
    }

    
}
