package de.demothb.util;

import java.beans.PropertyChangeListener;

public interface Observable {

    public void subscribe(PropertyChangeListener pcl);

    public void unsubscribe(PropertyChangeListener pcl);

}
