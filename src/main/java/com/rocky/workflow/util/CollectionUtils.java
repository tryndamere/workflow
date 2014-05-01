package com.rocky.workflow.util;

import java.util.Collection;

/**
 * Created by rocky on 14-4-7.
 */
public final class CollectionUtils {

    public static boolean isNotEmpty(Collection collection){
        if (collection != null && collection.size() > 0){
            return true;
        }
        return false;
    }

    public static boolean isEmpty(Collection collection){
        return !isNotEmpty(collection);
    }


}
