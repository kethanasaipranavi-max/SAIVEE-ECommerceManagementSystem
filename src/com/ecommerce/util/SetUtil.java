package com.ecommerce.util;

import java.util.HashSet;
import java.util.Set;

public class SetUtil {

    private final Set<Integer> registeredIds;

    public SetUtil() {
        registeredIds = new HashSet<>();
    }

    public boolean registerId(int id) {

        if (!ECommerceValidator.isValidId(id)) {
            return false;
        }

        return registeredIds.add(id);
    }

    public boolean isRegistered(int id) {

        return registeredIds.contains(id);
    }

    public boolean removeId(int id) {

        return registeredIds.remove(id);
    }

    public int getCount() {

        return registeredIds.size();
    }

    public void clear() {

        registeredIds.clear();
    }
}