package org.jeelee.ui.internal;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.PlatformObject;
import org.jeelee.event.AbstractBean;
import org.jeelee.event.PropertySupport;

@SuppressWarnings("rawtypes")
public class GenericPlatformObject<T, R extends GenericPlatformObject> extends PlatformObject
  implements PropertySupport
{
  public static final String ContentsInitialized = "contents.initialized";
  private boolean isContentsInitialized = false;
  protected T source;
  private R parent;
  protected final List<R> children;
  private AbstractBean beanSupport = new AbstractBean();

public GenericPlatformObject()
  {
    this.children = new ArrayList();
  }
  public GenericPlatformObject(T source) {
    this();
    this.source = source;
  }

public void addChild(R child)
  {
    this.children.add(child);
    child.setParent(this);
  }

  public R getChild(int index) {
    return this.children.get(index);
  }

  public R getParent()
  {
    return this.parent;
  }

  public void setParent(R parent) {
    this.parent = parent;
  }

  public List<R> getChildren()
  {
    return this.children;
  }

  public void clearChildren() {
    this.children.clear();
  }

  public T getSource() {
    return this.source;
  }

  protected void setSource(T source) {
    this.source = source;
  }

  @Override
public String toString()
  {
    return this.source.toString();
  }

  public void remove(R p) {
    this.children.remove(p);
  }

  public boolean isContentsInitialized() {
    return isContentsInitialized;
  }

  public void setContentsInitialized(boolean isContentsInitialized) {
    this.beanSupport.firePropertyChanged("contents.initialized", this.isContentsInitialized, 
      this.isContentsInitialized = isContentsInitialized);
  }

  protected void firePropertyChanged(String propertyName, Object oldValue, Object newValue)
  {
    this.beanSupport.firePropertyChanged(propertyName, oldValue, newValue);
  }

  @Override
public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    this.beanSupport.addPropertyChangeListener(listener);
  }

  @Override
public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    this.beanSupport.addPropertyChangeListener(propertyName, listener);
  }

  @Override
public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    this.beanSupport.removePropertyChangeListener(listener);
  }

  @Override
public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    this.beanSupport.removePropertyChangeListener(propertyName, listener);
  }
}

/* Location:           F:\backup\SystemDesktop\org.jeelee.core_2.1.0.201208282112.jar
 * Qualified Name:     org.jeelee.ui.internal.GenericPlatformObject
 * JD-Core Version:    0.6.0
 */