package com.smart.smc.inter.sdport.pay.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.smart.smc.inter.sdport.pay.constants.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author shizhongming
 * 2024/10/29 17:13
 * @since 1.0.0
 */
public class JacksonConverter {

    private JacksonConverter() {
        // do nothing
    }

    /**
     * 订单来源转换器
     */
    public static class ConstantCodeSerializer extends JsonSerializer<ConstantCode> {
        @Override
        public void serialize(ConstantCode constantCode, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (constantCode == null) {
                jsonGenerator.writeString("");
            } else {
                jsonGenerator.writeString(constantCode.getCode());
            }
        }
    }

    /**
     * LocalDateTime转时间戳
     */
    public static class LocalDateTimeTimestampSerializer extends JsonSerializer<LocalDateTime> {

        @Override
        public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (localDateTime == null) {
                jsonGenerator.writeNull();
            } else {
                jsonGenerator.writeNumber(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            }
        }
    }

    public static class BigDecimalStringSerializer extends JsonSerializer<BigDecimal> {

        @Override
        public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (bigDecimal == null) {
                jsonGenerator.writeString("");
            } else {
                jsonGenerator.writeString(bigDecimal.toPlainString());
            }
        }
    }


    public static class CustomerStatusEnumDeserializer extends JsonDeserializer<CustomerStatusEnum> {
        @Override
        public CustomerStatusEnum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            String text = jsonParser.getText();
            if (text == null) {
                return null;
            }
            return CustomerStatusEnum.getByCode(text);
        }
    }

    public static class PayTypeEnumDeserializer extends JsonDeserializer<PayTypeEnum> {
        @Override
        public PayTypeEnum deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
            String text = jsonParser.getText();
            if (text == null) {
                return null;
            }
            return PayTypeEnum.getByCode(text);
        }
    }

    public static class OrderStatusEnumDeserializer extends JsonDeserializer<OrderStatusEnum> {
        @Override
        public OrderStatusEnum deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
            String text = jsonParser.getText();
            if (text == null) {
                return null;
            }
            return OrderStatusEnum.getByCode(text);
        }
    }

    public static class PayChannelEnumDeserializer extends JsonDeserializer<PayChannelEnum> {
        @Override
        public PayChannelEnum deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
            String text = jsonParser.getText();
            if (text == null) {
                return null;
            }
            return PayChannelEnum.getByCode(text);
        }
    }

    public static class PayRefundStatusDeserializer extends JsonDeserializer<PayRefundStatusEnum> {
        @Override
        public PayRefundStatusEnum deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
            String text = jsonParser.getText();
            if (text == null) {
                return null;
            }
            return PayRefundStatusEnum.getByCode(text);
        }
    }
}
