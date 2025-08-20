package com.sh.patientmanagement.proto;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BillingServiceClient {
    private static final Logger log = LoggerFactory.getLogger(BillingServiceClient.class);
    private final BillingServiceGrpc.BillingServiceBlockingStub stub;
    public BillingServiceClient(
            @Value("${billing.service.address:localhost}") String serviceAddress,
            @Value("${billing.service.grpc.port:9002}") int servicePort
    ){
        log.info("Connecting to Billing Service GRPC service {}:{}", serviceAddress, servicePort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serviceAddress, servicePort).usePlaintext().build();
        stub = BillingServiceGrpc.newBlockingStub( channel);
    }
    public BillingResponse createBillingAccount(String patientId, String name, String email){
        BillingRequest request = BillingRequest.newBuilder().setPatientId(patientId).setName(name).setEmail(email).build();

        BillingResponse response = stub.createBillingAccount(request);
        log.info("Received response from Billing Service GRPC service: {}", response );
        return response;
    }
}
