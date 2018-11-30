/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.risksense.controller;

import java.util.Objects;

/**
 *
 * @author GUNALSE
 */
public class Key {

    public static enum Type {
        String, Integer, Boolean, Object, Array
    }
    private final Type type;
    private final String name;

    public Key(Type name, String attribute) {
        this.type = name;
        this.name = attribute;
    }

    public Key(Type name) {
        this.type = name;
        this.name = null;
    }

    public Key(Type name, String... arr) {
        this.type = name;
        this.name = arr.length > 0 ? arr[0] : null;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.type);
        hash = 79 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Key other = (Key) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return this.type == other.type;
    }

    @Override
    public String toString() {
        return "Key{" + "type=" + type + ", name=" + name + '}';
    }

}
