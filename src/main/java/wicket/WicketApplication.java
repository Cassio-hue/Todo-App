package wicket;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.stereotype.Component;
import wicket.pages.task.CriarTaskPage;
import wicket.pages.task.EditarTaskPage;
import wicket.pages.task.ListarTaskPage;

@Component
public class WicketApplication extends WebApplication {
    @Override
    public void init() {
        super.init();
        getComponentInstantiationListeners().add(new SpringComponentInjector(this));
        getCspSettings().blocking().disabled();
        mountPage("/listar-task", ListarTaskPage.class);
        mountPage("/criar-task", CriarTaskPage.class);
        mountPage("/editar-task", EditarTaskPage.class);
    }

    @Override
    public Class<? extends WebPage> getHomePage() {
        return ListarTaskPage.class;
    }
}
