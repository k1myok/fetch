package com.longriver.netpro.common.activemq.service;

import javax.jms.Destination;

public interface IProductService {

	void sendMessage(Destination destination,final String msg);
	
	void sendMessage(final String msg);
}
