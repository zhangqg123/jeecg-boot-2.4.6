/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package org.jeecg.modules.qwert.conn.modbus4j.test;

import org.jeecg.modules.qwert.conn.modbus4j.source.BatchRead;
import org.jeecg.modules.qwert.conn.modbus4j.source.BatchResults;
import org.jeecg.modules.qwert.conn.modbus4j.source.ModbusFactory;
import org.jeecg.modules.qwert.conn.modbus4j.source.ModbusMaster;
import org.jeecg.modules.qwert.conn.modbus4j.source.code.DataType;
import org.jeecg.modules.qwert.conn.modbus4j.source.exception.ErrorResponseException;
import org.jeecg.modules.qwert.conn.modbus4j.source.ip.IpParameters;
import org.jeecg.modules.qwert.conn.modbus4j.source.locator.BaseLocator;

/**
 * @author Matthew Lohbihler
 */
public class MasterTest2 {
    public static void main(String[] args) throws Exception {
        IpParameters ipParameters = new IpParameters();
        ipParameters.setHost("46.1.154.203");
        // ipParameters.setHost("99.247.60.96");
        // ipParameters.setHost("193.109.41.121");
        //ipParameters.setHost("141.211.194.29");
        ipParameters.setPort(10002);

        ModbusFactory modbusFactory = new ModbusFactory();
        // ModbusMaster master = modbusFactory.createTcpMaster(ipParameters, true);
        ModbusMaster master = modbusFactory.createTcpMaster(ipParameters, false);
        master.setTimeout(4000);
        master.setRetries(1);

        BatchRead<Integer> batch = new BatchRead<Integer>();
        //        batch.addLocator(0, new ModbusLocator(1, RegisterRange.COIL_STATUS, 2048, DataType.BINARY));
        //        batch.addLocator(1, new ModbusLocator(1, RegisterRange.COIL_STATUS, 2049, DataType.BINARY));
        //        batch.addLocator(2, new ModbusLocator(1, RegisterRange.COIL_STATUS, 2050, DataType.BINARY));
        //        batch.addLocator(3, new ModbusLocator(1, RegisterRange.COIL_STATUS, 3584, DataType.BINARY));
        //        batch.addLocator(4, new ModbusLocator(1, RegisterRange.COIL_STATUS, 3585, DataType.BINARY));
        //        batch.addLocator(5, new ModbusLocator(1, RegisterRange.COIL_STATUS, 3648, DataType.BINARY));
        //        batch.addLocator(6, new ModbusLocator(1, RegisterRange.COIL_STATUS, 3649, DataType.BINARY));
        //        batch.addLocator(7, new ModbusLocator(1, RegisterRange.COIL_STATUS, 3650, DataType.BINARY));
        //        batch.addLocator(8, new ModbusLocator(1, RegisterRange.COIL_STATUS, 3651, DataType.BINARY));
        //        batch.addLocator(9, new ModbusLocator(1, RegisterRange.COIL_STATUS, 3652, DataType.BINARY));
        //        batch.addLocator(10, new ModbusLocator(1, RegisterRange.COIL_STATUS, 3653, DataType.BINARY));
        //        batch.addLocator(11, new ModbusLocator(1, RegisterRange.COIL_STATUS, 3654, DataType.BINARY));
        //        batch.addLocator(12, new ModbusLocator(1, RegisterRange.COIL_STATUS, 3661, DataType.BINARY));
        //        batch.addLocator(13, new ModbusLocator(1, RegisterRange.COIL_STATUS, 3662, DataType.BINARY));
        //        batch.addLocator(15, new ModbusLocator(1, RegisterRange.COIL_STATUS, 3665, DataType.BINARY));
        //        batch.addLocator(16, new ModbusLocator(1, RegisterRange.COIL_STATUS, 3668, DataType.BINARY));
        //        batch.addLocator(18, new ModbusLocator(1, RegisterRange.COIL_STATUS, 3969, DataType.BINARY));

        batch.addLocator(0, BaseLocator.holdingRegister(2, 0, DataType.TWO_BYTE_INT_SIGNED));
        batch.addLocator(1, BaseLocator.holdingRegister(2, 202, DataType.EIGHT_BYTE_INT_SIGNED));

        try {
            master.init();

            while (true) {
                batch.setContiguousRequests(false);
                BatchResults<Integer> results = master.send(batch);
                System.out.println(results.getValue(0));
                System.out.println(results.getValue(1));

                Thread.sleep(2000);
            }
        }
        catch (ErrorResponseException e) {
            System.out.println(e.getErrorResponse().getExceptionMessage());
        }
        finally {
            master.destroy();
        }
    }
}
