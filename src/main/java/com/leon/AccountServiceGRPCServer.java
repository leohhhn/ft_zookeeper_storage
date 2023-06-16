package com.leon;


import com.leon.gRPC.*;
import io.grpc.stub.StreamObserver;

import java.io.IOException;


public class AccountServiceGRPCServer extends AccountServiceGrpc.AccountServiceImplBase {

	final AccountService service;
	final AppServer node;
	
	protected AccountServiceGRPCServer(AccountService service, AppServer node) {
		this.service = service;
		this.node = node;
	}
	
	@Override
	public void addAmount(AccountRequest request, StreamObserver<AccountResponse> responseObserver) {
		AccountResponse response;
		if (!node.isLeader()) {
			response = AccountResponse.newBuilder().
					setRequestId(request.getRequestId()).
					setStatus(RequestStatus.UPDATE_REJECTED_NOT_LEADER).
					build();
				}else {
			 
					float amount = service.addAmount(request.getAmount(), true);
					response = AccountResponse.newBuilder().
							setRequestId(request.getRequestId()).
							setStatus(RequestStatus.STATUS_OK).
							setBalance(amount).
							build();
		}
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
	
	@Override
	public void witdrawAmount(AccountRequest request, StreamObserver<AccountResponse> responseObserver) {
		AccountResponse response = null;
		if (!node.isLeader()) {
			response = AccountResponse.newBuilder().
					setRequestId(request.getRequestId()).
					setStatus(RequestStatus.UPDATE_REJECTED_NOT_LEADER).
					build();
				}else {
			
					float amount = service.witdrawAmount(request.getAmount(), true);
					if (amount<0) {
						response = AccountResponse.newBuilder().
								setRequestId(request.getRequestId()).
								setStatus(RequestStatus.WITDRAWAL_REJECT_NOT_SUFFICIENT_AMOUNT).
								build();
					}
					else {
						response = AccountResponse.newBuilder().
								setRequestId(request.getRequestId()).
								setStatus(RequestStatus.STATUS_OK).
								setBalance(amount).
								build();
					}
		}
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
	@Override
	public void getAmount(AccountRequest request, StreamObserver<AccountResponse> responseObserver) {
	     AccountResponse response = service.getAmount(request);
	     
	     responseObserver.onNext(response);
		 responseObserver.onCompleted(); 
	}
	@Override
	public void appendLog(LogEntry request, StreamObserver<LogResponse> responseObserver) {
		byte[] data = request.getLogEntryData().toByteArray();
		LogResponse response;
		
		try {
			response = service.appendLog(request.getEntryAtIndex(), data);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response = LogResponse.newBuilder().
					setStatus(LogStatus.IO_ERROR).
					setEntryAtIndex(request.getEntryAtIndex()).
					build();
		}
		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}
	
	@Override
	public void getLeaderInfo(LeaderRequest req, StreamObserver<LeaderInfo> response) {
		LeaderInfo leader = null;
		if (node.isLeader()) {
			leader = LeaderInfo.newBuilder().
					  setImLeader(true).
					  setHostnamePort(node.getMyGRPCAddress()).
					  build();
		}
		else {
			leader = LeaderInfo.newBuilder().
					  setImLeader(false).
					  setHostnamePort(node.getLeaaderGRPCAddress()).
					  build();
		}
		response.onNext(leader);
		response.onCompleted();
	}
}
