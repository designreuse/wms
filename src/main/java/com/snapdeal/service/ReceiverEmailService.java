package com.snapdeal.service;

import org.springframework.stereotype.Service;

import com.snapdeal.entity.ReceiverEmail;

@Service
public interface ReceiverEmailService {
	
	public ReceiverEmail getReceiver(String code);
	public void saveOrUpdate(ReceiverEmail receiverEmail);
	public Boolean checkReceiever(String code);
}
