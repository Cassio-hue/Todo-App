package wicket;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.stereotype.Component;
import wicket.pages.task.RegistrarTaskPage;
import wicket.pages.task.ListarTaskPage;

@Component
public class WicketApplication extends WebApplication {
    @Override
    public void init() {
        super.init();
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        getCspSettings().blocking().disabled();
        mountPage("/listar-task", ListarTaskPage.class);
        mountPage("/registrar-task", RegistrarTaskPage.class);
    }

    @Override
    public Class<? extends WebPage> getHomePage() {
        return ListarTaskPage.class;
    }
}
