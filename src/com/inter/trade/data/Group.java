/**
 * Copyright 2009 Joe LaPenna
 */

package com.inter.trade.data;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Joe LaPenna (joe@joelapenna.com)
 */
public class Group<T extends SunType> extends ArrayList<T> implements SunType {

    private static final long serialVersionUID = 1L;

    private String mType;
    
    public Group() {
        super();
    }
    
    public Group(Collection<T> collection) {
        super(collection);
    }

    public void setType(String type) {
        mType = type;
    }

    public String getType() {
        return mType;
    }
}
