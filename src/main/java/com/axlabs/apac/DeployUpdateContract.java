package com.axlabs.apac;

import io.neow3j.devpack.ByteString;
import io.neow3j.devpack.Hash160;
import io.neow3j.devpack.Runtime;
import io.neow3j.devpack.Storage;
import io.neow3j.devpack.StorageContext;
import io.neow3j.devpack.annotations.DisplayName;
import io.neow3j.devpack.annotations.ManifestExtra;
import io.neow3j.devpack.annotations.OnDeployment;
import io.neow3j.devpack.annotations.Permission;
import io.neow3j.devpack.constants.NativeContract;
import io.neow3j.devpack.contracts.ContractManagement;
import io.neow3j.devpack.events.Event1Arg;


@DisplayName("MyContract")
@ManifestExtra(key = "author", value = "AxLabs")
@Permission(nativeContract = NativeContract.ContractManagement)
public class DeployUpdateContract {

    public static StorageContext context = Storage.getStorageContext();
    public static final int ownerKey = 0x01;

    @DisplayName("SetNewOwner")
    static Event1Arg<Hash160> onOwnershipTransfer;

    // Deploy and update
    @OnDeployment
    public static void deployOrUpdate(Object data, boolean isUpdate) {
        if (isUpdate) {
            // migrate storage, etc.
            return;
        }
        // deployment configuration (here: setting initial owner)
        Storage.put(context, ownerKey, (Hash160) data);
    }

    public static void setOwner(Hash160 newOwner) throws Exception {
        if (!Runtime.checkWitness(getCurrentOwner())) {
            throw new Exception("No authorization.");
        }
        onOwnershipTransfer.fire(newOwner);
        Storage.put(context, ownerKey, newOwner);
    }

    public static Hash160 getCurrentOwner() {
        return Storage.getHash160(context, ownerKey);
    }

    public static void updateContract(ByteString nefFile, String manifest) {
        new ContractManagement().update(nefFile, manifest, null);
    }

}