package com.axlabs.apac;

import io.neow3j.contract.GasToken;
import io.neow3j.contract.NeoToken;
import io.neow3j.contract.SmartContract;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.response.InvocationResult;
import io.neow3j.protocol.core.response.NeoSendRawTransaction;
import io.neow3j.protocol.core.stackitem.StackItem;
import io.neow3j.test.ContractTest;
import io.neow3j.test.ContractTestExtension;
import io.neow3j.transaction.AccountSigner;
import io.neow3j.types.ContractParameter;
import io.neow3j.types.Hash256;
import io.neow3j.types.NeoVMStateType;
import io.neow3j.utils.Await;
import io.neow3j.wallet.Account;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ContractTest(blockTime = 1, contracts = StorageContract.class, batchFile = "setup.batch")
public class StorageTest {
    private static Neow3j neow3j;
    private static SmartContract contract;
    private static Account alice;

    @RegisterExtension
    public static final ContractTestExtension ext = new ContractTestExtension();

    @BeforeAll
    public static void setUp() throws Exception {
        contract = ext.getDeployedContract(StorageContract.class);
        neow3j = ext.getNeow3j();
        alice = ext.getAccount(Helper.Alice);
    }

    @Test
    public void testStruct() throws Throwable {
        String name = "bob";
        BigInteger age = new BigInteger("24");
        BigInteger power = new BigInteger("120");
        NeoSendRawTransaction response =
                contract.invokeFunction("putStruct",
                                ContractParameter.integer(400),
                                ContractParameter.array(name, age, power))
                        .signers(AccountSigner.none(alice))
                        .sign()
                        .send();

        assertFalse(response.hasError());
        Hash256 txHash = response.getSendRawTransaction().getHash();
        Await.waitUntilTransactionIsExecuted(txHash, ext.getNeow3j());

        NeoVMStateType state = neow3j.getApplicationLog(txHash).send()
                .getApplicationLog().getFirstExecution().getState();
        assertThat(state, is(NeoVMStateType.HALT));

        InvocationResult result =
                contract.callInvokeFunction("getStruct", Arrays.asList(ContractParameter.integer(400)))
                        .getInvocationResult();
        List<StackItem> struct = result.getFirstStackItem().getList();
        assertThat(struct.get(0).getString(), is(name));
        assertThat(struct.get(1).getInteger(), is(age));
        assertThat(struct.get(2).getInteger(), is(power));
    }

}
