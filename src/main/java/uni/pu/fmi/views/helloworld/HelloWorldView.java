package uni.pu.fmi.views.helloworld;


import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import uni.pu.fmi.data.entity.SamplePerson;
import uni.pu.fmi.data.service.SamplePersonRepository;
import uni.pu.fmi.views.MainLayout;


@PageTitle("Hello World")
@Route(value = "hello", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class HelloWorldView extends VerticalLayout
{

    private TextField name;
    private Button sayHello;


    public HelloWorldView(SamplePersonRepository repository)
    {
        name = new TextField("Въведете вашето име");
        sayHello = new Button("Кажи здравей :)");
        sayHello.addClickListener(e -> Notification.show("Здравей " + name.getValue()));
        sayHello.addClickShortcut(Key.ENTER);

        setMargin(true);
        //setVerticalComponentAlignment(Alignment.END, name, sayHello);

        add(name, sayHello);

        final ComboBox<SamplePerson> samplePersonComboBox = new ComboBox<>("Служители");
        samplePersonComboBox.setItems(repository.findAll());
        samplePersonComboBox.setItemLabelGenerator(SamplePerson::getFirstName);
        add(samplePersonComboBox);
        final Grid<SamplePerson> samplePersonGrid = new Grid<>(SamplePerson.class, false);
        samplePersonGrid.addColumn(SamplePerson::getFirstName).setHeader("Име").setSortable(true);
        samplePersonGrid.addColumn(SamplePerson::getLastName).setHeader("Фамилия");
        samplePersonGrid.addColumn(SamplePerson::getDateOfBirth).setHeader("Рожденна дата");
        samplePersonGrid.setItems(repository.findAll());
        samplePersonGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        samplePersonGrid.addSelectionListener(sl ->
                                              {
                                                  Notification.show("Избрахте " + sl.getAllSelectedItems().size() + " записа.");
                                              });
        add(samplePersonGrid);
    }

}
