package za.co.spsi.mdms.web.gui.layout;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextArea;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.db.AbstractPhotoEntity;
import za.co.spsi.toolkit.crud.gui.custom.ImageGalleryCrud;
import za.co.spsi.toolkit.db.DataSourceDB;

import static za.co.spsi.toolkit.crud.gui.render.AbstractView.getLocaleValue;

/**
 * Created by jaspervdb on 2016/05/05.
 */
public class IceImageCrudGallery extends ImageGalleryCrud {

    private Button editNote;
    private boolean editable = true;

    public IceImageCrudGallery() {
        super();
        editNote = addAction(FontAwesome.EDIT, new Runnable() {
            @Override
            public void run() {
                // edit the current note - assume its a abstract note field
                if (getSelectedEntity() != null) {
                    // show the note
                    final AbstractPhotoEntity photo = (AbstractPhotoEntity) getSelectedEntity();
                    TextArea textArea = new TextArea(null,photo.notes.getAsString());
                    textArea.setEnabled(editable);
                    MessageBox.createInfo().withCaption(getLocaleValue(MdmsLocaleId.NOTES))
                            .withMessage(textArea)
                            .withCancelButton(ButtonOption.caption(getLocaleValue(ToolkitLocaleId.CANCEL).toUpperCase()), ButtonOption.closeOnClick(true))
                            .withOkButton(new Runnable() {
                                @Override
                                public void run() {
                                    if (editable) {
                                        // save the entity)
                                        photo.notes.set(textArea.getValue());
                                        DataSourceDB.set(getLayout().getDataSource(), photo);
                                    }
                                }
                            },ButtonOption.caption(getLocaleValue(MdmsLocaleId.OK).toUpperCase()),ButtonOption.closeOnClick(true))
                            .open();
                }
            }
        });
    }

    @Override
    public void setEditable(boolean enabled) {
        super.setEditable(enabled);
        this.editable = enabled;
        editNote.setIcon(enabled?FontAwesome.EDIT:FontAwesome.EYE);
        editNote.setEnabled((entities.isEmpty() || !entities.isEmpty()&&entities.get(0) instanceof AbstractPhotoEntity));
    }
}
