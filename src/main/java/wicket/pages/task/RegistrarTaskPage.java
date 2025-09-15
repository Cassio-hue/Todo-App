package wicket.pages.task;

import h2factory.task.Task;
import h2factory.task.TaskDao;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.web.csrf.CsrfToken;

import java.sql.SQLException;

public class RegistrarTaskPage extends WebPage {
    @SpringBean
    private TaskDao taskDao;
    private Task task;

    private PageParameters parameters;
    private Form<Task> form;
    private TextField<String> descricao;
    private RadioGroup<Boolean> concluidoGroup;
    private Radio<Boolean> concluidoSim;
    private Radio<Boolean> concluidoNao;
    private HiddenField<String> csrfHidden;
    private BookmarkablePageLink<Void> tarefasLink;

    public RegistrarTaskPage() {
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        parameters = getPageParameters();

        if (!parameters.get("taskId").isEmpty()) {
            task = taskDao.getById(parameters.get("taskId").toOptionalInteger());
        } else {
            task = new Task();
        }

        IModel<Task> taskModel = new CompoundPropertyModel<>(task);
        form = new Form<>("taskForm", taskModel) {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                Task task = getModelObject();
                if (task.getId() == null) {
                    taskDao.insert(task);
                } else {
                    taskDao.update(task);
                }
                setResponsePage(ListarTaskPage.class);
            }
        };
        add(form);

        tarefasLink = new BookmarkablePageLink<>("tarefasLink", ListarTaskPage.class);
        add(tarefasLink);

        descricao = new TextField<>("descricao");
        descricao.setRequired(true);
        form.add(descricao);

        concluidoGroup = new RadioGroup<>("concluido");
        form.add(concluidoGroup);

        concluidoSim = new Radio<>("concluidoSim", Model.of(Boolean.TRUE));
        concluidoGroup.add(concluidoSim);

        concluidoNao = new Radio<>("concluidoNao", Model.of(Boolean.FALSE));
        concluidoGroup.add(concluidoNao);
        concluidoGroup.setRequired(true);

        WebRequest webRequest = (WebRequest) getRequest();
        HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getContainerRequest();
        CsrfToken csrfToken = (CsrfToken) servletRequest.getAttribute(CsrfToken.class.getName());
        csrfHidden = new HiddenField<>("_csrf",
                Model.of(csrfToken != null ? csrfToken.getToken() : ""));
        form.add(csrfHidden);
    }
}
