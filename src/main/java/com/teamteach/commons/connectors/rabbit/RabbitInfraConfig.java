package com.teamteach.commons.connectors.rabbit;

import org.springframework.stereotype.Component;

@Component
public class RabbitInfraConfig {

	static final String RESPONSE_CHANNEL_SUFFIX = "extraction.response";
	static final String REQUEST_CHANNEL_SUFFIX = "extraction.request";
	static final String REQUEST_CHANNEL_SUFFIX_INTERNAL = "extraction.request.internal";
	static final String COORDINATE_RESPONSE_CHANNEL_SUFFIX = "coordinate.response";

}
