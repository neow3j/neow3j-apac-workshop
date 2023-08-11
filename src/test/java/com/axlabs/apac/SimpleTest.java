package com.axlabs.apac;

import io.neow3j.contract.SmartContract;
import io.neow3j.protocol.core.response.InvocationResult;
import io.neow3j.test.ContractTest;
import io.neow3j.test.ContractTestExtension;
import io.neow3j.types.StackItemType;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ContractTest(blockTime = 1, contracts = SimpleContract.class)
public class SimpleTest {

    private static SmartContract contract;

    @RegisterExtension
    public static final ContractTestExtension ext = new ContractTestExtension();

    @BeforeAll
    public static void setUp() {
        contract = ext.getDeployedContract(SimpleContract.class);
    }

    @Test
    public void testGet() throws Exception {
        InvocationResult result = contract.callInvokeFunction("get", Arrays.asList()).getInvocationResult();
        assertEquals(result.getStack().size(), 1);
        assertThat(result.getFirstStackItem().getType(), is(StackItemType.BYTE_STRING));
    }

}
