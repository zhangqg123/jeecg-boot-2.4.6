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
package org.jeecg.modules.qwert.conn.modbus4j.source.msg;

import org.jeecg.modules.qwert.conn.modbus4j.source.base.ModbusUtils;
import org.jeecg.modules.qwert.conn.modbus4j.source.code.FunctionCode;
import org.jeecg.modules.qwert.conn.modbus4j.source.exception.ModbusTransportException;
import org.jeecg.modules.qwert.conn.modbus4j.source.sero.util.queue.ByteQueue;

/**
 * <p>ReportSlaveIdResponse class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class ReportSlaveIdResponse extends ModbusResponse {
    private byte[] data;

    ReportSlaveIdResponse(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    ReportSlaveIdResponse(int slaveId, byte[] data) throws ModbusTransportException {
        super(slaveId);
        this.data = data;
    }

    /** {@inheritDoc} */
    @Override
    public byte getFunctionCode() {
        return FunctionCode.REPORT_SLAVE_ID;
    }

    /** {@inheritDoc} */
    @Override
    protected void readResponse(ByteQueue queue) {
        int numberOfBytes = ModbusUtils.popUnsignedByte(queue);
        if (queue.size() < numberOfBytes)
            throw new ArrayIndexOutOfBoundsException();

        data = new byte[numberOfBytes];
        queue.pop(data);
    }

    /** {@inheritDoc} */
    @Override
    protected void writeResponse(ByteQueue queue) {
        ModbusUtils.pushByte(queue, data.length);
        queue.push(data);
    }

    /**
     * <p>Getter for the field <code>data</code>.</p>
     *
     * @return an array of {@link byte} objects.
     */
    public byte[] getData() {
        return data;
    }
}
