package de.demothb.util;

import java.beans.PropertyChangeListener;

public interface Observable {

    public void addPropertyChangeListener(PropertyChangeListener pcl);

    public void removePropertyChangeListener(PropertyChangeListener pcl);

}
