import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.spi.DefaultElementVisitor;
import com.google.inject.spi.Element;
import com.google.inject.spi.Elements;

import client.MyModule;
import client.scenes.DashboardCtrl;
import client.scenes.MainCtrl;
import client.scenes.ServerConnectCtrl;
import client.scenes.TaskEditCtrl;
import client.utils.ServerUtils;

class MyModuleTest {

    @Mock
    private Binder mockBinder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConfigure() {
//        Module myModule = new MyModule();
//        Injector injector = Elements.getElements(myModule)
//                .acceptVisitor(new DefaultElementVisitor<Injector>() {
//                    @Override
//                    public Injector visit(Iterable<? extends Element> elements) {
//                        Injector injector = com.google.inject.Guice.createInjector(Scopes.NO_SCOPE, myModule);
//                        injector.injectMembers(this);
//                        return injector;
//                    }
//
//                    @Override
//                    protected Injector visitOther(Element element) {
//                        throw new UnsupportedOperationException();
//                    }
//                });
//
//        assertNotNull(injector.getInstance(ServerUtils.class));
//        assertNotNull(injector.getInstance(MainCtrl.class));
//        assertNotNull(injector.getInstance(DashboardCtrl.class));
//        assertNotNull(injector.getInstance(TaskEditCtrl.class));
//        assertNotNull(injector.getInstance(ServerConnectCtrl.class));
    }
}