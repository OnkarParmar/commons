/*
 * Copyright (c) 2020. PRM Fincon, Pvt. Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Elmer Fudd <efudd@yoyodyne.com> 
 */

package com.teamteach.commons.connectors.rabbit.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class CoreResponseDeserializer extends JsonDeserializer {
    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctx) throws IOException, JsonProcessingException {
        try {
            Class targetClass = (Class) ctx.findInjectableValue("class", null, null);
            JsonNode node = jp.getCodec().readTree(jp);
            ObjectMapper objMapper = new ObjectMapper();
            objMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return objMapper.treeToValue(node, targetClass);
        } catch (Exception e) {
            log.error("Error Deserializing Core Body :: {} ", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
