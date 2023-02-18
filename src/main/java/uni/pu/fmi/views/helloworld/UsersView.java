package uni.pu.fmi.views.helloworld;


import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.selection.MultiSelect;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.apache.commons.lang3.StringUtils;
import uni.pu.fmi.data.entity.SamplePerson;
import uni.pu.fmi.data.service.SamplePersonRepository;
import uni.pu.fmi.views.MainLayout;

import java.util.Set;


@PageTitle("Потребители")
@Route(value = "users", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class UsersView extends VerticalLayout
{

    private SamplePersonRepository samplePersonRepository;
    private TextField name;
    private Button sayHello;
    Grid<SamplePerson> samplePersonGrid;


    public UsersView(SamplePersonRepository samplePersonRepository)
    {
        this.samplePersonRepository = samplePersonRepository;
        name = new TextField("Въведете вашето име");
        name.addValueChangeListener(l->{
            String value = l.getValue();
            if(StringUtils.isNoneBlank()){
                resetGrid(samplePersonRepository);
            } else {
                samplePersonGrid.setItems(samplePersonRepository.findAllByLastNameContaining(value));
            }
        });
        sayHello = new Button("Кажи здравей :)");
        sayHello.addClickListener(e -> Notification.show("Здравей " + name.getValue()));
        sayHello.addClickShortcut(Key.ENTER);

        setMargin(true);
        //setVerticalComponentAlignment(Alignment.END, name, sayHello);

        add(name, sayHello);

        final ComboBox<SamplePerson> samplePersonComboBox = new ComboBox<>("Служители");
        samplePersonComboBox.setItems(samplePersonRepository.findAll());
        samplePersonComboBox.setItemLabelGenerator(SamplePerson::getFirstName);
        samplePersonComboBox.addValueChangeListener(l->{
            SamplePerson value = l.getValue();
            if(value==null){
                resetGrid(samplePersonRepository);
            } else {
                samplePersonGrid.setItems(samplePersonRepository.findAllByFirstName(value.getFirstName()));
            }
        });

        final Button addButton = new Button("Добави", l->openUserDialog(new SamplePerson()));
        final Button editButton = new Button("Промени");
        editButton.setEnabled(false);
        editButton.addClickListener(l ->
                                    {
                                        Notification.show("Натиснахте промени!", 2000, Notification.Position.TOP_END);
                                        Set<SamplePerson> value = samplePersonGrid.asMultiSelect().getValue();
                                        SamplePerson samplePerson = value.iterator().next();
                                        openUserDialog(samplePerson);
                                    });
        final Button removeButton = new Button("Изтрий");
        removeButton.setEnabled(false);
        removeButton.addClickListener(l ->
                                      {
                                          MultiSelect<Grid<SamplePerson>, SamplePerson> gridSamplePersonMultiSelect = samplePersonGrid.asMultiSelect();
                                          Set<SamplePerson> selectValues = gridSamplePersonMultiSelect.getValue();
                                          samplePersonRepository.deleteAll(selectValues);
                                          Notification.show("Изтрихте " + selectValues.size() + " записа", 2000,
                                                            Notification.Position.TOP_CENTER);
                                          resetGrid(samplePersonRepository);
                                      });
        final HorizontalLayout buttons = new HorizontalLayout(samplePersonComboBox, addButton, editButton, removeButton);
        add(buttons);

        samplePersonGrid = new Grid<>(SamplePerson.class, false);
        samplePersonGrid.addColumn(SamplePerson::getFirstName).setHeader("Име").setSortable(true);
        samplePersonGrid.addColumn(SamplePerson::getLastName).setHeader("Фамилия");
        samplePersonGrid.addColumn(SamplePerson::getDateOfBirth).setHeader("Рожденна дата");
        resetGrid(samplePersonRepository);
        samplePersonGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        samplePersonGrid.addSelectionListener(sl ->
                                              {
                                                  int size = sl.getAllSelectedItems().size();
                                                  Notification.show("Избрахте " + size + " записа.");
                                                  removeButton.setEnabled(size > 0);
                                                  editButton.setEnabled(size == 1);
                                                 /* if(size>0){
                                                      removeButton.setEnabled(true);
                                                  }else{
                                                      removeButton.setEnabled(false);
                                                  }*/
                                              });
        add(samplePersonGrid);
    }


    private void resetGrid(SamplePersonRepository samplePersonRepository)
    {
        samplePersonGrid.setItems(samplePersonRepository.findAll());
    }


    private void openUserDialog(SamplePerson samplePerson)
    {
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("New employee");

        SamplePersonForm dialogLayout = new SamplePersonForm(samplePerson);
        dialog.add(dialogLayout);

        Button saveButton = new Button("Съхрани", l ->
        {
            if (dialogLayout.commit())
            {
                samplePersonRepository.save(samplePerson);
                dialog.close();
                resetGrid(samplePersonRepository);
            }
        });
        Button cancelButton = new Button("Затвори", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);
        dialog.open();

    }

}
