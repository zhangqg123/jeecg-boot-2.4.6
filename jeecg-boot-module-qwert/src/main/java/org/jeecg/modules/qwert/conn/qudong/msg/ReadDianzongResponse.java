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
package org.jeecg.modules.qwert.conn.qudong.msg;

import lombok.SneakyThrows;
import org.jeecg.modules.qwert.conn.qudong.base.QwertAsciiUtils;
import org.jeecg.modules.qwert.conn.qudong.base.QwertUtils;
import org.jeecg.modules.qwert.conn.qudong.code.FunctionCode;
import org.jeecg.modules.qwert.conn.qudong.exception.QudongTransportException;
import org.jeecg.modules.qwert.conn.qudong.sero.util.queue.ByteQueue;

/**
 * <p>ReadDianzongResponse class.</p>
 *
 * @author Matthew Lohbihler
 * @version 5.0.0
 */
public class ReadDianzongResponse extends ReadResponse {
	private byte[] data;
    public ReadDianzongResponse(int slaveId, byte[] data) throws QudongTransportException {
        super(slaveId, data);
        this.data=data;
    }

    public ReadDianzongResponse(int slaveId) throws QudongTransportException {
        super(slaveId);
    }

    /** {@inheritDoc} */
    @Override
    public byte getFunctionCode() {
        return FunctionCode.READ_DIANZONG_REGISTERS;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "ReadDianzongResponse [exceptionCode=" + exceptionCode + ", slaveId=" + slaveId
                + ", getFunctionCode()=" + getFunctionCode() + ", isException()=" + isException()
                + ", getExceptionMessage()=" + getExceptionMessage() + ", getExceptionCode()=" + getExceptionCode()
                + ", toString()=" + super.toString(true) + "]";
    }

	
	public byte[] getRetData() {
		// TODO Auto-generated method stub
		return data;
	}

	
    @Override
    final protected void writeImpl(ByteQueue queue) {
    	if(simulator==0) {
	    	byte[] bur = new byte[2];
			queue.pop(bur,0,2);
	  		queue.push("02");
			queue.push("01");
			queue.push(bur);
	  		queue.push("06");
			queue.push("00");
	    	queue.push("04");
	   		queue.push("07");
	        String lenid = chkLength(48);
	        byte[] tmp = lenid.toUpperCase().getBytes();
	   		queue.push(tmp);
			byte[] rd = getRetData();
			queue.push(rd);
//			QwertAsciiUtils.getAsciiData(queue,2);
		}else {
            writeResponse(queue);
		}
    }
	
	public static String chkLength(int value){
		byte a1 = (byte) (value & 0xf);
		byte a2 = (byte) ((value>>4) & 0xf);
		byte a3 = (byte) ((value>>8) & 0xf);
		int sum = a1+a2+a3;
		sum=((~sum%0x10000+1)& 0xf)<<12 | (value&0xffff);
		return Integer.toHexString(sum);
	}

    /** {@inheritDoc} */
    @SneakyThrows
	@Override
    protected void readResponse(ByteQueue queue) {
		queue=getUnDianzongMessage(queue);
    	if(simulator==0) {
	    	int tmpnumber = ((((queue.peek(4))<<8) & 0xff00) | ((queue.peek(5))&0x00ff))&0x0fff;
	    	int numberOfBytes = tmpnumber/2;
	  //      int numberOfBytes = QwertUtils.popUnsignedByte(queue);
	        if (queue.size() < numberOfBytes)
	            throw new ArrayIndexOutOfBoundsException();
// 下面是正式场景代码
	        data = new byte[numberOfBytes];
	        queue.pop(7);
	        queue.pop(data);
//	模拟数据
//			byte tmpbyte = QwertUtils.hex2byte("de");
//			data=new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,7,tmpbyte,0,0,0,0,0,0,0,0,0};
//			data=new byte[]{1,9,0,0,1,1,1,0,0,0,2};
        }else {
            int numberOfBytes = QwertUtils.popUnsignedByte(queue);
            if (queue.size() < numberOfBytes)
                throw new ArrayIndexOutOfBoundsException();

            data = new byte[numberOfBytes];
            queue.pop(data);
        }
    }

	public ByteQueue getUnDianzongMessage(ByteQueue queue) throws QudongTransportException {
		// Validate that the message starts with the required indicator
		byte b = queue.pop();
		if (b != QwertAsciiUtils.START)
			throw new QudongTransportException("Invalid message start: " + b);

		// Find the end indicator
		int end = queue.indexOf(QwertAsciiUtils.END);
		if (end == -1)
			throw new ArrayIndexOutOfBoundsException();

		// Remove the message from the queue, leaving the LRC there
		byte[] asciiBytes = new byte[end - 4];
		queue.pop(asciiBytes);
		ByteQueue msgQueue = new ByteQueue(asciiBytes);

		// Pop off the LRC
		short givenLrc = QwertAsciiUtils.readAscii2(queue);

		// Pop the end indicator off of the queue
		queue.pop(QwertAsciiUtils.END.length);


		// Check the LRC
		int calcLrc = QwertAsciiUtils.calculateDRC(msgQueue);
		if (calcLrc != givenLrc)
			throw new QudongTransportException("LRC mismatch: given=" + (givenLrc & 0xff) + ", calc="
					+ (calcLrc & 0xff));
		// Convert to unascii
		QwertAsciiUtils.fromAscii(msgQueue, msgQueue.size());

		return msgQueue;
	}

    public short[] getShortData() {
      return convertToShorts(data);
  }
	
}
