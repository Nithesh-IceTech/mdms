package za.co.spsi.toolkit.entity;

/**
 * Created by jaspervdbijl on 2017/02/27.
 */
public class SystemPropertyField<T> extends Field<T> {

    private String name;

    public SystemPropertyField(String name,Entity entity) {
        super(entity);
        this.name = name;
    }

    public String getVariableName() {
        return name;
    }

    @Override
    public T get() {
        if (super.get() == null && System.getProperty(getEntity().getName()+"."+name) != null) {
            setSerial(System.getProperty(name));
        }
        return super.get();
    }

    @Override
    public Entity set(T value) {
        super.set(value);
        System.setProperty(getEntity().getName()+"."+name,getSerial());
        return getEntity();
    }
}
