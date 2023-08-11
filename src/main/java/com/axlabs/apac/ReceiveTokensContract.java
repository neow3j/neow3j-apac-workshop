package com.axlabs.apac;

import io.neow3j.devpack.ByteString;
import io.neow3j.devpack.Hash160;
import io.neow3j.devpack.annotations.OnNEP11Payment;
import io.neow3j.devpack.annotations.OnNEP17Payment;

public class ReceiveTokensContract {

    // receiving NEP-17 tokens
    @OnNEP17Payment
    public static void receive(Hash160 from, int amount, Object data) {
        // accept receiving any NEP-17 token
    }

    // receiving NEP-11 tokens
    @OnNEP11Payment
    public static void receiveNft(Hash160 from, int amount, ByteString tokenId, Object data){
        // accept receiving any NFT
    }

}
