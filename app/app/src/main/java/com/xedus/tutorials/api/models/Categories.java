package com.xedus.tutorials.api.models;

/**
 * Created by PRAV on 21-06-2016.
 */
public class Categories {
    public int id;
    public int count;
    public String name;
    @Override
     public int hashCode(){
      if(name==null)
        return 0;
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        Categories n = (Categories)o;
        if(n==null)
            return false;
        return n.id == this.id;
    }
}