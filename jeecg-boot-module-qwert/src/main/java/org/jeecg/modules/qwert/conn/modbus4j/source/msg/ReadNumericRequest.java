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
import org.jeecg.modules.qwert.conn.modbus4j.source.exception.ModbusTransportException;
import org.jeecg.modules.qwert.conn.modbus4j.source.sero.util.queue.ByteQueue;

/**
 * <p>Abstract ReadNumericRequest class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
abstract public class ReadNumericRequest extends ModbusRequest {
    private int startOffset;
    private int numberOfRegisters;

    /**
     * <p>Constructor for ReadNumericRequest.</p>
     *
     * @param slaveId a int.
     * @param startOffset a int.
     * @param numberOfRegisters a int.
     * @throws ModbusTransportException if any.
     */
    public ReadNumericRequest(int slaveId, int startOffset, int numberOfRegisters) throws ModbusTransportException {
        super(slaveId);
        this.startOffset = startOffset;
        this.numberOfRegisters = numberOfRegisters;
    }

    /** {@inheritDoc} */
    @Override
    public void validate(Modbus modbus) throws ModbusTransportException {
        ModbusUtils.validateOffset(startOffset);
        modbus.validateNumberOfRegisters(numberOfRegisters);
        ModbusUtils.validateEndOffset(startOffset + numberOfRegisters - 1);
    }

    ReadNumericRequest(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    /** {@inheritDoc} */
    @Override
    protected void writeRequest(ByteQueue queue) {
        ModbusUtils.pushShort(queue, startOffset);
        ModbusUtils.pushShort(queue, numberOfRegisters);
    }

    /** {@inheritDoc} */
    @Override
    protected void readRequest(ByteQueue queue) {
        startOffset = ModbusUtils.popUnsignedShort(queue);
        numberOfRegisters = ModbusUtils.popUnsignedShort(queue);
    }

    /**
     * <p>getData.</p>
     *
     * @param processImage a {@link ProcessImage} object.
     * @return an array of {@link byte} objects.
     * @throws ModbusTransportException if any.
     */
    protected byte[] getData(ProcessImage processImage) throws ModbusTransportException {
        short[] data = new short[numberOfRegisters];

        // Get the data from the process image.
        for (int i = 0; i < numberOfRegisters; i++)
            data[i] = getNumeric(processImage, i + startOffset);

        return convertToBytes(data);
    }

    /**
     * <p>getNumeric.</p>
     *
     * @param processImage a {@link ProcessImage} object.
     * @param index a int.
     * @return a short.
     * @throws ModbusTransportException if any.
     */
    abstract protected short getNumeric(ProcessImage processImage, int index) throws ModbusTransportException;

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "ReadNumericRequest [startOffset=" + startOffset + ", numberOfRegisters=" + numberOfRegisters + "]";
    }
}
