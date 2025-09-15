package wicket.pages.task;

import h2factory.task.Task;
import h2factory.task.TaskDao;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.security.web.csrf.CsrfToken;

import java.util.List;

public class ListarTaskPage extends WebPage {
    @SpringBean
    private TaskDao taskDao;

    public ListarTaskPage() {

        Link<Void> criarLink = new Link<>("criarLink") {
            @Override
            public void onClick() {
                setResponsePage(CriarTaskPage.class);
            }
        };
        add(criarLink);

        WebMarkupContainer taskListContainer = new WebMarkupContainer("taskList");
        add(taskListContainer);
        List<Task> tasks = taskDao.list();
        ListView<Task> listView = new ListView<>("taskItem", tasks) {
            @Override
            protected void populateItem(ListItem<Task> item) {
                Task task = item.getModelObject();

                item.add(new Label("checkbox") {
                    @Override
                    protected void onConfigure() {
                        super.onConfigure();
                        String cssClass = "checkbox" + (task.getConcluido() ? " completed" : "");
                        add(AttributeModifier.replace("class", cssClass));
                    }
                });

                Label descLabel = new Label("descricao", Model.of(task.getDescricao()));
                if (task.getConcluido()) {
                    descLabel.add(AttributeModifier.append("class", "completed"));
                }
                item.add(descLabel);

                PageParameters params = new PageParameters();
                params.add("taskId", task.getId());
                BookmarkablePageLink<Void> editarLink = new BookmarkablePageLink<>("editarLink", EditarTaskPage.class, params);
                item.add(editarLink);

                Form<?> deletarForm = new Form<Void>("deletarForm") {
                    @Override
                    protected void onSubmit() {
                        taskDao.delete(task.getId());
                        setResponsePage(ListarTaskPage.class);
                    }
                };
                WebRequest webRequest = (WebRequest) getRequest();
                HttpServletRequest servletRequest = (HttpServletRequest) webRequest.getContainerRequest();
                CsrfToken csrfToken = (CsrfToken) servletRequest.getAttribute(CsrfToken.class.getName());
                HiddenField<String> deletarCsrfHidden = new HiddenField<>("_csrf",
                        Model.of(csrfToken != null ? csrfToken.getToken() : ""));
                deletarForm.add(deletarCsrfHidden);

                item.add(deletarForm);
            }

        };
        taskListContainer.add(listView);
    }
}
