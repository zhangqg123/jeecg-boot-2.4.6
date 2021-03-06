/*
    Copyright (C) 2006-2007 Serotonin Software Technologies Inc.
 	@author Matthew Lohbihler
 */
package org.jeecg.modules.qwert.conn.modbus4j.test;

import org.jeecg.modules.qwert.conn.modbus4j.source.ModbusFactory;
import org.jeecg.modules.qwert.conn.modbus4j.source.ModbusMaster;
import org.jeecg.modules.qwert.conn.modbus4j.source.code.DataType;
import org.jeecg.modules.qwert.conn.modbus4j.source.code.RegisterRange;
import org.jeecg.modules.qwert.conn.modbus4j.source.ip.IpParameters;
import org.jeecg.modules.qwert.conn.modbus4j.source.locator.NumericLocator;

/**
 * @author Matthew Lohbihler
 */
public class ReadTest {
    public static void main(String[] args) throws Exception {
        IpParameters ipParameters = new IpParameters();
        // ipParameters.setHost("99.247.60.96");
        // ipParameters.setHost("193.109.41.121");
        //      ipParameters.setHost("10.241.224.195");
        ipParameters.setHost("46.1.154.203");
        ipParameters.setPort(10002);
        ipParameters.setEncapsulated(false);

        ModbusFactory modbusFactory = new ModbusFactory();
        // ModbusMaster master = modbusFactory.createTcpMaster(ipParameters, true);
        ModbusMaster master = modbusFactory.createTcpMaster(ipParameters, false);
        master.setTimeout(8000);
        master.setRetries(0);
        master.init();

        //        for (int i = 1; i < 5; i++) {
        //            System.out.print("Testing " + i + "... ");
        //            System.out.println(master.testSlaveNode(i));
        //        }

        NumericLocator el = new NumericLocator(2, RegisterRange.HOLDING_REGISTER, 0, DataType.FOUR_BYTE_FLOAT);
        NumericLocator fjk = new NumericLocator(2, RegisterRange.HOLDING_REGISTER, 0, DataType.FOUR_BYTE_FLOAT);

        for (int i = 0; i < 3; i++) {
            try {
                System.out.println("el: " + master.getValue(el));
                System.out.println("fjk: " + master.getValue(fjk));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        // try {
        // // ReadCoilsRequest request = new ReadCoilsRequest(2, 65534, 1);
        // ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master
        // .send(new ReadHoldingRegistersRequest(2, 0, 1));
        // System.out.println(response);
        // }
        // catch (Exception e) {
        // e.printStackTrace();
        // }

        // System.out.println(master.scanForSlaveNodes());

        master.destroy();
    }
}
