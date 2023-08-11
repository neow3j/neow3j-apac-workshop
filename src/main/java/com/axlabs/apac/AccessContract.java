package com.axlabs.apac;

import io.neow3j.devpack.Hash160;
import io.neow3j.devpack.Runtime;
import io.neow3j.devpack.Storage;
import io.neow3j.devpack.StorageContext;
import io.neow3j.devpack.StringLiteralHelper;
import io.neow3j.devpack.annotations.OnVerification;

public class AccessContract {

    public static StorageContext context = Storage.getStorageContext();
    public static Hash160 admin = StringLiteralHelper.addressToScriptHash("NfjhUeupSQvxWWDM4LiKt4im8L63Uim6sj");
    
    private static boolean hasAccess(Hash160 scriptHash) {
        return Runtime.checkWitness(scriptHash);
    }

    public static void putSomething(int key, int value) throws Exception {
        if (!hasAccess(admin)) {
            throw new Exception("No authorization.");
        }
        Storage.put(context, key, value);
    }

    @OnVerification
    public static boolean verify() {
        return hasAccess(admin);
    }

    // CheckWitness cases, if scriptHash is...
    // - a normal address (private-public key pair) -> true if tx has hash160 as signer
    // - a multi-sig -> true if tx has enough signatures for multi-sig
    // - another contract X
    //   -> is X calling this method? -> if yes, true
    //   -> otherwise, OnVerification() is invoked on contract X returns true or false

}
