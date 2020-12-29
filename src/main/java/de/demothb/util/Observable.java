package de.demothb.util;

import java.beans.PropertyChangeListener;

public interface Observable {

    void subscribe(PropertyChangeListener pcl);

    void unsubscribe(PropertyChangeListener pcl);

}
