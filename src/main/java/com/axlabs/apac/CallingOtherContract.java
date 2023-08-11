package com.axlabs.apac;

import io.neow3j.devpack.Hash160;
import io.neow3j.devpack.Runtime;
import io.neow3j.devpack.annotations.OnNEP17Payment;
import io.neow3j.devpack.annotations.Permission;
import io.neow3j.devpack.constants.NativeContract;
import io.neow3j.devpack.contracts.GasToken;
import io.neow3j.devpack.contracts.NeoToken;

@Permission(nativeContract = NativeContract.NeoToken)
public class CallingOtherContract {

    public static boolean transfer(Hash160 to, int amount) {
        return new NeoToken().transfer(Runtime.getExecutingScriptHash(), to, amount, null);
    }

    @OnNEP17Payment
    public static void receive(Hash160 from, int amount, Object data) throws Exception {
        // Only accept receiving NEO and GAS
        Hash160 callingScriptHash = Runtime.getCallingScriptHash();
        if (callingScriptHash != new NeoToken().getHash() && callingScriptHash != new GasToken().getHash()) {
            throw new Exception("token not accepted.");
        }
    }
    
}
