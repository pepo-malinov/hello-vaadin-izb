package uni.pu.fmi.views.helloworld;


import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import uni.pu.fmi.data.entity.SamplePerson;


public class SamplePersonForm extends VerticalLayout
{
    private final SamplePerson samplePerson;
    private TextField firstName = new TextField("Име");
    private TextField lastName = new TextField("Фамилия");
    private TextField email = new TextField("email");
    private TextField phone = new TextField("Телефонен номер");
    private DatePicker dateOfBirth = new DatePicker("Рождена дата");
    private Checkbox important = new Checkbox("Важен");

    private BeanValidationBinder<SamplePerson> binder = new BeanValidationBinder<>(SamplePerson.class);


    public SamplePersonForm(SamplePerson samplePerson)
    {
        this.samplePerson = samplePerson;
        init();
    }


    private void init()
    {
        binder.bindInstanceFields(this);
        binder.readBean(samplePerson);
        FormLayout formLayout = new FormLayout();
        formLayout.add(firstName, lastName,
                       email, phone, dateOfBirth, important);
        add(formLayout);
    }


    public boolean commit()
    {
        try
        {
            binder.writeBean(samplePerson);
            return true;
        }
        catch (ValidationException e)
        {
            System.out.println(e.getMessage());
            return false;
        }

    }
}
