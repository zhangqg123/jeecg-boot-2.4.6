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

import org.jeecg.modules.qwert.conn.modbus4j.source.code.FunctionCode;
import org.jeecg.modules.qwert.conn.modbus4j.source.exception.ModbusTransportException;

/**
 * <p>ReadDiscreteInputsResponse class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class ReadDiscreteInputsResponse extends ReadResponse {
    ReadDiscreteInputsResponse(int slaveId, byte[] data) throws ModbusTransportException {
        super(slaveId, data);
    }

    ReadDiscreteInputsResponse(int slaveId) throws ModbusTransportException {
        super(slaveId);
    }

    /** {@inheritDoc} */
    @Override
    public byte getFunctionCode() {
        return FunctionCode.READ_DISCRETE_INPUTS;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "ReadDiscreteInputsResponse [exceptionCode=" + exceptionCode + ", slaveId=" + slaveId
                + ", getFunctionCode()=" + getFunctionCode() + ", isException()=" + isException()
                + ", getExceptionMessage()=" + getExceptionMessage() + ", getExceptionCode()=" + getExceptionCode()
                + super.toString(false) + "]";
    }
}
