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

public class EditarTaskPage extends WebPage {
    @SpringBean
    private TaskDao taskDao;

    public EditarTaskPage(final PageParameters parameters) {
        super(parameters);
        Task task;
        if (parameters.get("taskId") != null) {
            task = taskDao.getById(Integer.parseInt(parameters.get("taskId").toString()));
        } else {
            task = new Task();
        }

        IModel<Task> taskModel = new CompoundPropertyModel<>(task);
        Form<Task> form = new Form<>("taskForm", taskModel) {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                Task task = getModelObject();
                taskDao.update(task);
                setResponsePage(ListarTaskPage.class);
            }
        };
        add(form);

        TextField<String> descricao = new TextField<>("descricao");
        descricao.setRequired(true);
        form.add(descricao);

        RadioGroup<Boolean> concluidoGroup = new RadioGroup<>("concluido");
        form.add(concluidoGroup);

        Radio<Boolean> concluidoSim = new Radio<>("concluidoSim", Model.of(Boolean.TRUE));
        Radio<Boolean> concluidoNao = new Radio<>("concluidoNao", Model.of(Boolean.FALSE));
        concluidoGroup.add(concluidoSim);
        concluidoGroup.add(concluidoNao);
        concluidoGroup.setRequired(true);

        BookmarkablePageLink<Void> tarefasLink = new BookmarkablePageLink<>("tarefasLink", ListarTaskPage.class);
        add(tarefasLink);

        WebRequest webRequest = (WebRequest) getRequest();
        HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getContainerRequest();
        CsrfToken csrfToken = (CsrfToken) servletRequest.getAttribute(CsrfToken.class.getName());
        HiddenField<String> csrfHidden = new HiddenField<>("_csrf",
                Model.of(csrfToken != null ? csrfToken.getToken() : ""));
        form.add(csrfHidden);
    }
}
