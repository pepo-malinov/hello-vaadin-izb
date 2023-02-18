package uni.pu.fmi.data.entity;


import javax.persistence.Entity;


@Entity
public class Item extends AbstractEntity
{
    private String name;
    private String category;
    private float prise;
    private int amount;
    private String description;


    public Item()
    {
    }


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getCategory()
    {
        return category;
    }


    public void setCategory(String category)
    {
        this.category = category;
    }


    public float getPrise()
    {
        return prise;
    }


    public void setPrise(float prise)
    {
        this.prise = prise;
    }


    public int getAmount()
    {
        return amount;
    }


    public void setAmount(int amount)
    {
        this.amount = amount;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }
}
