/*
 * ============================================================================
 * GNU General Public License
 * ============================================================================
 *
 * Copyright (C) 2006-2011 Serotonin Software Technologies Inc. http://serotoninsoftware.com
 * @author Matthew Lohbihler
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jeecg.modules.qwert.conn.modbus4j.source.ip.xa;

import org.jeecg.modules.qwert.conn.modbus4j.source.base.ModbusUtils;
import org.jeecg.modules.qwert.conn.modbus4j.source.exception.ModbusTransportException;
import org.jeecg.modules.qwert.conn.modbus4j.source.msg.ModbusRequest;
import org.jeecg.modules.qwert.conn.modbus4j.source.sero.messaging.IncomingRequestMessage;
import org.jeecg.modules.qwert.conn.modbus4j.source.sero.messaging.OutgoingRequestMessage;
import org.jeecg.modules.qwert.conn.modbus4j.source.sero.util.queue.ByteQueue;

/**
 * <p>XaMessageRequest class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class XaMessageRequest extends XaMessage implements OutgoingRequestMessage, IncomingRequestMessage {
    static XaMessageRequest createXaMessageRequest(ByteQueue queue) throws ModbusTransportException {
        // Remove the XA header
        int transactionId = ModbusUtils.popShort(queue);
        int protocolId = ModbusUtils.popShort(queue);
        if (protocolId != ModbusUtils.IP_PROTOCOL_ID)
            throw new ModbusTransportException("Unsupported IP protocol id: " + protocolId);
        ModbusUtils.popShort(queue); // Length, which we don't care about.

        // Create the modbus response.
        ModbusRequest request = ModbusRequest.createModbusRequest(queue);
        return new XaMessageRequest(request, transactionId);
    }

    /**
     * <p>Constructor for XaMessageRequest.</p>
     *
     * @param modbusRequest a {@link ModbusRequest} object.
     * @param transactionId a int.
     */
    public XaMessageRequest(ModbusRequest modbusRequest, int transactionId) {
        super(modbusRequest, transactionId);
    }

    /** {@inheritDoc} */
    @Override
    public boolean expectsResponse() {
        return modbusMessage.getSlaveId() != 0;
    }

    /**
     * <p>getModbusRequest.</p>
     *
     * @return a {@link ModbusRequest} object.
     */
    public ModbusRequest getModbusRequest() {
        return (ModbusRequest) modbusMessage;
    }
}
