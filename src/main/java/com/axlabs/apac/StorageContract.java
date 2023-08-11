package com.axlabs.apac;

import com.axlabs.apac.structs.Feature;
import io.neow3j.devpack.ByteString;
import io.neow3j.devpack.Storage;
import io.neow3j.devpack.StorageContext;
import io.neow3j.devpack.contracts.StdLib;

public class StorageContract {

    public static StorageContext context = Storage.getStorageContext();
    public static StorageContext readOnlyContext = Storage.getReadOnlyContext();

    // Storage methods:
    // put, get, delete, find

    // method put
    public static void putSomething(int key, int value) {
        Storage.put(context, key, value);
    }

    // method get
    public static int getSomething(int key) {
        return Storage.getInt(readOnlyContext, key);
    }
    
    // method put more complex structure
    // String name, int age, int power
    public static void putStruct(int key, Feature feature) {
        // convert feature to neovm compatible type (ByteString)
        ByteString compatibleFeature = new StdLib().serialize(feature);
        Storage.put(context, key, compatibleFeature);
    }

    public static Feature getStruct(int key) {
        ByteString compatibleFeature = Storage.get(readOnlyContext, key);
        return (Feature) new StdLib().deserialize(compatibleFeature);
    }

}
