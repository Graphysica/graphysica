package org.graphysica.vue;

import com.sun.istack.internal.NotNull;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.scene.AccessibleAttribute;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;

/**
 * Implementation of a {@code SplitMenuButton} that can be toggled.
 * <p>
 * From a StackOverflow answer by wcmatthysen:
 * https://stackoverflow.com/questions/49812227/javafx-toggle-menubutton
 * @author wcmatthyseb https://stackoverflow.com/users/475044/wcmatthysen
 */
public class ToggleSplitMenuButton extends SplitMenuButton {

    private static final PseudoClass PSEUDO_CLASS_SELECTED 
            = PseudoClass.getPseudoClass("selected");
    
    /**
     * Indicates whether this toggle split menu button is selected. This can be
     * manipulated programmatically.
     */
    private BooleanProperty selected;

    public ToggleSplitMenuButton() {
    }

    public ToggleSplitMenuButton(@NotNull final MenuItem ... items) {
        super(items);
    }

    public final void setSelected(final boolean value) {
        selectedProperty().set(value);
    }

    public final boolean isSelected() {
        return selected == null ? false : selected.get();
    }

    public final BooleanProperty selectedProperty() {
        if (selected == null) {
            selected = new BooleanPropertyBase() {

                @Override
                protected void invalidated() {
                    final boolean selected = get();
                    pseudoClassStateChanged(PSEUDO_CLASS_SELECTED, selected);
                    notifyAccessibleAttributeChanged(
                            AccessibleAttribute.SELECTED);
                }

                @Override
                public Object getBean() {
                    return ToggleSplitMenuButton.this;
                }

                @Override
                public String getName() {
                    return "selected";
                }
            };
        }
        return selected;
    }

    @Override
    public void fire() {
        if (!isDisabled()) {
            setSelected(!isSelected());
            fireEvent(new ActionEvent());
        }
    }
}