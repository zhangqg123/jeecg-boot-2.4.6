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

import org.jeecg.modules.qwert.conn.modbus4j.source.Modbus;
import org.jeecg.modules.qwert.conn.modbus4j.source.ProcessImage;
import org.jeecg.modules.qwert.conn.modbus4j.source.base.ModbusUtils;
import org.jeecg.modules.qwert.conn.modbus4j.source.code.FunctionCode;
import org.jeecg.modules.qwert.conn.modbus4j.source.exception.ModbusTransportException;
import org.jeecg.modules.qwert.conn.modbus4j.source.sero.util.queue.ByteQueue;

/**
 * <p>WriteCoilsRequest class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class WriteCoilsRequest extends ModbusRequest {
    private int startOffset;
    private int numberOfBits;
    private byte[] data;

    /**
     * <p>Constructor for WriteCoilsRequest.</p>
     *
     * @param slaveId a int.
     * @param startOffset a int.
     * @param bdata an array of {@link boolean} objects.
     * @throws ModbusTransportException if any.
     */
    public WriteCoilsRequest(int slaveId, int startOffset, boolean[] bdata) throws ModbusTransportException {
        super(slaveId);
        this.startOffset = startOffset;
        numberOfBits = bdata.length;
        data = convertToBytes(bdata);
    }

    /** {@inheritDoc} */
    @Override
    public void validate(Modbus modbus) throws ModbusTransportException {
        ModbusUtils.validateOffset(startOffset);
        modbus.validateNumberOfBits(numberOfBits);
        ModbusUtils.validateEndOffset(startOffset + numberOfBits - 1);
    }

    WriteCoilsRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    /** {@inheritDoc} */
    @Override
    protected void writeRequest(ByteQueue queue) {
        ModbusUtils.pushShort(queue, startOffset);
        ModbusUtils.pushShort(queue, numberOfBits);
        ModbusUtils.pushByte(queue, data.length);
        queue.push(data);
    }

    @Override
    ModbusResponse handleImpl(ProcessImage processImage) throws ModbusTransportException {
        boolean[] bdata = convertToBooleans(data);
        for (int i = 0; i < numberOfBits; i++)
            processImage.writeCoil(startOffset + i, bdata[i]);
        return new WriteCoilsResponse(slaveId, startOffset, numberOfBits);
    }

    /** {@inheritDoc} */
    @Override
    public byte getFunctionCode() {
        return FunctionCode.WRITE_COILS;
    }

    @Override
    ModbusResponse getResponseInstance(int slaveId) throws ModbusTransportException {
        return new WriteCoilsResponse(slaveId);
    }

    /** {@inheritDoc} */
    @Override
    protected void readRequest(ByteQueue queue) {
        startOffset = ModbusUtils.popUnsignedShort(queue);
        numberOfBits = ModbusUtils.popUnsignedShort(queue);
        data = new byte[ModbusUtils.popUnsignedByte(queue)];
        queue.pop(data);
    }
}
